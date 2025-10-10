package com.example.user_srv.service;

import com.example.user_srv.constant_.Constants;
import com.example.user_srv.exception.AppException;
import com.example.user_srv.exception.ERROR_CODE;
import com.example.user_srv.dto.request.AuthenticationRequest;
import com.example.user_srv.dto.request.RefreshRequest;
import com.example.user_srv.dto.request.ValidTokenRequest;
import com.example.user_srv.dto.response.AuthenticationResponse;
import com.example.user_srv.dto.response.ValidTokenResponse;
import com.example.user_srv.entity.Token;
import com.example.user_srv.entity.User;
import com.example.user_srv.repository.TokenRepository;
import com.example.user_srv.repository.UserRepository;
import com.example.user_srv.repository.custom.CustomUserRepository;
import com.example.user_srv.security.TokenService;
import com.example.user_srv.utils.AESHelper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    private static final Long NUMBER_OF_FAIL_LOGIN = 5L;

    UserRepository userRepository;
    TokenRepository tokenRepository;
    CustomUserRepository customUserRepository;
    AESHelper aesHelper;
    TokenService tokenService;

    public ValidTokenResponse verifyToken(ValidTokenRequest request) throws ParseException, JOSEException {
        boolean isValid = true;
        try {
            tokenService.verifyToken(request.getToken(), false);
        } catch (AppException e) {
            isValid = false;
        }
        return ValidTokenResponse.builder().valid(isValid).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, Optional<Long> roleId) {
        // Lấy user từ DB
        User user = userRepository
                .findByUsernameAndIsActiveAndIsBlock(
                        request.getUsername(),
                        Constants.DELETE.NORMAL,
                        Constants.IS_BLOCK.FALSE)
                .orElseThrow(() -> new AppException(ERROR_CODE.USER_NOT_EXISTED));

        // 1. Lấy plain password từ client
        String plainPasswordFromClient = request.getPassword(); // "123456"

        // 2. Decrypt password từ database (AES encrypted)
        String decryptedPasswordFromDB;
        try {
            decryptedPasswordFromDB = aesHelper.decrypt(user.getPassword());
            log.info("Successfully decrypted password from database");
        } catch (Exception e) {
            log.error("Failed to decrypt password from database: {}", e.getMessage());
            throw new AppException(ERROR_CODE.INVALID_AUTH);
        }

        // 3. So sánh plain text passwords
        boolean authenticated = plainPasswordFromClient.equals(decryptedPasswordFromDB);

        user.setIsFirstLogin(user.getIsFirstLogin() == null ? true : false);

        if (!authenticated) {
            // Tăng số lần login fail
            user.setFailLogin(user.getFailLogin() != null ? user.getFailLogin() + 1L : 1L);
            if (user.getFailLogin().equals(NUMBER_OF_FAIL_LOGIN)) {
                user.setIsBlock(Constants.IS_BLOCK.TRUE);
            }
            customUserRepository.save(user);

            throw new AppException(ERROR_CODE.INVALID_AUTH);
        }

        // Reset số lần login fail nếu đăng nhập thành công
        if (user.getFailLogin() != null && user.getFailLogin() > 0) {
            user.setFailLogin(0L);
            customUserRepository.save(user);
        }

        // Tạo token
        var accessToken = tokenService.generateToken(user, true, roleId);
        var refreshToken = tokenService.generateToken(user, false, roleId);

        return AuthenticationResponse.builder()
                .accessToken(accessToken.token())
                .refreshToken(refreshToken.token())
                .isFirstLogin(user.getIsFirstLogin())
                .expiryTime(accessToken.expiryDate())
                .build();
    }

    public void logout(HttpServletRequest servletRequest) throws ParseException, JOSEException {
        try {
            String token = tokenService.extractToken(servletRequest);
            SignedJWT signedJWT = tokenService.verifyToken(token, true);
            String jit = signedJWT.getJWTClaimsSet().getJWTID();

            Token invalidatedToken = Token.builder()
                    .id(jit)
                    .revoked(true)
                    .expired(true)
                    .build();
            tokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            throw new AppException(ERROR_CODE.INVALID_TOKEN);
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = tokenService.verifyToken(request.getToken(), true);
        String jit = signedJWT.getJWTClaimsSet().getJWTID();

        Token invalidatedToken = Token.builder()
                .id(jit)
                .revoked(true)
                .expired(true)
                .build();
        tokenRepository.save(invalidatedToken);

        Long userId = Long.parseLong(signedJWT.getJWTClaimsSet().getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ERROR_CODE.UNAUTHENTICATED));

        var accessToken = tokenService.generateToken(user, true, Optional.empty());
        var refreshToken = tokenService.generateToken(user, false, Optional.empty());

        return AuthenticationResponse.builder()
                .accessToken(accessToken.token())
                .refreshToken(refreshToken.token())
                .expiryTime(accessToken.expiryDate())
                .build();
    }

}
