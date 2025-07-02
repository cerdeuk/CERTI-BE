package org.sopt.certi_server.domain.user.entity.enums;

import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CollegeType {
	ENGINEERING("공과대학"),
	BUSINESS("경영대학"),
	HUMANITIES("인문대학"),
	SOCIALSCIENCES("사회과학대학");

	private final String collegeName;

	public static CollegeType from(String collegeName){
		try{
			return CollegeType.valueOf(collegeName);
		}catch(IllegalArgumentException e){
			throw new NotFoundException(ErrorCode.DATA_NOT_FOUND);
		}
	}
}
