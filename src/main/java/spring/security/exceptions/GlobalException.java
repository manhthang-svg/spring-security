package spring.security.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.security.dto.response.ErrorResponse;
import spring.security.enums.ErrorCode;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    // 1. Bắt chính xác quả bom Custom 'AppException' do chúng ta tự ném ra
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        // Log ở mức WARN vì đây là lỗi business có thể dự đoán, không phải crash hệ thống
        log.warn("[APP ERROR] code={}, message={}", errorCode.getCode(), errorCode.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    // 2. Bắt tất cả các lỗi không xác định khác (Lỗi runtime tụt xích, null pointer...)
    // để tránh lộ log hệ thống ra ngoài
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUncategorizedException(Exception exception) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        log.error("[SYSTEM ERROR] Uncategorized exception caught: {}", exception.getMessage(), exception);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        // Lấy ra câu thông báo lỗi đầu tiên mà bạn cấu hình ở thuộc tính (ví dụ: "Username không được để trống")
        String errorMessage = exception.getBindingResult().getFieldError().getDefaultMessage();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("AUTH_002")
                .message("Tài khoản hoặc mật khẩu không chính xác")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("AUTH_003")
                .message("Bạn không có quyền thực hiện hành động này")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}