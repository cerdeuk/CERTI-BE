package org.sopt.certi_server.domain.userpriorcertification.dto.response;

import java.util.List;

public record GetPriorCertificationListResponse(
	List<GetPriorCertificaitonResponse> getPriorCertificaitonResponses
) {
	public static GetPriorCertificationListResponse of(List<GetPriorCertificaitonResponse> priorCertificaitonResponses) {
		return new GetPriorCertificationListResponse(priorCertificaitonResponses);
	}
}
