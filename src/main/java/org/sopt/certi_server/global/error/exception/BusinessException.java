package org.sopt.certi_server.global.error.exception;


import lombok.Getter;
import org.sopt.certi_server.global.error.code.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
