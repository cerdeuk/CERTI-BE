package org.sopt.certi_server.domain.acquisition.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.certification.entity.Certification;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetAcquisitionResponse(
	String name,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.DD", timezone = "Asia/Seoul")
	LocalDate createdAt,
	String cardFrontImageUrl,
	String cardBackImageUrl,
	List<String> tags
) {
	public static GetAcquisitionResponse from(Acquisition acquisition) {
		return GetAcquisitionResponse.builder()
			.name(acquisition.getCertification().getName())
			.createdAt(acquisition.getCreatedTime().toLocalDate())
			.cardFrontImageUrl(acquisition.getCardType().getCardFrontImageUrl())
			.cardBackImageUrl(acquisition.getCardType().getCardBackImageUrl())
			.tags(acquisition.getCertification().getTags())
			.build();
	}
}
