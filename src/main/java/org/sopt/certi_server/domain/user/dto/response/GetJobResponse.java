package org.sopt.certi_server.domain.user.dto.response;

import java.util.List;

public record GetJobResponse(
        List<String> jobList
) {
    public static GetJobResponse of(List<String> jobList) {
        return new GetJobResponse(jobList);
    }
}
