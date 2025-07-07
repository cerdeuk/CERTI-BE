package org.sopt.certi_server.domain.certification.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.Agency;
import org.sopt.certi_server.domain.certification.repository.AgencyRepository;
import org.sopt.certi_server.domain.certification.entity.Category;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.dto.response.CertificationDetailResponse;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.enums.TestType;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
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

    public CertificationDetailResponse getCertificationDetail(Long certificationId){
        Certification certification = getCertification(certificationId);
        List<Category> categories = certificationRepository.getCategoriesByCertificationId(certificationId);
        return CertificationDetailResponse.from(certification, categories);
    }

    @Transactional
    public void createCertification(CertificationCreateRequest request) {
        Agency findAgency = getAgency(request);
        TestType testType = TestType.from(request.testType());
        Certification newCertification = convertDtoToEntity(request, testType, findAgency);
        certificationRepository.save(newCertification);
    }

    public Certification getCertification(Long certificationId) {
        return certificationRepository.findById(certificationId).orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
    }

    private Agency getAgency(CertificationCreateRequest request) {
        return agencyRepository.findById(request.agencyId()).orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
    }

    private Certification convertDtoToEntity(CertificationCreateRequest request, TestType testType, Agency agency) {
        return Certification.builder()
                .agency(agency)
                .name(request.certificationName())
                .testType(testType)
                .averagePeriod(request.averagePeriod())
                .charge(request.charge())
                .description(request.description())
                .testDateInformation(request.testDateInformation())
                .applicationMethod(request.applicationMethod())
                .cardImageUrl(request.cardImageUrl())
                .applicationUrl(request.applicationUrl())
                .build();
    }

}
