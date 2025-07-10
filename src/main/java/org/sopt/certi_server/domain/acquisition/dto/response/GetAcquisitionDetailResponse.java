package org.sopt.certi_server.domain.acquisition.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.certification.entity.Certification;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetAcquisitionDetailResponse(
	Long acquisitionId,
	String cardFrontImageUrl,
	String cardBackImageUrl,
	int index,
	String name,
	List<String> tags,
	String description,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
	LocalDate createdAt
) {
	public static GetAcquisitionDetailResponse from(Acquisition acquisition) {
		return GetAcquisitionDetailResponse.builder()
			.acquisitionId(acquisition.getId())
			.cardFrontImageUrl(acquisition.getCardType().getCardFrontImageUrl())
			.cardBackImageUrl(acquisition.getCardType().getCardBackImageUrl())
			.index(acquisition.getCardType().getIndex())
			.name(acquisition.getCertification().getName())
			.createdAt(acquisition.getCreatedTime().toLocalDate())
			.description(acquisition.getCertification().getDescription())
			.tags(acquisition.getCertification().getTags())
			.build();
	}
}
