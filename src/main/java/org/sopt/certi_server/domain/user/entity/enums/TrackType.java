package org.sopt.certi_server.domain.user.entity.enums;

import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TrackType {
	HUMANITIES("인문계열"),
	SOCIAL_SCIENCES("사회계열"),
	EDUCATION("교육계열"),
	NATURAL_SCIENCES("자연계열"),
	ENGINEERING("공학계열"),
	MEDICINE_AND_PHARMACY("의약계열"),
	ARTS_AND_PHYSICAL_EDUCATION("예체능계열");


	private final String collegeName;

	public static TrackType from(String collegeName){
		try{
			return TrackType.valueOf(collegeName);
		}catch(IllegalArgumentException e){
			throw new NotFoundException(ErrorCode.DATA_NOT_FOUND);
		}
	}
}
