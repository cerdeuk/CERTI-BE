package org.sopt.certi_server.domain.user.dto.response;

import lombok.Builder;
import org.sopt.certi_server.domain.user.entity.User;

import java.util.List;

@Builder
public record GetMyPageInfoResponse(
        String name,
        String email,
        GetJobResponse jobResponse,
        int upCount,
        int acCount,
        int fCount
) {
    public static GetMyPageInfoResponse from(
            User user, GetJobResponse jobResponse, int upCount, int acCount, int fCount
    ){
        return GetMyPageInfoResponse.builder()
                .name(user.getNickname())
                .email(user.getEmail())
                .jobResponse(jobResponse)
                .upCount(upCount)
                .acCount(acCount)
                .fCount(fCount)
                .build();
    }
}
