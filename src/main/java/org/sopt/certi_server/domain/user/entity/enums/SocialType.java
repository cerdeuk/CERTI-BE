package org.sopt.certi_server.domain.user.entity.enums;

import lombok.Getter;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.InvalidValueException;

@Getter
public enum SocialType {
    KAKAO, APPLE;

    public static SocialType from(String value){
        try{
            return SocialType.valueOf(value.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new InvalidValueException(ErrorCode.INVALID_FIELD_ERROR);
        }
    }
}
