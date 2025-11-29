package org.sopt.certi_server.domain.favorite.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.favorite.entity.Favorite;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.enums.TrackType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("""
            select c 
            from Favorite f 
            join f.certification c 
            join fetch c.agency
            where f.user.id = :userId
            """)
    List<Certification> findFavoriteCertificationsByUserId(Long userId);

    Optional<Favorite> findByUserAndCertification(User user, Certification certification);

    void deleteAllByCertification(Certification certification);

    boolean existsByUserAndCertification(User user, Certification certification);

	void deleteAllByUser(User user);

    int countByUser(User user);

    @Query("""
        select distinct c
        from Favorite f
            join f.certification c
            join CertificationJob cj on cj.certification = c
        where cj.job.id = :jobId
        group by c
        order by count(f) desc, max(cj.weight) desc
        """)
    List<Certification> findTopByJobOrderByFavoriteCount(
        @Param("jobId") Long jobId,
        Pageable pageable
    );

    @Query("""
        select c
        from Favorite f
            join f.certification c
            join f.user u
        where u.track = :track
        group by c
        order by count(f) desc
        """)
    List<Certification> findTopCertificationsByTrack(
        @Param("track") TrackType track,
        Pageable pageable
    );
}
