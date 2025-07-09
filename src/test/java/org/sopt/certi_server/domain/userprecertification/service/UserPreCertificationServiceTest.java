package org.sopt.certi_server.domain.userprecertification.service;

import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.sopt.certi_server.domain.userprecertification.entity.enums.IconType;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserPreCertificationServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    UserPreCertificationRepository userPreCertificationRepository;

    @Autowired
    UserPreCertificationService userPreCertificationService;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    @Rollback(value = false)
    void preCertificationTest(){
        // given
        User user = User.createUser("lee", "ekjs", "asdf");
        userRepository.save(user);
        Certification certification1 = Certification.builder()
                .name("정처기1")
                .build();

        Certification certification2 = Certification.builder()
                .name("정처기2")
                .build();

        Certification certification3 = Certification.builder()
                .name("정처기3")
                .build();

        Certification certification4 = Certification.builder()
                .name("정처기4")
                .build();

        Certification certification5 = Certification.builder()
                .name("정처기5")
                .build();

        Certification certification6 = Certification.builder()
                .name("정처기6")
                .build();

        certificationRepository.save(certification1);
        certificationRepository.save(certification2);
        certificationRepository.save(certification3);
        certificationRepository.save(certification4);
        certificationRepository.save(certification5);
        certificationRepository.save(certification6);

        em.flush();
        em.clear();

        // when
        userPreCertificationService.createNewPreCertification(user.getId(), certification1.getId());
        userPreCertificationService.createNewPreCertification(user.getId(), certification2.getId());
        userPreCertificationService.createNewPreCertification(user.getId(), certification3.getId());
        userPreCertificationService.createNewPreCertification(user.getId(), certification4.getId());
        userPreCertificationService.createNewPreCertification(user.getId(), certification5.getId());
        userPreCertificationService.createNewPreCertification(user.getId(), certification6.getId());

        // then
        List<UserPreCertification> preCertificationsByUserId = userPreCertificationRepository.getPreCertificationsByUserId(user.getId());

        System.out.println("preCertificationsByUserId.size() = " + preCertificationsByUserId.size());
        for (UserPreCertification userPreCertification : preCertificationsByUserId) {
            System.out.println("userPreCertification.getIconType().getIndex() = " + userPreCertification.getIconType().getIndex());
        }
    }

}
