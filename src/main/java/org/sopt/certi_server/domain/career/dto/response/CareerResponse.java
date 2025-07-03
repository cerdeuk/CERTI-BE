package org.sopt.certi_server.domain.career.dto.response;

import java.time.LocalDate;

import org.sopt.certi_server.domain.career.entity.Career;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record CareerResponse(
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM", timezone = "Asia/Seoul")
	LocalDate startAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM", timezone = "Asia/Seoul")
	LocalDate endAt,
	String name,
	String description,
	String place
) {
	public static CareerResponse of(Career career) {
		return CareerResponse.builder()
			.startAt(career.getStartAt())
			.endAt(career.getEndAt())
			.name(career.getName())
			.description(career.getDescription())
			.place(career.getPlace())
			.build();
	}
}
