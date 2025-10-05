package com.example.user_srv.dto.request; // Thường đặt trong package DTO/Request

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data // Bao gồm @Getter, @Setter, @ToString, @EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

    private Long id;

    @NotBlank(message = "Tên vai trò không được để trống")
    @Size(max = 255, message = "Tên vai trò không được vượt quá 255 ký tự")
    private String name;

    @NotBlank(message = "Mã vai trò không được để trống")
    @Size(max = 50, message = "Mã vai trò không được vượt quá 50 ký tự")
    private String code;

    private Set<Long> menuIds;

    private Set<Long> permissionIds;

    private Boolean isActive;
}