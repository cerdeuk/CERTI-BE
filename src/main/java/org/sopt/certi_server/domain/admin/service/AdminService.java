package org.sopt.certi_server.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.admin.dto.response.*;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationCategory;
import org.sopt.certi_server.domain.certification.entity.CertificationJob;
import org.sopt.certi_server.domain.certification.entity.CertificationMajor;
import org.sopt.certi_server.domain.certification.repository.CertificationCategoryRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationJobRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationMajorRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.favorite.repository.FavoriteRepository;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final CertificationRepository certificationRepository;
    private final CertificationMajorRepository certificationMajorRepository;
    private final CertificationJobRepository certificationJobRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final FavoriteRepository favoriteRepository;
    private final CertificationCategoryRepository certificationCategoryRepository;
    private final UserPreCertificationRepository userPreCertificationRepository;

    public AdminCertificationDetailResponse getCertificationDetail(Long certificationId) {

        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

        List<CertificationMajor> certificationMajorList = certificationMajorRepository.findAllByCertification(certification);
        List<CertificationJob> certificationJobList = certificationJobRepository.findAllByCertification(certification);

        return AdminCertificationDetailResponse.of(
                certificationId,
                certificationMajorList.stream()
                        .map(CertificationMajorSimple::from)
                        .toList(),
                certificationJobList.stream()
                        .map(CertificationJobSimple::from)
                        .toList()
        );
    }

    public AdminCertificationListResponse getAllCertifications() {

        List<Certification> allCertifications = certificationRepository.findAll();

        return AdminCertificationListResponse.of(
                allCertifications.stream()
                        .map(AdminCertificationResponse::from)
                        .toList()
        );
    }

    @Transactional
    public void deleteCertification(Long certificationId) {

        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

        certificationJobRepository.deleteAllByCertification(certification);
        certificationMajorRepository.deleteAllByCertification(certification);
        acquisitionRepository.deleteAllByCertification(certification);
        favoriteRepository.deleteAllByCertification(certification);
        certificationCategoryRepository.deleteAllByCertification(certification);
        userPreCertificationRepository.deleteAllByCertification(certification);

        certificationRepository.delete(certification);
    }
}
