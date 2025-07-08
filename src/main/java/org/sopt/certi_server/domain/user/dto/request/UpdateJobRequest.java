package org.sopt.certi_server.domain.user.dto.request;

import java.util.List;

import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.BadRequestException;

public record UpdateJobRequest(
	List<String> jobNameList
) {
	public UpdateJobRequest {
		if(jobNameList.isEmpty() || jobNameList.size() > 3){
			throw new BadRequestException(ErrorCode.JOB_SIZE_ERROR);
		}
	}
}
