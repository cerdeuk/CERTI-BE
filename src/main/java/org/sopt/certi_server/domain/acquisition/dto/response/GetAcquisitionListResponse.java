package org.sopt.certi_server.domain.acquisition.dto.response;

import java.util.List;

public record GetAcquisitionListResponse(
	List<GetAcquisitionResponse> getPriorCertificaitonResponses
) {
	public static GetAcquisitionListResponse of(List<GetAcquisitionResponse> priorCertificaitonResponses) {
		return new GetAcquisitionListResponse(priorCertificaitonResponses);
	}
}
