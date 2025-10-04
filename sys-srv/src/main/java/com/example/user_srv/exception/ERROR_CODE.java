package com.example.user_srv.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ERROR_CODE {

    UNKNOWN_EXCEPTION(9999, "Lỗi chưa xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(9000, "Sai enum key", HttpStatus.BAD_REQUEST),

    /**
     * Authentication error
     */
    INVALID_TOKEN(1001, "Token không hợp lệ", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(1002, "Token đã hết hạn", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1003, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "Người dùng không tồn tại", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005, "Không được xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "Bạn không có quyền", HttpStatus.FORBIDDEN),
    INVALID_AUTH(1007, "Tên người dùng hoặc mật khẩu không đúng", HttpStatus.BAD_REQUEST),
    INVALID_AGENCY(1008, "Không có quyền kết nối đến hệ thống", HttpStatus.BAD_REQUEST),
    PASSWORD_IS_SEQUENTIAL(4042, "Vui lòng không đặt mật khẩu tuần tự", HttpStatus.BAD_REQUEST),
    PASSWORD_IS_COMMON(4013, "Mật khẩu không an toàn, vui lòng đặt lại mật khẩu mới", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(20011, "Sai mật khẩu", HttpStatus.BAD_REQUEST),
    USERNAME_OR_EMAIL_ARE_INCORRECT(2007, "Tên người dùng hoặc email không đúng", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(4002, "Vai trò không tồn tại", HttpStatus.NOT_FOUND),
    MENU_NOT_EXISTED(4005, "Menu không tồn tại", HttpStatus.NOT_FOUND),
    MENU_EXISTED(4006, "Menu đã tồn tại", HttpStatus.NOT_FOUND),
    MENU_NEED_REMOVE_FROM_ROLE(4017, "Danh sách vai trò cần xóa menu: ", HttpStatus.BAD_REQUEST),

    /**
     * Business Error
     **/
    EMPTY_FILE(4001, "Tập tin trống", HttpStatus.BAD_REQUEST),
    ERROR_WHILE_ENCRYPTING_FILE(4002, "Lỗi khi mã hóa tập tin", HttpStatus.BAD_REQUEST),
    ERROR_WHILE_DECRYPTING_FILE(4003, "Lỗi khi giải mã tập tin", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(4004, "Không tìm thấy tệp", HttpStatus.BAD_REQUEST),
    ERROR_READING_FILE(4005, "Không tìm thấy tệp", HttpStatus.BAD_REQUEST),
    ERROR_UPLOAD_FILE(4006, "Lỗi khi tải lên tệp", HttpStatus.BAD_REQUEST),
    DIRECTORY_CREATION_FAILED(4007, "Lỗi khi tạo thư mục lưu trữ", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(4008, "Không tìm thấy danh mục", HttpStatus.BAD_REQUEST),

    ERROR_REDIS(4009, "Err redis", HttpStatus.BAD_REQUEST),

    DEPARTMENT_NOT_EXISTED(4010, "Đơn vị không tồn tại", HttpStatus.NOT_FOUND),
    DEPARTMENT_EXISTED(4011, "Đơn vị đã tồn tại", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND(4012, "Không tìm thấy dữ liệu", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT(2008, "Định dạng ngày tháng sai - ", HttpStatus.BAD_REQUEST),
    OUT_OF_RANGE_DATE(2009, "Ngày vượt quá phạm vi cho phép", HttpStatus.BAD_REQUEST),
    EXCEEDS_THE_CURRENT_DATE(2010, "Ngày vượt quá ngày hiện tại", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(4013, "Quyền không tồn tại", HttpStatus.NOT_FOUND),

    DEPARTMENT_CHILD_EXISTED(4010, "Không thể xóa khi có đơn vị con", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ERROR_CODE(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}

