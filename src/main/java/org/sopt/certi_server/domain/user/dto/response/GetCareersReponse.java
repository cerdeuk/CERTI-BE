package org.sopt.certi_server.domain.user.dto.response;

import java.util.List;

public record GetCareersReponse(
	List<CareerDetailResponse> careerDetailResponseList
) {
	public static GetCareersReponse of(List<CareerDetailResponse> careerDetailResponseList) {
		return new GetCareersReponse(careerDetailResponseList);
	}
}
