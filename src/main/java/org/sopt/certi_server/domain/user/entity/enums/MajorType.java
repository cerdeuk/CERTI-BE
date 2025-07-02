package org.sopt.certi_server.domain.user.entity.enums;

import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MajorType {
	MECHANICAL(CollegeType.ENGINEERING, "기계공학과"),
	ELECTRICAL(CollegeType.ENGINEERING, "전자공학과"),
	CIVIL(CollegeType.ENGINEERING, "토목공학과"),
	BUSINESS_ADMIN(CollegeType.BUSINESS, "경영학과"),
	ACCOUNTING(CollegeType.BUSINESS, "회계학과"),
	LITERATURE(CollegeType.HUMANITIES, "국문학과");

	private final CollegeType collegeType;
	private final String majorTypeName;

	public static MajorType fromCollegeType(String majorTypeName) {
		for (MajorType majorType : MajorType.values()) {
			if (majorType.majorTypeName.equals(majorTypeName)) {
				return majorType;
			}
		}
		throw new NotFoundException(ErrorCode.DATA_NOT_FOUND);
	}
}
