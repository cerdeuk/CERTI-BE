package org.sopt.certi_server.domain.certification.dto.response;

import java.util.List;

public record CertificationRecommendationListResponse(List<CertificationScoreDto> recommendationList) {

    public static CertificationRecommendationListResponse of(List<CertificationScoreDto> recommendationList){
        return new CertificationRecommendationListResponse(recommendationList);
    }
}
