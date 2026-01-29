package org.sopt.certi_server.domain.admin.dto.request;

public record CertificationJobCreateRequest(Long certificationId, Long jobId, double weight) {
}
