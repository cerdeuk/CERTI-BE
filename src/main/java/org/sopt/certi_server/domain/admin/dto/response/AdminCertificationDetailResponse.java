package org.sopt.certi_server.domain.admin.dto.response;

import org.sopt.certi_server.domain.certification.dto.response.CertificationDetailResponse;

import java.util.List;

public record AdminCertificationDetailResponse(CertificationDetailResponse certificationDetail, List<CertificationMajorSimple> majorSimpleList,
                                               List<CertificationJobSimple> jobSimpleList) {

    public static AdminCertificationDetailResponse of(CertificationDetailResponse certificationDetail, List<CertificationMajorSimple> majorSimpleList, List<CertificationJobSimple> jobSimpleList) {
        return new AdminCertificationDetailResponse(certificationDetail, majorSimpleList, jobSimpleList);
    }
}
