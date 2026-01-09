package org.sopt.certi_server.domain.certification.dto.response;

import org.sopt.certi_server.domain.certification.entity.Certification;

public record CertificationRankResponse(
	Long certificationId,
	int rank,
	String certificationName,
	String certificationType
) {
	public CertificationRankResponse(int rank, Certification certification) {
		this(
			certification.getId(),
			rank,
			certification.getName(),
			certification.getCertificationType() != null
				? certification.getCertificationType().getKoreanName()
				: null
		);
	}
}
