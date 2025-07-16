package org.sopt.certi_server.domain.certification.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.QCertification;
import org.sopt.certi_server.domain.certification.entity.QCertificationMajor;
import org.sopt.certi_server.domain.favorite.entity.QFavorite;
import org.sopt.certi_server.domain.major.entity.QMajor;

import java.util.List;

@RequiredArgsConstructor
public class CertificationMajorRepositoryImpl implements CertificationMajorRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> findByMajorIds(List<Long> majorIds, Long userId) {
        QCertification certification = QCertification.certification;
        QCertificationMajor certificationMajor = QCertificationMajor.certificationMajor;
        QFavorite favorite = QFavorite.favorite;

        return jpaQueryFactory.select(certificationMajor, favorite)
                .from(certificationMajor)
                .join(certificationMajor.certification, certification).fetchJoin()
                .leftJoin(favorite)
                    .on(favorite.certification.eq(certification)
                            .and(favorite.user.id.eq(userId)))
                .where(certificationMajor.major.id.in(majorIds))
                .distinct()
                .fetch();
    }
}
