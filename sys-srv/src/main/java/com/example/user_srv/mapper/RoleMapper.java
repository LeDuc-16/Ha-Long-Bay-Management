package com.example.user_srv.mapper;

import com.example.user_srv.dto.request.RoleRequest;
import com.example.user_srv.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole (RoleRequest request);
}
