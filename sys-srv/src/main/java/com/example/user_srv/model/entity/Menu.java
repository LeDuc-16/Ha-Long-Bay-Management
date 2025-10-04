package com.example.user_srv.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "menus")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_generator")
    @SequenceGenerator(name = "menu_generator", sequenceName = "MENU_SEQ", allocationSize = 1)
    @Basic(optional = false)
    Long id;

    String name;

    String code;

    @Column(name = "parent_id")
    Long parentId;

    @Column(name = "order_no")
    Integer orderNo;

    @Transient
    List<Menu> menu;

    String url;

    String icon;

}
