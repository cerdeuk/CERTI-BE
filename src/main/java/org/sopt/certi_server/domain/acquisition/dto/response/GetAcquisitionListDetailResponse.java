package org.sopt.certi_server.domain.acquisition.dto.response;

import java.time.LocalDate;
import java.util.List;

import org.sopt.certi_server.domain.acquisition.entity.Acquisition;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record GetAcquisitionListDetailResponse(
	Long acquisitionId,
	String cardFrontImageUrl,
	int index,
	String name,
	List<String> tags,
	String description,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
	LocalDate createdAt
) {
	public static GetAcquisitionListDetailResponse from(Acquisition acquisition) {
		return GetAcquisitionListDetailResponse.builder()
			.acquisitionId(acquisition.getId())
			.cardFrontImageUrl(acquisition.getCardType().getCardFrontImageUrl())
			.index(acquisition.getCardType().getIndex())
			.name(acquisition.getCertification().getName())
			.createdAt(acquisition.getCreatedTime().toLocalDate())
			.description(acquisition.getCertification().getDescription())
			.tags(acquisition.getCertification().getTags())
			.build();
	}
}
