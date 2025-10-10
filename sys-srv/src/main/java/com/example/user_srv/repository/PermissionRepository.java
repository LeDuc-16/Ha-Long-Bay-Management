package com.example.user_srv.repository;

import com.example.user_srv.dto.request.PermissionFilter;
import com.example.user_srv.dto.response.PermissionResponse;
import com.example.user_srv.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Set<Permission> findAllByIdInAndIsActive(Set<Long> ids, Boolean isActive);

    Set<Permission> findAllByMenuCodeAndIsActive(String menuCode, Boolean isActive);

    @Query(" SELECT r.permissions from Role r " +
            "JOIN r.permissions p " +
            "JOIN r.menus m " +
            "WHERE r.id = :roleId and m.id = :menuId " +
            "AND p.menuId = :menuId " +
            "AND r.isActive = true")
    Set<Permission> findPermissionActive(Long roleId, Long menuId);

    //tim tat cac cac quyen
    @Query("SELECT new com.example.user_srv.dto.response.PermissionResponse(p.id, p.name, p.code, p.isActive, p.menuId, p.menuCode, p.description, m.name, p.appName) " +
            "FROM Permission p " +
            "LEFT JOIN Menu m on m.code = p.menuCode " +
            "WHERE 1=1 " +
            "AND (:#{#filter.name} IS NULL OR lower(p.name) like CONCAT('%', LOWER(:#{#filter.name}), '%')) " +
            "AND (:#{#filter.code} IS NULL OR lower(p.code) like CONCAT('%', LOWER(:#{#filter.code}), '%')) " +
            "AND (:#{#filter.appName} IS NULL OR lower(p.appName) like CONCAT('%', LOWER(:#{#filter.appName}), '%')) " +
            "AND (:#{#filter.menuCode} IS NULL OR p.menuCode = :#{#filter.menuCode}) " +
            "AND (p.isActive = true) ")
    Page<PermissionResponse> findAllPermission(@Param("filter") PermissionFilter filter,
                                               Pageable pageable);

    @Query("SELECT new com.example.user_srv.dto.response.PermissionResponse(p.id, p.name, p.code, p.isActive, p.menuId, p.menuCode, p.description, m.name, p.appName) " +
            "FROM Permission p " +
            "LEFT JOIN Menu m on m.code = p.menuCode " +
            "WHERE 1=1 " +
            "AND (p.id = :id) " +
            "AND (p.isActive = true) ")
    Optional<PermissionResponse> findByIdAndIsActive(@Param("id") Long id);

    List<Permission> findAllByIsActiveAndAppName(Boolean isActive, String appName);

}
