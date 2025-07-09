package org.sopt.certi_server.domain.admin.dto.request;

public record CreateJobRequest(
	String jobName,
	float weight
) {
}
