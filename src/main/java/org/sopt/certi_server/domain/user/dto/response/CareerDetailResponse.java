package org.sopt.certi_server.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.sopt.certi_server.domain.user.entity.Career;

import java.time.LocalDate;

@Builder
public record CareerDetailResponse(
        Long careerId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endAt,
        String name,
        String description,
        String place
) {
    public static CareerDetailResponse from(Career career) {
        return CareerDetailResponse.builder()
                .careerId(career.getId())
                .startAt(career.getStartAt())
                .endAt(career.getEndAt())
                .name(career.getName())
                .description(career.getDescription())
                .place(career.getPlace())
                .build();
    }
}
