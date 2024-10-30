package hana.simple.boardservice.global.exception;

import hana.simple.boardservice.global.exception.constant.ErrorCode;
import lombok.Getter;

public class ApplicationException extends RuntimeException{
    @Getter
    private final ErrorCode errorCode;
    private final String message;

    public ApplicationException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message == null? errorCode.getMessage() : message;
    }
}
