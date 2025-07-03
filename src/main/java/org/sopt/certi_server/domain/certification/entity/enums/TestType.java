package org.sopt.certi_server.domain.certification.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.InvalidValueException;
import org.sopt.certi_server.global.error.exception.NotFoundException;

@Getter
@RequiredArgsConstructor
public enum TestType {
    WRITTEN("필기형"),
    PRACTICAL("실기형"),
    COMBINED("종합형");

    private final String type;

    public static TestType from(String typeName){
        try{
            return TestType.valueOf(typeName);
        }catch (IllegalArgumentException e){
            throw new NotFoundException(ErrorCode.DATA_NOT_FOUND);
        }
    }
}
