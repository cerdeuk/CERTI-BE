package org.sopt.certi_server.domain.userprecertification.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.domain.userprecertification.dto.request.UserPreCertificationRequest;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimple;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimpleListResponse;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.sopt.certi_server.domain.userprecertification.entity.enums.IconType;
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

    private final UserService userService;
    private final CertificationService certificationService;
    private final UserPreCertificationRepository userPreCertificationRepository;

    public PreCertificationSimpleListResponse getPreCertificationListDataByUserId(Long userId) {
        return new PreCertificationSimpleListResponse(userPreCertificationRepository.getPreCertificationsByUserId(userId).stream().map(
                PreCertificationSimple::from
        ).toList());
    }

    @Transactional
    public void createNewPreCertification(Long userId, Long preCertificationId) {
        User user = userService.getUser(userId);
        Certification certification = certificationService.getCertification(preCertificationId);

        IconType iconType = userPreCertificationRepository.findFirstByUserOrderByCreatedTimeDesc(user)
                .map(userPreCertification -> IconType.issueNextIconType(userPreCertification.getIconType().getIndex()))
                .orElseGet(IconType::issueRandomIconType);

        userPreCertificationRepository.save(UserPreCertification.create(user, certification, iconType));
    }

    @Transactional
    public void deletePreCertification(Long userId, Long certificationId) {
        User user = userService.getUser(userId);
        Certification certification = certificationService.getCertification(certificationId);
        userPreCertificationRepository.deleteByUserAndCertification(user, certification);
    }
}
