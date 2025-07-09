package org.sopt.certi_server.domain.user.dto.response;

import java.time.LocalDate;

import org.sopt.certi_server.domain.user.entity.Career;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record CareerDetailResponse(
	Long careerId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM", timezone = "Asia/Seoul")
	LocalDate startAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM", timezone = "Asia/Seoul")
	LocalDate endAt,
	String name,
	String description,
	String place
) {
	public static CareerDetailResponse from(Career career) {
		return CareerDetailResponse.builder()
			.careerId(career.getId())
			.startAt(career.getStartAt())
			.endAt(career.getEndAt())
			.name(career.getName())
			.description(career.getDescription())
			.place(career.getPlace())
			.build();
	}
}
