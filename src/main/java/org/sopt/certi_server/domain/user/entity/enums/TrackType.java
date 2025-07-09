package org.sopt.certi_server.domain.user.entity.enums;

import org.sopt.certi_server.domain.certification.entity.enums.TestType;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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


	private final String name;

	public static TrackType from(String name){
		if(name == null){
			throw new NotFoundException(ErrorCode.TRACK_NOT_FOUND);
		}
		return Arrays.stream(TrackType.values())
				.filter(t -> t.name.equalsIgnoreCase(name))
				.findFirst()
				.orElseThrow(() -> new NotFoundException(ErrorCode.TRACK_NOT_FOUND));
	}
}
