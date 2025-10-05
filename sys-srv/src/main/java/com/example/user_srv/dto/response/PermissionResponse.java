package com.example.user_srv.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    Long id;

    String name;

    String code;

    Boolean isActive;

    Long menuId;
    String menuCode;

    String description;

    String menuName;
    String appName;
}

