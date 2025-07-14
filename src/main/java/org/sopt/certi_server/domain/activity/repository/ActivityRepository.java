package org.sopt.certi_server.domain.activity.repository;

import org.sopt.certi_server.domain.user.entity.Activity;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUser(User user);

    int countByUser(User user);

	void deleteALlByUser(User user);
}
