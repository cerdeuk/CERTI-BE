package org.sopt.certi_server.domain.acquisition.dto.response;

import java.time.LocalDate;
import java.util.List;

import org.sopt.certi_server.domain.acquisition.entity.Acquisition;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record GetAcquisitionResponse(
	Long acquisitionId,
	String cardFrontImageUrl,
	int index,
	String name,
	List<String> tags,
	String description,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
	LocalDate acquisitionDate,
	String grade
) {
	public static GetAcquisitionResponse from(Acquisition acquisition) {
		return GetAcquisitionResponse.builder()
			.acquisitionId(acquisition.getId())
			.cardFrontImageUrl(acquisition.getCardType().getCardFrontImageUrl())
			.index(acquisition.getCardType().getIndex())
			.name(acquisition.getCertification().getName())
			.acquisitionDate(acquisition.getAcquisitionDate())
			.description(acquisition.getCertification().getDescription())
			.tags(acquisition.getCertification().getTags())
			.grade(acquisition.getGrade())
			.build();
	}
}
