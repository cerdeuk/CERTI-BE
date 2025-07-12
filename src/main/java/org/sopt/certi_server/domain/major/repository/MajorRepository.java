package org.sopt.certi_server.domain.major.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
	Optional<Major> findByName(String name);

	@Query("""
		select mi.major
		from MajorImpl mi
		join UserMajorImpl umi on mi.id = umi.majorImpl.id
		where umi.user = :user
""")
	List<Major> findAllByUser(User user);
}
