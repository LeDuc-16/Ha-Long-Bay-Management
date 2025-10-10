package com.example.user_srv.repository;

import com.example.user_srv.entity.Role;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    @Query("select r from Role r where r.name = ?1")
    Optional<Role> findByName(@Param("name") String name);

    @Query("SELECT r FROM Role r WHERE r.id IN :ids")
    Set<Role> findAllByIds(@Param("ids") Set<Long> ids);

}
