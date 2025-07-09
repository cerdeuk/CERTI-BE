package org.sopt.certi_server.domain.admin.dto.response;

import org.sopt.certi_server.domain.certification.entity.CertificationJob;

public record CertificationJobSimple(Long certificationJobId, Long jobId, String jobName, float weight) {

    public static CertificationJobSimple from(CertificationJob cj){
        return new CertificationJobSimple(cj.getId(), cj.getJob().getId(), cj.getJob().getName(), cj.getWeight());
    }
}
