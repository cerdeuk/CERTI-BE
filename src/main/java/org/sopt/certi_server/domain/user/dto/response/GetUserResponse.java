package org.sopt.certi_server.domain.user.dto.response;

import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.user.entity.User;

import lombok.Builder;

@Builder
public record GetUserResponse(
	String name,
	String university,
	String major,
	int percentage
) {
	public static GetUserResponse from(User user, MajorImpl majorImpl, int percentage) {
		return GetUserResponse.builder()
			.name(user.getNickname())
			.university(user.getUniversity().getName())
			.major(majorImpl.getName())
			.percentage(percentage)
			.build();
	}
}
