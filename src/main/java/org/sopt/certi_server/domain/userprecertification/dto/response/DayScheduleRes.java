package org.sopt.certi_server.domain.userprecertification.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DayScheduleRes(
	LocalDate date,
	List<ScheduleICertificationRes> certifications // 없으면 null
) {}
