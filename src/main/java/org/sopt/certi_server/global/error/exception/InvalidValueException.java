package org.sopt.certi_server.global.error.exception;

import org.sopt.certi_server.global.error.code.ErrorCode;

public class InvalidValueException extends BusinessException {
    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
