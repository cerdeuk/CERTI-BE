package org.sopt.certi_server.global.error.exception;

import org.sopt.certi_server.global.error.code.ErrorCode;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}

