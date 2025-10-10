package com.example.file_srv.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ERROR_CODE {
    UNKNOWN_EXCEPTION(9999, "Lỗi chưa xác định",HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(9000, "Sai enum key", HttpStatus.BAD_REQUEST),

    // --- Lỗi Authentication ---
    INVALID_TOKEN(1001, "Token không hợp lệ", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(1002, "Token đã hết hạn", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(2001, "Người dùng không tồn tại", HttpStatus.NOT_FOUND), // Ví dụ thêm

    // --- Lỗi liên quan đến File (MỚI THÊM) ---
    FILE_CANNOT_BE_EMPTY(3001, "File không được để trống", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(3002, "Upload file thất bại do lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    // MÃ LỖI MỚI
    INVALID_FILE_TYPE(3003, "Loại file không được hỗ trợ", HttpStatus.BAD_REQUEST);


    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ERROR_CODE(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}