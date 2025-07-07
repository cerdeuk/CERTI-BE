package org.sopt.certi_server.domain.userpriorcertification.dto.response;

import java.time.LocalDate;
import java.util.List;

import org.sopt.certi_server.domain.certification.entity.Category;
import org.sopt.certi_server.domain.certification.entity.Certification;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record GetPriorCertificationDetailResponse(
	String cardimageUrl,
	String name,
	List<String> tags,
	List<String> categories,
	String description,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.DD", timezone = "Asia/Seoul")
	LocalDate createdAt
) {
	public static GetPriorCertificationDetailResponse from(Certification certification, List<String> categories) {
		return GetPriorCertificationDetailResponse.builder()
			.cardimageUrl(certification.getCardImageUrl())
			.name(certification.getName())
			.createdAt(LocalDate.now())
			.description(certification.getDescription())
			.tags(certification.getTags())
			.categories(categories)
			.build();
	}
}
