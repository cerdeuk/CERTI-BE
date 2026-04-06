package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.certification.entity.Certification;
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
public interface CertificationRepository extends JpaRepository<Certification, Long>, CertificationRepositoryCustom {


    @Query("""
                   select new org.sopt.certi_server.domain.certification.dto.response.CertificationSimple(
                    c,
                    case when f is not null then true else false END
                   ) 
                   from Certification c 
                   left join Favorite f on f.certification = c and f.user = :user 
                   where c.name like concat('%', :keyword, '%') 
            """)
    List<CertificationSimple> searchByKeyword(User user, String keyword);

    Optional<Certification> findByName(String certificationName);

    @Query("SELECT c FROM Certification c LEFT JOIN FETCH c.tags WHERE c.id = :id")
    Optional<Certification> findByIdWithTags(Long id);

    @Query("""
        select c
        from Certification c
        join CertificationTrack ct on ct.certification = c
        left join Favorite f on (f.certification = c)
        left join f.user u on (u = f.user and u.track = :track)
        where ct.track = :track
        group by c.id
        order by count(u) desc
""")
    List<Certification> findTopCertificationsByTrack(
        @Param("track") TrackType track,
        Pageable pageable);


    @Query("""
        select c
        from Certification c
            left join Favorite f on f.certification = c
            join CertificationJob cj on cj.certification = c
        where cj.job.id = :jobId
        group by c
        order by count(f) desc
""")
    List<Certification> findTopByJobOrderByFavoriteCount(
        @Param("jobId") Long jobId,
        Pageable pageable
    );
}
