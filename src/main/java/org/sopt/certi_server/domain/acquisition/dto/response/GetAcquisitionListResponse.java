package org.sopt.certi_server.domain.acquisition.dto.response;

import java.util.List;

public record GetAcquisitionListResponse(
        List<GetAcquisitionListDetailResponse> acquisitionListDetailResponses
) {
    public static GetAcquisitionListResponse of(List<GetAcquisitionListDetailResponse> priorCertificaitonResponseList) {
        return new GetAcquisitionListResponse(priorCertificaitonResponseList);
    }
}
