package org.sopt.certi_server.domain.acquisition.dto.response;

import java.util.List;

public record GetAcquisitionListResponse(
        List<GetAcquisitionResponse> acquisitionListDetailResponses
) {
    public static GetAcquisitionListResponse of(List<GetAcquisitionResponse> priorCertificaitonResponseList) {
        return new GetAcquisitionListResponse(priorCertificaitonResponseList);
    }
}
