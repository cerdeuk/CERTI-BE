package org.sopt.certi_server.domain.certification.entity.enums;

import java.util.Arrays;

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
    COMBINED("복합형");

    private final String type;

    public static TestType from(String typeName) {
        return Arrays.stream(TestType.values())
            .filter(t -> t.type.equalsIgnoreCase(typeName))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(ErrorCode.TEST_TYPE_MISMATCH));
    }
}
