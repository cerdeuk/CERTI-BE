package org.sopt.certi_server.global.error.exception;

import org.sopt.certi_server.global.error.code.ErrorCode;

public class InvalidNicknameException extends BusinessException{
    public InvalidNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
