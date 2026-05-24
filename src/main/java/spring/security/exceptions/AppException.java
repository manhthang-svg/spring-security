package spring.security.exceptions;

import lombok.Getter;
import spring.security.enums.ErrorCode;
@Getter
public class AppException extends RuntimeException{
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
