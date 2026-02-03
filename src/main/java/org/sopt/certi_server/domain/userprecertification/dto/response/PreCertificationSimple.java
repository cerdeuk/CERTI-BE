package org.sopt.certi_server.domain.userprecertification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PreCertificationSimple(
        Long certificationId,
        Long preCertificationId,
        String certificationName,
        String certificationType,
        String description,
        String averagePeriod,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul") LocalDate nearestTestDate,
        String agencyName,
        int iconIndex,
        String city,
        String state,
        LocalDateTime testDate
) {

    public static PreCertificationSimple from(UserPreCertification upc) {
        return new PreCertificationSimple(
                upc.getCertification().getId(),
                upc.getId(),
                upc.getCertification().getName(),
                upc.getCertification().getCertificationType().getKoreanName(),
                upc.getCertification().getDescription(),
                upc.getCertification().getAveragePeriod(),
                upc.getCertification().getNearestTestDate(),
                upc.getCertification().getAgency().getName(),
                upc.getIconType().getIndex(),
                upc.getLocation().getCity(),
                upc.getLocation().getState(),
                upc.getTestDate()
        );
    }
}
