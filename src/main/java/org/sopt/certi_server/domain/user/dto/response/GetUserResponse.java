package org.sopt.certi_server.domain.user.dto.response;

import lombok.Builder;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.user.entity.User;

import java.time.LocalDate;

@Builder
public record GetUserResponse(
        Long userId,
        String nickname,
        String name,
        String university,
        String major,
        String job,
        String profileImage,
        LocalDate birthDate,
        int percentage
) {
    public static GetUserResponse from(User user, MajorImpl majorImpl, Job job, int percentage) {
        return GetUserResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .name(user.getName())
                .university(user.getUniversity().getName())
                .major(majorImpl.getName())
                .job(job.getName())
                .profileImage(user.getProfileImageUrl())
                .percentage(percentage)
                .birthDate(user.getBirthDate())
                .build();
    }
}
