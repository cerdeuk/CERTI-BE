package org.sopt.certi_server.domain.career.dto.response;

import java.util.List;

public record GetCareersReponse(
	List<CareerResponse> careerResponseList
) {
	public static GetCareersReponse of(List<CareerResponse> careerResponseList) {
		return new GetCareersReponse(careerResponseList);
	}
}
