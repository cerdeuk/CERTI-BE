package org.sopt.certi_server.domain.user.dto.request;

import java.util.List;

import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.BadRequestException;

import jakarta.validation.constraints.Size;

public record UpdateJobRequest(
	@Size(min = 1, max = 3, message = "희망 직무는 1~3개까지 선택가능합니다")
	List<String> jobNameList
){

}
