package org.sopt.certi_server.domain.admin.dto.request;

public record CertificationMajorCreateRequest(String certificationName, String majorName, float weight) {
}
