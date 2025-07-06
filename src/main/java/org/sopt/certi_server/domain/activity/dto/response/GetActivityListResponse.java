package org.sopt.certi_server.domain.activity.dto.response;

import java.util.List;

public record GetActivityListResponse(
	List<ActivityDetailResponse> activityDetailResponses
) {
	public static GetActivityListResponse of(List<ActivityDetailResponse> activityDetailResponse) {
		return new GetActivityListResponse(activityDetailResponse);
	}
}
