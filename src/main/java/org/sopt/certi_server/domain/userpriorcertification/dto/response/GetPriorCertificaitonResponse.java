package org.sopt.certi_server.domain.userpriorcertification.dto.response;

import java.security.cert.Certificate;
import java.time.LocalDate;
import java.util.List;

import org.sopt.certi_server.domain.certification.entity.Certification;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record GetPriorCertificaitonResponse(
	String name,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.DD", timezone = "Asia/Seoul")
	LocalDate createdAt,
	String cardImageUrl,
	List<String> tags
) {
	public static GetPriorCertificaitonResponse from(Certification certification) {
		return GetPriorCertificaitonResponse.builder()
			.name(certification.getName())
			.createdAt(LocalDate.now())
			.cardImageUrl(certification.getCardImageUrl())
			.tags(certification.getTags())
			.build();
	}
}
