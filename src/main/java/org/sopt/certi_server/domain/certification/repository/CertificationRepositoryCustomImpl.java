package org.sopt.certi_server.domain.certification.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.QCertification;
import org.sopt.certi_server.domain.certification.entity.QCertificationJob;
import org.sopt.certi_server.domain.favorite.entity.QFavorite;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CertificationRepositoryCustomImpl implements CertificationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CertificationSimple> findByJobAndFavorite(User user, boolean isFavorite, Long jobId) {
        QCertification certification = QCertification.certification;
        QFavorite favorite = QFavorite.favorite;
        QCertificationJob certificationJob = QCertificationJob.certificationJob;

        // 자격증과 즐겨찾기를 함께 조회
        List<Tuple> results = jpaQueryFactory
                .select(certification, favorite)
                .from(certification)
                .leftJoin(favorite).on(favorite.user.eq(user).and(favorite.certification.eq(certification)))
                .join(certificationJob).on(certificationJob.certification.eq(certification))
                .where(certificationJob.job.id.eq(jobId))
                .fetch();


        return results.stream()
                .map(tuple -> {
                    Certification findCertification = tuple.get(certification);
                    boolean isFav = tuple.get(favorite) != null;

                    if(isFavorite && !isFav){
                        return null;
                    }

                    if(findCertification == null){
                        return null;
                    }

                    return new CertificationSimple(findCertification, isFav);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
