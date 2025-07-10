package org.sopt.certi_server.domain.certification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.sopt.certi_server.domain.certification.dto.response.CertificationListResponse;
import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.certification.entity.*;
import org.sopt.certi_server.domain.certification.entity.enums.CertificationType;
import org.sopt.certi_server.domain.certification.repository.*;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.dto.response.CertificationDetailResponse;
import org.sopt.certi_server.domain.certification.entity.enums.TestType;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final AgencyRepository agencyRepository;
    private final CertificationMajorRepository certificationMajorRepository;
    private final UserService userService;
    private final JobRepository jobRepository;
    private final CertificationRepositoryCustom certificationRepositoryCustom;


    public CertificationDetailResponse getCertificationDetail(Long certificationId){
        Certification certification = getCertification(certificationId);
        List<Job> jobs = certificationRepository.getJobsByCertificationId(certificationId);
        return CertificationDetailResponse.from(certification, jobs);
    }

    @Transactional
    public void createCertification(CertificationCreateRequest request) {
        Agency findAgency = getAgencyByName(request.agencyName());
        CertificationType certificationType = CertificationType.from(request.certificationType());
        TestType testType = TestType.from(request.testType());

        Certification newCertification = convertDtoToEntity(request, certificationType, testType, findAgency);

        certificationRepository.save(newCertification);

    }

    public Certification getCertification(Long certificationId) {
        return certificationRepository.findById(certificationId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CERTIFICATION_NOT_FOUND));
    }

    private Agency getAgencyByName(String agencyName) {
        return agencyRepository.findByName(agencyName).orElseThrow(() -> new NotFoundException(ErrorCode.AGENCY_NOT_FOUND));
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
                .nearestTestDate(request.nearestTestDate())
                .tags(request.tags())
                .applicationMethod(request.applicationMethod())
                .applicationUrl(request.applicationUrl())
                .build();
    }

    public CertificationMajor getCertificationMajor(Certification certification, Major major) {
        return certificationMajorRepository.findByCertificationAndMajor(certification, major)
            .orElse(null);
    }

    public CertificationListResponse searchCertification(Long userId, String keyword) {

        User user = userService.getUser(userId);

        List<CertificationSimple> certificationSimpleRespons = certificationRepository.searchByKeyword(user, keyword);

        return CertificationListResponse.of(certificationSimpleRespons);
    }


    public CertificationListResponse getCertificationList(final Long userId, final boolean isFavorite, final String jobName){
        log.info("userId : " + userId + " isFavorite : " + isFavorite + " jobName : " + jobName);
        User user = userService.getUser(userId);
        Job job = jobRepository.findByName(jobName)
            .orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));

        log.info("job : " + job.getName() + "user" + user.getNickname());
        List<CertificationSimple> certificationSimpleList = certificationRepositoryCustom.findByJobAndFavorite(user, isFavorite, job.getId());

        return CertificationListResponse.of(certificationSimpleList);

    }

}
