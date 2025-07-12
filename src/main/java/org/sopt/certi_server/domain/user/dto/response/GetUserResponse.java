package org.sopt.certi_server.domain.user.dto.response;

import lombok.Builder;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.user.entity.User;

@Builder
public record GetUserResponse(
        String name,
        String university,
        String major,
        int percentage
) {
    public static GetUserResponse from(User user, MajorImpl majorImpl, int percentage) {
        return GetUserResponse.builder()
                .name(user.getNickname())
                .university(user.getUniversity().getName())
                .major(majorImpl.getName())
                .percentage(percentage)
                .build();
    }
}
