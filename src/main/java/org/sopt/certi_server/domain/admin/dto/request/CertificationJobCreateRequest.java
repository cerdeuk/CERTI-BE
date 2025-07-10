package org.sopt.certi_server.domain.admin.dto.request;

public record CertificationJobCreateRequest(String certificationName, String jobName, float weight) {
}
