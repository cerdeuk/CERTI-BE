package org.sopt.certi_server.domain.acquisition.repository;

import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcquisitionRepository extends JpaRepository<Acquisition, Long> {
	Optional<Acquisition> findByUserAndId(User user, Long acquisitionId);

	@Query("select ac from Acquisition ac join fetch ac.certification where ac.user = :user")
	List<Acquisition> findByUserOrderByIdAsc(User user);

	Optional<Acquisition> findFirstByUserOrderByCreatedTimeDesc(User user);

	void deleteAllByCertification(Certification certification);

	List<Acquisition> findAllByUser(User user);

	boolean existsByUserAndCertification(User user, Certification certification);

	int countByUser(User user);
}
