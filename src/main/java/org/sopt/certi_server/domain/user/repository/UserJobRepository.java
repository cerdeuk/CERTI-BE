package org.sopt.certi_server.domain.user.repository;

import org.sopt.certi_server.domain.user.entity.UserJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJobRepository extends JpaRepository<UserJob, Long> {

}
