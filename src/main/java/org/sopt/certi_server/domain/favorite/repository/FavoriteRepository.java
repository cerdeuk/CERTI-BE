package org.sopt.certi_server.domain.favorite.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.favorite.entity.Favorite;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
