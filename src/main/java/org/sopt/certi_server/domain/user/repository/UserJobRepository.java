package org.sopt.certi_server.domain.user.repository;

import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJobRepository extends JpaRepository<UserJob, Long> {
    List<UserJob> findAllByUserId(Long userId);

    void deleteAllByUser(User user);
}
