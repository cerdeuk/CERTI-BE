package org.sopt.certi_server.domain.userprecertification.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ScheduleICertificationRes(
	Long certificationId,
	String certificationName,
	List<String> tags,
	String averagePeriod,
	String charge,
	String agencyName,
	String testType,
	String description,
	String applicationMethod,
	String applicationUrl,
	String expirationPeriod,
	String city,
	String state,
	@JsonFormat()
	LocalDateTime testDate,
	boolean isAcquired
) {
}
