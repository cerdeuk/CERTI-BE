package org.sopt.certi_server.domain.user.entity.enums;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Grade {

    FRESHMAN("1학년"),
    SOPHOMORE("2학년"),
    JUNIOR("3학년"),
    SENIOR("4학년 이상"),
    GRADUATED("졸업/졸업유예");


    private final String grade;

    public static Grade from(String grade){
        return Arrays.stream(Grade.values())
                .filter(g -> g.grade.equalsIgnoreCase(grade))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.GRADE_NOT_FOUND));
    }
}
