package org.sopt.certi_server.domain.admin.dto.response;

import java.util.List;

public record AdminCertificationDetailResponse(Long certificationId, List<CertificationMajorSimple> majorSimpleList,
                                               List<CertificationJobSimple> jobSimpleList) {

    public static AdminCertificationDetailResponse of(Long certificationId, List<CertificationMajorSimple> majorSimpleList, List<CertificationJobSimple> jobSimpleList) {
        return new AdminCertificationDetailResponse(certificationId, majorSimpleList, jobSimpleList);
    }
}
