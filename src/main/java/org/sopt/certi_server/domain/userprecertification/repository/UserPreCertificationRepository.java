package org.sopt.certi_server.domain.userprecertification.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.userprecertification.dto.response.DayDotRes;
import org.sopt.certi_server.domain.userprecertification.dto.response.MonthCalendarRes;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserPreCertificationRepository extends JpaRepository<UserPreCertification, Long> {


    // 원래 fetch join의 대상에는 별칭을 지정해주어선 안되지만, 체인 형식으로 쓰는 경우에는 사용할 수 있도록 함
    @Query("""
            select upc 
            from UserPreCertification upc 
            join fetch upc.certification c 
            left join fetch c.agency
            where upc.user.id = :userId
            order by upc.certification.nearestTestDate asc
            """)
    List<UserPreCertification> findPreCertificationsByUserIdOrderByNearestTestDate(Long userId);

    Optional<UserPreCertification> findFirstByUserOrderByCreatedTimeDesc(User user);

    void deleteAllByCertification(Certification certification);

    boolean existsByUserAndCertification(User user, Certification certification);

    Optional<UserPreCertification> findByUserAndCertification(User user, Certification certification);

	void deleteAllByUser(User user);

    @Query("SELECT upc FROM UserPreCertification upc " +
            "JOIN FETCH upc.user u " +
            "WHERE upc.certification.id = :certificationId AND upc.user IN :users")
    List<UserPreCertification> findByCertificationUserIn(
            @Param("certificationId") Long certificationId,
            @Param("users") List<User> users
    );

    int countByUser(User user);

    @Query("""
    select function('day', upc.testDate) as day,
           count(upc.id) as count
    from UserPreCertification upc
    where upc.user.id = :userId
      and upc.testDate between :start and :end
    group by function('day', upc.testDate)
    order by function('day', upc.testDate) asc
""")
    List<DayDotProjection> findMonthDots(
        @Param("userId") Long userId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("""
        select upc
        from UserPreCertification upc
        join fetch upc.certification c
        where upc.user.id = :userId
          and upc.testDate between :start and :end
        order by upc.testDate asc
    """)
    List<UserPreCertification> findDayItems(
        @Param("userId") Long userId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    public interface DayDotProjection {
        Integer getDay();
        Long getCount();
    }
}
