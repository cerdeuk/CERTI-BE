package org.sopt.certi_server.domain.certification.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.Agency;
import org.sopt.certi_server.domain.certification.entity.CertificationMajor;
import org.sopt.certi_server.domain.certification.entity.enums.CertificationType;
import org.sopt.certi_server.domain.certification.repository.AgencyRepository;
import org.sopt.certi_server.domain.certification.entity.Category;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.dto.response.CertificationDetailResponse;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.enums.TestType;
import org.sopt.certi_server.domain.certification.repository.CertificationMajorRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final AgencyRepository agencyRepository;
    private final CertificationMajorRepository certificationMajorRepository;

    public CertificationDetailResponse getCertificationDetail(Long certificationId){
        Certification certification = getCertification(certificationId);
        List<Category> categories = certificationRepository.getCategoriesByCertificationId(certificationId);
        return CertificationDetailResponse.from(certification, categories);
    }

    @Transactional
    public void createCertification(CertificationCreateRequest request) {
        Agency findAgency = getAgency(request);
        CertificationType certificationType = CertificationType.from(request.certificationType());
        TestType testType = TestType.from(request.testType());
        Certification newCertification = convertDtoToEntity(request, certificationType, testType, findAgency);
        certificationRepository.save(newCertification);
    }

    public Certification getCertification(Long certificationId) {
        return certificationRepository.findById(certificationId).orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
    }

    private Agency getAgency(CertificationCreateRequest request) {
        return agencyRepository.findByName(request.agencyName()).orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
    }

    private Certification convertDtoToEntity(CertificationCreateRequest request, CertificationType certificationType, TestType testType, Agency agency) {
        return Certification.builder()
                .agency(agency)
                .name(request.certificationName())
                .certificationType(certificationType)
                .testType(testType)
                .averagePeriod(request.averagePeriod())
                .charge(request.charge())
                .description(request.description())
                .testDateInformation(request.testDateInformation())
                .applicationMethod(request.applicationMethod())
                .applicationUrl(request.applicationUrl())
                .build();
    }

    public CertificationMajor getCertificationMajor(Certification certification, Major major) {
        CertificationMajor certificationMajor = certificationMajorRepository.findByCertificationAndMajor(certification, major)
            .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

        return certificationMajor;
    }

}
