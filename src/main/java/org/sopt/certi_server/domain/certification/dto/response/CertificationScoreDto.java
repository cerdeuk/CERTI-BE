package org.sopt.certi_server.domain.certification.dto.response;

import org.sopt.certi_server.domain.certification.entity.Certification;

import java.util.List;

public record CertificationScoreDto(
        Long certificationId,
        String certificationName,
        String certificationType,
        String testType,
        List<String> tags,
        int recommendationScore,
        boolean isFavorite,
        String description) {
    public static CertificationScoreDto from(
            Certification certification,
            int recommendationScore,
            boolean isFavorite
    ) {
        return new CertificationScoreDto(
                certification.getId(),
                certification.getName(),
                certification.getCertificationType().getKoreanName(),
                certification.getTestType().getType(),
                certification.getTags().stream().toList(),
                recommendationScore,
                isFavorite,
                certification.getDescription()
        );
    }

    @Override
    public String toString() {
        return "CertificationScoreDto{" +
                "certificationId=" + certificationId +
                ", certificationName='" + certificationName + '\'' +
                ", certificationType='" + certificationType + '\'' +
                ", testType='" + testType + '\'' +
                ", tags=" + tags +
                ", recommendationScore=" + recommendationScore +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
