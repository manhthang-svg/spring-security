package spring.security.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_001", "Người dùng không tồn tại trên hệ thống", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER_002", "Tài khoản hoặc Email đã tồn tại trên hệ thống", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND("USER_003", "Role không tồn tại trên hệ thống", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("AUTH_002", "Mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("AUTH_001", "Bạn chưa đăng nhập hoặc token không hợp lệ", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("AUTH_003", "Bạn không có quyền thực hiện hành động này", HttpStatus.FORBIDDEN),
    REFRESTOKEN_EXPIRED("AUTH_004","Token của bạn đã hết hạn",HttpStatus.UNAUTHORIZED),
    REFRESHTOKEN_NOT_FOUND("AUTH_005","Không tìm thấy refresh token",HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("AUTH_006","Refresh token không hợp lệ",HttpStatus.UNAUTHORIZED),
    UNCATEGORIZED_EXCEPTION("SYS_999", "Lỗi hệ thống nội bộ, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
