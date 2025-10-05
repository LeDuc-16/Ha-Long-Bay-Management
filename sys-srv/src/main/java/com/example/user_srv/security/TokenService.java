package com.example.user_srv.security;

import com.example.user_srv.constant_.Constants;
import com.example.user_srv.constant_.ROLE;
import com.example.user_srv.exception.AppException;
import com.example.user_srv.exception.ERROR_CODE;
import com.example.user_srv.entity.Role;
import com.example.user_srv.entity.Token;
import com.example.user_srv.entity.User;
import com.example.user_srv.entity.enums.TokenCategory;
import com.example.user_srv.repository.PermissionRepository;
import com.example.user_srv.repository.RoleRepository;
import com.example.user_srv.repository.TokenRepository;
import com.example.user_srv.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.user_srv.constant_.SECURITY.AUTHOR_HEADER_KEY;
import static com.example.user_srv.constant_.SECURITY.TOKEN_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenService {

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private TokenRepository TokenRepository;

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    public TokenInfo generateToken(User user, boolean isAccessToken, Optional<Long> roleId) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        String jti = UUID.randomUUID().toString();

        long expiryTime = isAccessToken ? VALID_DURATION : REFRESHABLE_DURATION;
        Date issueTime = new Date();
        Date expiryDate = new Date(Instant.ofEpochMilli(issueTime.getTime())
                .plus(expiryTime, ChronoUnit.SECONDS)
                .toEpochMilli());

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .issueTime(issueTime)
                .expirationTime(expiryDate)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", new TokenClaims(
                        user.getId(),
                        this.buildScope(user, roleId)))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            TokenCategory category = isAccessToken
                    ? TokenCategory.ACCESS_TOKEN
                    : TokenCategory.REFRESH_TOKEN;

            Token tokenEntity = Token.builder()
                    .id(jti) // RẤT QUAN TRỌNG: Dùng JWT ID (JTI) làm ID chính của Token Entity
                    .user(user) // Gán Entity User cho Token
                    // Khởi tạo trạng thái mặc định: chưa hết hạn và chưa bị thu hồi
                    .revoked(false)
                    .expired(false)
                    .tokenCategory(category)
                    .build();

            TokenRepository.save(tokenEntity);
            return new TokenInfo(jwsObject.serialize(), expiryDate);
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(ERROR_CODE.UNAUTHENTICATED);
        }
    }

    public Boolean isAdmin() {
        Long userId = getUserIdFromContext();

        User user = userRepository.findByIdAndIsActive(userId, Constants.DELETE.NORMAL)
                .orElseThrow(() -> new AppException(ERROR_CODE.USER_NOT_EXISTED));

        Set<Role> roles = user.getRoles();

        return roles.stream()
                .anyMatch(role -> ROLE.ADMIN.val.equalsIgnoreCase(role.getCode()));
    }

    public SignedJWT verifyToken(String token, boolean isRefreshToken) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verified = signedJWT.verify(verifier);
        long expiryTime = isRefreshToken ? REFRESHABLE_DURATION : VALID_DURATION;
        Date expiryDate = isRefreshToken
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(expiryTime, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!verified) {
            throw new AppException(ERROR_CODE.INVALID_TOKEN);
        }

        boolean isInvalidated = TokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());
        if (expiryDate.before(new Date()) || isInvalidated) {
            throw new AppException(ERROR_CODE.EXPIRED_TOKEN);
        }

        return signedJWT;
    }

    private Set<Long> buildScope(User user, Optional<Long> roleId) {
        Set<Long> roleIds;
        if (roleId.isPresent()) {
            roleIds = new HashSet<>();
            roleIds.add(roleId.get());
        } else {
            roleIds = user.getRoles().stream()
                    .map(Role::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }

        return roleIds;
    }

    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHOR_HEADER_KEY.val);
        if (token != null && token.startsWith(TOKEN_PREFIX.val)) {
            return token.substring(TOKEN_PREFIX.val.length());
        }
        throw new AppException(ERROR_CODE.INVALID_TOKEN);
    }

    public Long getUserIdFromContext() {
        var context = SecurityContextHolder.getContext();
        return Long.parseLong(context.getAuthentication().getName());
    }

    public TokenClaims getTokenClaims() {
        var context = SecurityContextHolder.getContext();
        JwtAuthenticationToken auth = (JwtAuthenticationToken) context.getAuthentication();
        if (auth != null) {
            Jwt jwt = (Jwt) auth.getCredentials();
            Map<String, Object> claims = jwt.getClaims();
            Gson gson = new Gson();
            return gson.fromJson(gson.toJson(claims.get("scope")), TokenClaims.class);
        }

        return new TokenClaims(null, null);
    }

    public record TokenInfo(String token, Date expiryDate) {
    }

    public record TokenClaims(Long userId, Set<Long> roleIds) {
    }
}
