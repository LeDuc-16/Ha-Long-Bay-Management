package com.example.user_srv.repository;

import com.example.user_srv.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByIdAndIsActive(Long id, Boolean isActive);
    Optional<User> findByUsernameAndIsActive(String userName, Boolean isActive);
    Optional<User> findByUsernameAndIsActiveAndIsBlock(String username, Boolean isActive, Long isBlock);
    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
