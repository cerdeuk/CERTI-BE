package org.sopt.certi_server.domain.certification.repository;


import com.querydsl.core.Tuple;

import java.util.List;

public interface CertificationMajorRepositoryCustom {

    List<Tuple> findByMajorIds(List<Long> majorIds, Long userId);
}
