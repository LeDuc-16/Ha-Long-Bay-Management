package com.example.user_srv.entity;

import com.example.user_srv.entity.enums.TokenCategory;
import com.example.user_srv.entity.enums.TokenType; // Import Token Type
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
    @Id
    private String id; // JWT ID (UUID string)

    // TOKEN TYPE: Loại Token (ví dụ: BEARER).
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TokenType tokenType = TokenType.BEARER;

    // TOKEN CATEGORY: Mục đích sử dụng (Access hay Refresh).
    @Enumerated(EnumType.STRING)
    @Column(name = "token_category")
    private TokenCategory tokenCategory;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}