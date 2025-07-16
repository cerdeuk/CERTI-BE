package org.sopt.certi_server.domain.certification.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.certification.entity.QCertification;
import org.sopt.certi_server.domain.certification.entity.QCertificationJob;
import org.sopt.certi_server.domain.favorite.entity.QFavorite;
import org.sopt.certi_server.domain.user.entity.User;

import java.util.List;

@RequiredArgsConstructor
public class CertificationJobRepositoryImpl implements CertificationJobRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Tuple> findByJobIds(List<Long> jobIds, Long userId) {
        QCertification certification = QCertification.certification;
        QCertificationJob certificationJob = QCertificationJob.certificationJob;
        QFavorite favorite = QFavorite.favorite;

        return jpaQueryFactory.select(certificationJob, favorite)
                .from(certificationJob)
                .join(certificationJob.certification, certification).fetchJoin()
                .leftJoin(favorite).on(certification.id.eq(favorite.certification.id)
                        .and(favorite.user.id.eq(userId)))
                .where(certificationJob.job.id.in(jobIds))
                .distinct()
                .fetch();

    }
}
