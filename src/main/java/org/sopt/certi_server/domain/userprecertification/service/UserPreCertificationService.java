package org.sopt.certi_server.domain.userprecertification.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.domain.userprecertification.dto.request.UserPreCertificationRequest;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimple;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimpleListResponse;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPreCertificationService {

    private final UserPreCertificationRepository userPreCertificationRepository;
    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;

    public PreCertificationSimpleListResponse getPreCertificationListDataByUserId(Long userId) {
        return new PreCertificationSimpleListResponse(userPreCertificationRepository.getPreCertificationsByUserId(userId).stream().map(
                PreCertificationSimple::from
        ).collect(Collectors.toList()));
    }

    @Transactional
    public void createNewPreCertification(Long userId, UserPreCertificationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );
        Certification certification = certificationRepository.findById(request.certificationId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND)
        );
        userPreCertificationRepository.save(UserPreCertification.create(user, certification));
    }

    @Transactional
    public void deletePreCertification(Long userId, Long certificationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

        userPreCertificationRepository.deleteByUserAndCertification(user, certification);
    }
}
