package org.sopt.certi_server.domain.user.repository;

import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserJobRepository extends JpaRepository<UserJob, Long> {
    List<UserJob> findAllByUserId(Long userId);

    void deleteAllByUser(User user);

    @Query("SELECT uj FROM UserJob uj " +
            "JOIN FETCH uj.job j " +
            "JOIN FETCH uj.user u " +
            "WHERE uj.user IN :users")
    List<UserJob> findWithJobByUserIn(@Param("users") List<User> users);
}
