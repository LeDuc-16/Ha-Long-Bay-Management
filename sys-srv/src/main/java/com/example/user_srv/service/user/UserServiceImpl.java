package com.example.user_srv.service.user;

import com.example.user_srv.constant_.Constants;
import com.example.user_srv.dto.SimplePage;
import com.example.user_srv.dto.request.user.UserCreationRequest;
import com.example.user_srv.dto.request.user.UserSearchRequest;
import com.example.user_srv.dto.response.UserCreationResponse;
import com.example.user_srv.dto.response.UserGetResponse;
import com.example.user_srv.entity.Role;
import com.example.user_srv.entity.User;
import com.example.user_srv.exception.AppException;
import com.example.user_srv.exception.ERROR_CODE;
import com.example.user_srv.mapper.UserMapper;
import com.example.user_srv.repository.RoleRepository;
import com.example.user_srv.repository.UserRepository;
import com.example.user_srv.security.TokenService;
import com.example.user_srv.utils.DateUtils;
import com.example.user_srv.utils.H;
import com.example.user_srv.utils.QueryUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    TokenService tokenService;

    @Override
    public SimplePage<UserGetResponse> getAll(UserSearchRequest request) {
        Specification<User> spec = (root, query, cb) -> QueryUtils.and(cb,
                QueryUtils.buildLikeFilter(root, cb, request.getUsername(), "username"),
                QueryUtils.buildLikeFilter(root, cb, request.getFullName(), "fullName"),
                QueryUtils.buildLikeFilter(root, cb, request.getEmail(), "email"),
                QueryUtils.buildLikeFilter(root, cb, request.getPhoneNumber(), "phoneNumber"),
                QueryUtils.buildEqFilter(root, cb, "gender", request.getGender()),
                QueryUtils.buildEqFilter(root, cb, "id", request.getId()),
                request.getIsFirstLogin() != null ? cb.equal(root.get("isFirstLogin"), request.getIsFirstLogin()) : null,
                QueryUtils.buildLikeFilter(root, cb, request.getSearchKeyword(), "username", "fullName", "email"),
                H.isTrue(request.getRoleIds()) ? root.join("roles").get("id").in(request.getRoleIds()) : null,
                request.getIsActive() != null ? cb.equal(root.get("isActive"), request.getIsActive()) : null,
                cb.equal(root.get("isActive"), Constants.DELETE.NORMAL)
        );

        // lấy page & size từ request body
        int page = request.getPage() != null ? request.getPage().intValue() : 0;
        int size = request.getSize() != null ? request.getSize().intValue() : 10;
        Pageable pageable = PageRequest.of(page, size);

        // gọi repo với spec + pageable
        Page<User> usersPage = userRepository.findAll(spec, pageable);

        // convert sang response DTO
        List<UserGetResponse> response = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .toList();

        return new SimplePage<>(response, usersPage.getTotalElements(),pageable);
    }

    @Override
    public UserCreationResponse createUser(UserCreationRequest request) {
        // 1. Kiểm tra username tồn tại
        userRepository.findByUsernameAndIsActive(request.getUsername(), Constants.DELETE.NORMAL).ifPresent(user -> {
            throw new AppException(ERROR_CODE.USER_EXISTED);
        });

        User user = userMapper.toCreateUser(request);
        user.setIsBlock(Constants.IS_BLOCK.FALSE);
        Set<Role> roles = roleRepository.findAllByIds(request.getRoleIds());
        if (roles.isEmpty() || roles.size() != request.getRoleIds().size()) {
            throw new AppException(ERROR_CODE.ROLE_NOT_EXISTED);
        }
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        DateUtils.isValidTimeRange(request.getDob(), DateUtils.SHORT_TIME_PATTERN);
        user.setDob(DateUtils.stringToLocalDateTime(request.getDob(), DateUtils.SHORT_TIME_PATTERN));
        user.setIsFirstLogin(true);
        user = userRepository.save(user);

        var response = userMapper.toUserCreationResponse(user);

        return response;
    }

    @Override
    public UserGetResponse getMyInfo() {
        Long userId = tokenService.getUserIdFromContext();
        return this.getUserById(userId);
    }

    @Override
    public UserGetResponse getUserById(Long id) {
        User user = this.findById(id);

        UserGetResponse response = userMapper.toDto(user);
        response.setDob(DateUtils.localDateTimeToString(user.getDob(), DateUtils.SHORT_TIME_PATTERN));
        return response;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findByIdAndIsActive(id, Constants.DELETE.NORMAL)
                .orElseThrow(() -> new AppException(ERROR_CODE.USER_NOT_EXISTED));
    }
}
