package com.example.user_srv.dto.response;

import java.util.List;

public class MenuResponse {
    Long id;
    String name;
    String code;
    Long parentId;
    Integer orderNo;
    List<MenuResponse> child;
    Boolean isActive;
    String url;
    String icon;
    List<PermissionResponse> permissions;
}
