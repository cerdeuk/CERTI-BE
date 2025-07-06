package org.sopt.certi_server.domain.activity.dto.response;

import java.time.LocalDate;

import org.sopt.certi_server.domain.user.entity.Activity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record ActivityDetailResponse(
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM", timezone = "Asia/Seoul")
	LocalDate startAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM", timezone = "Asia/Seoul")
	LocalDate endAt,
	String name,
	String description,
	String place
) {
	public static ActivityDetailResponse from(Activity activity) {
		return ActivityDetailResponse.builder()
			.startAt(activity.getStartAt())
			.endAt(activity.getEndAt())
			.name(activity.getName())
			.description(activity.getDescription())
			.place(activity.getPlace())
			.build();
	}
}
