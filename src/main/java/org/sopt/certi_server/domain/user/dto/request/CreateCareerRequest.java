package org.sopt.certi_server.domain.user.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCareerRequest(
	@NotNull(message = "근무 시작일은 필수 입력값입니다.")
	LocalDate startAt,
	@NotNull(message = "근무 종료일은 필수 입력값입니다")
	LocalDate endAt,
	@NotNull(message = "근무 회사는 필수 입력값입니다")
	String place,
	@NotNull(message = "직무는 필수 입력값입니다")
	String name,
	@Size(max = 16, message = "최대 16자까지 입력가능합니다")
	@NotNull(message = "직무 관련 내용은 필수 입력값입니다")
	String description
) {
}
