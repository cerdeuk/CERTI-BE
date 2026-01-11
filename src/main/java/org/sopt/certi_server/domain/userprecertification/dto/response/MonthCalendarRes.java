package org.sopt.certi_server.domain.userprecertification.dto.response;

import java.util.List;

public record MonthCalendarRes(
	int year,
	int month,
	List<DayDotRes> days
) {
}

