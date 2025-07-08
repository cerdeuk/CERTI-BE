package org.sopt.certi_server.domain.user.entity.enums;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

@RequiredArgsConstructor
public enum Grade {

    FRESHMAN("1학년"),
    SOPHOMORE("2학년"),
    JUNIOR("3학년"),
    SENIOR("4학년 이상"),
    GRADUATED("졸업/졸업유예");


    private final String grade;

    public static TrackType from(String grade){
        try{
            return TrackType.valueOf(grade);
        }catch(IllegalArgumentException e){
            throw new NotFoundException(ErrorCode.DATA_NOT_FOUND);
        }
    }
}
