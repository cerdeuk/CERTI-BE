package org.sopt.certi_server.domain.user.dto.response;

import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.user.entity.User;

import java.util.List;

public record SignUpResponse(
        Long userId,
        String nickName,
        String university,
        String trackType,
        String major,
        List<String> jobs,
        JwtResponse jwtResponse
) {

    public static SignUpResponse of(User user, MajorImpl major, List<Job> jobs, JwtResponse jwtResponse){
        return new SignUpResponse(
                user.getId(),
                user.getNickname(),
                user.getUniversityName(),
                user.getTrack().getName(),
                major.getName(),
                jobs.stream()
                        .map(Job::getName)
                        .toList(),
                jwtResponse
                );
    }
}
