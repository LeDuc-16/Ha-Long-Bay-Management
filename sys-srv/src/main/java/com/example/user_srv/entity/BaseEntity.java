package com.example.user_srv.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity {

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "update_by")
    String updatedBy;

    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = true;

    @PrePersist
    private void prePersist() {
        setCreatedAt(LocalDateTime.now());
        setIsActive(true);
    }

    @PreUpdate
    private void preUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }
}
