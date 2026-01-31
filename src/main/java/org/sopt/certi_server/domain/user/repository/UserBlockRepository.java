package org.sopt.certi_server.domain.user.repository;

import org.sopt.certi_server.domain.user.entity.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {

    
}
