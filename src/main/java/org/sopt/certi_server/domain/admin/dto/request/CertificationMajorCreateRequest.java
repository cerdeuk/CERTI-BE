package org.sopt.certi_server.domain.admin.dto.request;

public record CertificationMajorCreateRequest(Long certificationId, Long majorId, double weight) {
}
