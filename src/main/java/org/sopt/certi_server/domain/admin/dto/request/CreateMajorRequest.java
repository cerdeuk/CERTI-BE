package org.sopt.certi_server.domain.admin.dto.request;

public record CreateMajorRequest(
	String majorName,
	float weight
) {
}
