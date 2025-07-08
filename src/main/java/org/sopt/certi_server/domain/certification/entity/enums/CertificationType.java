package org.sopt.certi_server.domain.certification.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum CertificationType {

    NATIONAL_TECHNICAL("국가기술자격"),
    NATIONAL_PROFESSIONAL("국가전문자격"),
    PRIVATE("민간자격"),
    LANGUAGE("어학자격");

    private final String koreanName;

    public static CertificationType from(String koreanName) {
        return Arrays.stream(CertificationType.values())
                .filter(ct -> ct.koreanName.equalsIgnoreCase(koreanName))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.CERTIFICATION_TYPE_NOT_FOUND));
    }
}
