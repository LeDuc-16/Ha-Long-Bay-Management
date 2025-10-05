package com.example.user_srv.constant_;

public enum SECURITY {

    TOKEN_PREFIX("Bear "),
    PAYLOAD_USER_ID("userId"),
    AUTHOR_HEADER_KEY("Authorization"),
    AUTH_SCOPE("scope"),
    PERMISSION_CLAIM("PERMISSION_CLAIM"),
    PERMISSION_PREFIX("PERMISSION_");
    public final String val;

    SECURITY(String val) {
        this.val = val;
    }
}
