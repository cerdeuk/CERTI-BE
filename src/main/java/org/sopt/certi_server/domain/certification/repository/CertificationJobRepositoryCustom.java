package org.sopt.certi_server.domain.certification.repository;

import com.querydsl.core.Tuple;

import java.util.List;

public interface CertificationJobRepositoryCustom {

    List<Tuple> findByJobIds(List<Long> jobIds, Long userId);
}
