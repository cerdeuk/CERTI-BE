package org.sopt.certi_server.domain.acquisition.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.sopt.certi_server.domain.acquisition.entity.Acquisition;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetAcquisitionResponse(
        Long acquisitionId,
        int index,
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate createdAt,
        String cardFrontImageUrl,
        String cardBackImageUrl,
        List<String> tags
) {
    public static GetAcquisitionResponse from(Acquisition acquisition) {
        return GetAcquisitionResponse.builder()
                .acquisitionId(acquisition.getId())
                .index(acquisition.getCardType().getIndex())
                .name(acquisition.getCertification().getName())
                .createdAt(acquisition.getCreatedTime().toLocalDate())
                .cardFrontImageUrl(acquisition.getCardType().getCardFrontImageUrl())
                .cardBackImageUrl(acquisition.getCardType().getCardBackImageUrl())
                .tags(acquisition.getCertification().getTags())
                .build();
    }
}
