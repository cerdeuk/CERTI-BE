package org.sopt.certi_server.domain.career.dto.response;

import java.time.LocalDate;

import org.sopt.certi_server.domain.career.entity.Career;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record CareerDetailResponse(
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM", timezone = "Asia/Seoul")
	LocalDate startAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM", timezone = "Asia/Seoul")
	LocalDate endAt,
	String name,
	String description,
	String place
) {
	public static CareerDetailResponse of(Career career) {
		return CareerDetailResponse.builder()
			.startAt(career.getStartAt())
			.endAt(career.getEndAt())
			.name(career.getName())
			.description(career.getDescription())
			.place(career.getPlace())
			.build();
	}
}
