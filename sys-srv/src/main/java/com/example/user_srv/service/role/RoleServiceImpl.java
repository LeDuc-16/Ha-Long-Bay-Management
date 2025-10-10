package com.example.user_srv.service.role;

import com.example.user_srv.constant_.Constants;
import com.example.user_srv.dto.SimplePage;
import com.example.user_srv.dto.request.RoleRequest;
import com.example.user_srv.dto.response.RoleResponse;
import com.example.user_srv.dto.response.RoleResponseById;
import com.example.user_srv.entity.Menu;
import com.example.user_srv.entity.Permission;
import com.example.user_srv.entity.Role;
import com.example.user_srv.mapper.RoleMapper;
import com.example.user_srv.repository.MenuRepository;
import com.example.user_srv.repository.PermissionRepository;
import com.example.user_srv.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {
    RoleMapper roleMapper;
    MenuRepository menuRepository;
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;

    @Override
    public RoleResponseById createRole(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);

        Set<Menu> menus = menuRepository.findAllByIdsAndIsActive(roleRequest.getMenuIds());
        role.setMenus(menus);

        Set<Permission> permissions = permissionRepository.findAllByIdInAndIsActive(roleRequest.getPermissionIds(), Constants.DELETE.NORMAL);
        role.setPermissions(permissions);

        role.setIsActive(roleRequest.getIsActive());

        role = roleRepository.save(role);
        return roleMapper.toRoleResponseById(role);
    }

    @Override
    public RoleResponseById updateRole(RoleRequest roleRequest) {
        return null;
    }

    @Override
    public boolean deleteRole(Long id) {
        return false;
    }

    @Override
    public SimplePage<RoleResponse> getAllRole(RoleRequest roleRequest, Pageable pageable) {
        return null;
    }

    //tao role
}
