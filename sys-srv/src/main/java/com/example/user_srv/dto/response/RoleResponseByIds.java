package com.example.user_srv.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponseByIds {
    Long id;
    String name;
    String code;
    Set<MenuResponse> menus;
    List<PermissionResponse> permissions;
    Boolean isActive;
}
