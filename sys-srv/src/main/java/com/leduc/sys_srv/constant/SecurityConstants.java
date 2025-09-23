package com.leduc.sys_srv.constant;

public class SecurityConstants {
    public static final String SECRET_KEY = "your-secret-key-very-long-and-secure"; // Thay bằng key mạnh
    public static final long EXPIRATION_TIME = 864_000_000; // 10 ngày (ms)
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}