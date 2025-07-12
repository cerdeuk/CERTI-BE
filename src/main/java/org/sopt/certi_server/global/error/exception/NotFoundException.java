package org.sopt.certi_server.global.error.exception;

import org.sopt.certi_server.global.error.code.ErrorCode;

public class NotFoundException extends BusinessException {
    public NotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
