package org.sopt.certi_server.domain.user.dto.request;

import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateJobRequest(
        @Size(min = 1, max = 3, message = "희망 직무는 1~3개까지 선택가능합니다")
        List<String> jobNameList
) {

}
