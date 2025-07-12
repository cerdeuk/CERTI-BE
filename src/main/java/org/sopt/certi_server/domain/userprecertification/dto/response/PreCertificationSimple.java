package org.sopt.certi_server.domain.userprecertification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;

import java.time.LocalDate;

public record PreCertificationSimple(
        Long certificationId,
        String certificationName,
        String averagePeriod,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul") LocalDate nearestTestDate,
        String agencyName,
        int iconIndex
) {

    public static PreCertificationSimple from(UserPreCertification upc) {
        return new PreCertificationSimple(upc.getCertification().getId(), upc.getCertification().getName(), upc.getCertification().getAveragePeriod(), upc.getCertification().getNearestTestDate(), upc.getCertification().getAgency().getName(), upc.getIconType().getIndex());
    }
}
