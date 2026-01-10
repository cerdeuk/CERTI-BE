package org.sopt.certi_server.domain.userprecertification.dto.response;

public record ScheduleICertificationRes(
	Long userPreCertificationId,
	String certificationName,
	String certificationType,
	String description,
	String location,
	String time
) {
}
