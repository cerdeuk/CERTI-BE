package org.sopt.certi_server.domain.certification.repository;

import java.util.List;

import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.QCertification;
import org.sopt.certi_server.domain.certification.entity.QCertificationJob;
import org.sopt.certi_server.domain.favorite.entity.QFavorite;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CertificationRepositoryCustomImpl implements CertificationRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<CertificationSimple> findByJobAndFavorite(User user, boolean isFavorite, Long jobId) {
		QCertification certification = QCertification.certification;
		QFavorite favorite = QFavorite.favorite;
		QCertificationJob certificationJob = QCertificationJob.certificationJob;

		JPQLQuery<Certification> certificationQuery = jpaQueryFactory
			.select(certification)
			.from(certification)
			.leftJoin(favorite).on(favorite.certification.eq(certification).and(favorite.user.eq(user)))
			.leftJoin(certificationJob).on(certificationJob.certification.eq(certification))
			.where(certificationJob.job.id.eq(jobId));

		if (isFavorite) {
			certificationQuery.where(favorite.isNotNull());
		}else {
			certificationQuery.where();
		}

		return certificationQuery.fetch().stream()
			.map(cert -> new CertificationSimple(
				cert,
				isFavorite
			))
			.toList();
	}
}
