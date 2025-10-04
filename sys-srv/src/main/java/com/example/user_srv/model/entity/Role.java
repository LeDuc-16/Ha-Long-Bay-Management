package com.example.user_srv.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
    @SequenceGenerator(name = "role_generator", sequenceName = "ROLE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "ROLE_NAME")
    String name;

    @Column(name = "ROLE_CODE")
    String code;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "role_menu",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    Set<Menu> menus;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    Set<Permission> permissions;

    @Column(name = "CREATE_DATE")
    LocalDateTime createdAt;

    @Column(name = "CREATOR_ID")
    Long creatorBy;

    @Column(name = "CREATOR_NAME")
    String creatorName;

    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = true;

    @Column(name = "TYPE")
    Long type;

}
