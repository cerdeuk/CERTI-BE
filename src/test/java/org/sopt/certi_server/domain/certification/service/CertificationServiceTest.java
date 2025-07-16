package org.sopt.certi_server.domain.certification.service;

import org.junit.jupiter.api.Test;
import org.sopt.certi_server.domain.certification.dto.response.CertificationRecommendationListResponse;
import org.sopt.certi_server.domain.certification.dto.response.CertificationScoreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CertificationServiceTest {

    @Autowired
    CertificationService certificationService;

    @Test
    void certification_recommandation_test() {
        CertificationRecommendationListResponse certificationRecommendationListResponse = certificationService.recommendCertifications(1L);

        System.out.println("certificationRecommendationListResponse.recommendationList().size() = " + certificationRecommendationListResponse.recommendationList().size());

        for (CertificationScoreDto certificationScoreDto : certificationRecommendationListResponse.recommendationList()) {
            System.out.println("certificationScoreDto = " + certificationScoreDto);
        }
    }


    @Test
    @Transactional(readOnly = true)
    void certification_recommandation_testV2() {
        CertificationRecommendationListResponse certificationRecommendationListResponse = certificationService.recommendCertifications(1L);

        System.out.println("certificationRecommendationListResponse.recommendationList().size() = " + certificationRecommendationListResponse.recommendationList().size());

        for (CertificationScoreDto certificationScoreDto : certificationRecommendationListResponse.recommendationList()) {
            System.out.println("certificationScoreDto = " + certificationScoreDto);
        }
    }
}
