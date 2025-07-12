package org.sopt.certi_server.domain.certification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.sopt.certi_server.domain.certification.dto.response.*;
import org.sopt.certi_server.domain.certification.entity.*;
import org.sopt.certi_server.domain.certification.entity.enums.CertificationType;
import org.sopt.certi_server.domain.certification.repository.*;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.entity.enums.TestType;
import org.sopt.certi_server.domain.favorite.repository.FavoriteRepository;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.domain.major.repository.MajorRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CacheConfig(cacheManager = "redisCacheManager")
public class CertificationService {

	private final AgencyRepository agencyRepository;
	private final MajorRepository majorRepository;
	private final JobRepository jobRepository;
	private final CertificationRepository certificationRepository;
	private final CertificationMajorRepository certificationMajorRepository;
	private final CertificationJobRepository certificationJobRepository;
	private final CertificationRepositoryCustomImpl certificationRepositoryCustomImpl;
	private final FavoriteRepository favoriteRepository;
	private final UserService userService;



    @Cacheable(value = "certification", key = "#certificationId")
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


    public CertificationRecommendationListResponse recommendCertifications(Long userId){
        User user = userService.getUser(userId);
        List<Major> userMajors = majorRepository.findAllByUser(user);
        System.out.println("사용자 전공 개수 = " + userMajors.size());
        System.out.println("userMajors.get(0).getName() = " + userMajors.get(0).getName());
        List<Job> userJobs = jobRepository.findAllByUser(user);

        List<CertificationMajor> certificationMajors = certificationMajorRepository.findByMajorNames(
                userMajors.stream()
                        .map(Major::getName)
                        .toList()
        );
        System.out.println("사용자 전공 연관 자격증 매핑 개수 = " + certificationMajors.size());

        System.out.println("certificationMajors.size() = " + certificationMajors.size());

        List<CertificationJob> certificationJobs = certificationJobRepository.findByJobNames(
                userJobs.stream()
                        .map(Job::getName)
                        .toList()
        );

        System.out.println("certificationJobs.size() = " + certificationJobs.size());

        return getCertificationRecommendationListResponse(certificationMajors, certificationJobs, user);
    }

    private CertificationRecommendationListResponse getCertificationRecommendationListResponse(List<CertificationMajor> certificationMajors, List<CertificationJob> certificationJobs, User user) {
        Map<Long, List<CertificationMajor>> certificationMajorMapGroupingByCertification = certificationMajors.stream()
                .collect(Collectors.groupingBy(
                        cm -> cm.getCertification().getId()
                ));

        System.out.println("certificationMajorMapGroupingByCertification.size() = " + certificationMajorMapGroupingByCertification.size());

        Map<Long, List<CertificationJob>> certificationJobMapGroupingByCertification = certificationJobs.stream()
                .collect(Collectors.groupingBy(
                        cj -> cj.getCertification().getId()
                ));
        System.out.println("certificationJobMapGroupingByCertification.size() = " + certificationJobMapGroupingByCertification.size());

        Set<Long> allCertificationIds = new HashSet<>();
        allCertificationIds.addAll(certificationMajorMapGroupingByCertification.keySet());
        allCertificationIds.addAll(certificationJobMapGroupingByCertification.keySet());

        List<CertificationScoreDto> recommendationList = allCertificationIds.stream()
                .map(certificationId -> {
                    List<CertificationMajor> certificationMajorListByCertId = certificationMajorMapGroupingByCertification.getOrDefault(certificationId, List.of());
                    System.out.println("자격증 연관 전공 매핑 개수 = " + certificationMajorListByCertId.size());
                    List<CertificationJob> certificationJobListByCertId = certificationJobMapGroupingByCertification.getOrDefault(certificationId, List.of());
                    Certification certification = certificationMajorListByCertId.isEmpty() ? certificationJobListByCertId.get(0).getCertification() : certificationMajorListByCertId.get(0).getCertification();

                    System.out.println("==============" + certification.getName() + "==================");

                    double majorScore = reverseProductScore(certificationMajorListByCertId.stream()
                            .map(CertificationMajor::getWeight));

                    double jobScore = reverseProductScore(certificationJobListByCertId.stream()
                            .map(CertificationJob::getWeight));
                    System.out.println("jobScore = " + jobScore);
                    System.out.println("majorScore = " + majorScore);
                    System.out.println("================================");

                    int finalScore = (int)((majorScore * 0.4 + jobScore * 0.6) * 100);

                    return CertificationScoreDto.from(certification, finalScore, favoriteRepository.existsByUserAndCertification(user, certification));

                })
                .sorted(Comparator.comparing(CertificationScoreDto::recommendationScore).reversed())
                .limit(6)
                .toList();

        return CertificationRecommendationListResponse.of(recommendationList);
    }

    private double reverseProductScore(Stream<Double> weightStream) {
        return 1 - weightStream
                .map(w -> 1 - w)
                .reduce(1.0, (a, b) -> a * b);
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
        User user = userService.getUser(userId);
        Job job = jobRepository.findByName(jobName)
            .orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));

        List<CertificationSimple> certificationSimpleList = certificationRepositoryCustomImpl.findByJobAndFavorite(user, isFavorite, job.getId());

        return CertificationListResponse.of(certificationSimpleList);

    }

}
