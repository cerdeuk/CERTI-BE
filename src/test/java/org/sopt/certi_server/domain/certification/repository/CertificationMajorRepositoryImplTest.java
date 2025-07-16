package org.sopt.certi_server.domain.certification.repository;

import com.querydsl.core.Tuple;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CertificationMajorRepositoryImplTest {

    @Autowired
    CertificationMajorRepository certificationMajorRepository;

    @Test
    void certification_major_find_by_major_id_and_user_id(){
        // given
        List<Long> majorIds = new ArrayList<>();
        majorIds.add(8L);

        Long userId = 1L;

        // when
        List<Tuple> byMajorIds = certificationMajorRepository.findByMajorIds(majorIds, userId);

        // then
        Assertions.assertThat(byMajorIds.size()).isEqualTo(54);

    }
}
