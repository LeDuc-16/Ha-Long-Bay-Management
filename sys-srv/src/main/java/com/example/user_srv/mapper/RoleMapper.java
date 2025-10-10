package com.example.user_srv.mapper;

import com.example.user_srv.dto.request.RoleRequest;
import com.example.user_srv.dto.response.RoleResponse;
import com.example.user_srv.dto.response.RoleResponseById;
import com.example.user_srv.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MenuMapper.class})
public interface RoleMapper {
    Role toRole (RoleRequest request);
    RoleResponseById toRoleResponseById(Role role);
    RoleResponse toRoleResponse(Role role);
}
