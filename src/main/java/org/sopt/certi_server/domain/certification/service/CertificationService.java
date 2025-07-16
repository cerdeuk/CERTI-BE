package org.sopt.certi_server.domain.certification.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.dto.response.*;
import org.sopt.certi_server.domain.certification.entity.*;
import org.sopt.certi_server.domain.certification.entity.enums.CertificationType;
import org.sopt.certi_server.domain.certification.entity.enums.TestType;
import org.sopt.certi_server.domain.certification.repository.*;
import org.sopt.certi_server.domain.favorite.entity.Favorite;
import org.sopt.certi_server.domain.favorite.entity.QFavorite;
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
@Slf4j
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


    @Cacheable(
            value = "certification",
            key = "#certificationId"
    )
    public CertificationDetailResponse getCertificationDetail(final Long certificationId) {
        Certification certification = getCertification(certificationId);
        List<Job> jobs = jobRepository.getJobsByCertificationId(certificationId);
        return CertificationDetailResponse.from(certification, jobs);
    }

    @Transactional
    public void createCertification(CertificationCreateRequest request) {
        Certification newCertification = convertDtoToEntity(request);

        certificationRepository.save(newCertification);
    }


    public CertificationRecommendationListResponse recommendCertifications(Long userId){
        User user = userService.getUser(userId);
        List<Major> userMajors = majorRepository.findAllByUser(user);
        log.info("=============사용자 전공================");
        for (Major userMajor : userMajors) {
            log.info("id = {}", userMajor.getId());
            log.info("major name = {}", userMajor.getName());
        }
        List<Job> userJobs = jobRepository.findAllByUser(user);
        log.info("=============사용자 직무================");
        for (Job userJob : userJobs) {
            log.info("id = {}", userJob.getId());
            log.info("job name = {}", userJob.getName());
        }

        List<Tuple> certificationMajorAndFavoriteTupleList = certificationMajorRepository.findByMajorIds(
                userMajors.stream()
                        .map(Major::getId)
                        .toList(),
                userId
        );

        List<Tuple> certificationJobAndFavoriteTupleList = certificationJobRepository.findByJobIds(
                userJobs.stream()
                        .map(Job::getId)
                        .toList(),
                userId
        );

        Map<Long, List<Tuple>> certificationMajorMap = certificationMajorAndFavoriteTupleList.stream()
                .collect(Collectors.groupingBy(
                        tuple -> Objects.requireNonNull(tuple.get(QCertificationMajor.certificationMajor)).getCertification().getId()
                ));

        Map<Long, List<Tuple>> certificationJobMap = certificationJobAndFavoriteTupleList.stream()
                .collect(Collectors.groupingBy(
                        tuple -> Objects.requireNonNull(tuple.get(QCertificationJob.certificationJob)).getCertification().getId()
                ));

        Set<Long> allCertIds = new HashSet<>();
        allCertIds.addAll(certificationMajorMap.keySet());
        allCertIds.addAll(certificationJobMap.keySet());

        List<CertificationScoreDto> recommendationList = allCertIds.stream()
                .map(certId -> {
                    List<Tuple> certificationMajors = certificationMajorMap.get(certId);
                    List<Tuple> certificationJobs = certificationJobMap.get(certId);

                    Certification certification = certificationMajors.isEmpty() ? certificationJobs.get(0).get(QCertificationJob.certificationJob).getCertification() : certificationMajors.get(0).get(QCertificationMajor.certificationMajor).getCertification();
                    Favorite favorite = certificationMajors.isEmpty() ? certificationJobs.get(0).get(QFavorite.favorite) : certificationMajors.get(0).get(QFavorite.favorite);

                    double majorScore = certificationMajors != null ? reverseProductScore(certificationMajors.stream()
                            .map(tuple -> tuple.get(QCertificationMajor.certificationMajor).getWeight())) : 0;
                    double jobScore = certificationJobs != null ? reverseProductScore(certificationJobs.stream()
                            .map(tuple -> tuple.get(QCertificationJob.certificationJob).getWeight())) : 0;

                    int finalScore = (int) ((majorScore * 0.4 + jobScore * 0.6) * 100);

                    return CertificationScoreDto.from(certification, finalScore, favorite != null);
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

    public Certification getCertification(final Long certificationId) {
        return certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CERTIFICATION_NOT_FOUND));
    }


    private Certification convertDtoToEntity(CertificationCreateRequest request) {
        Agency findAgency = agencyRepository.findByName(request.agencyName())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AGENCY_NOT_FOUND));

        CertificationType findCertificationType = CertificationType.from(request.certificationType());

        TestType findTestType = TestType.from(request.testType());

        return Certification.builder()
                .agency(findAgency)
                .name(request.certificationName())
                .certificationType(findCertificationType)
                .testType(findTestType)
                .averagePeriod(request.averagePeriod())
                .charge(request.charge())
                .description(request.description())
                .testDateInformation(request.testDateInformation())
                .nearestTestDate(request.nearestTestDate())
                .tags(request.tags())
                .expirationPeriod(request.expirationPeriod())
                .applicationMethod(request.applicationMethod())
                .applicationUrl(request.applicationUrl())
                .build();
    }

    public List<CertificationSimple> searchCertification(final Long userId, final String keyword) {

        User user = userService.getUser(userId);

        return certificationRepository.searchByKeyword(user, keyword);
    }


    public CertificationListResponse getCertificationList(final Long userId, final boolean isFavorite, final String jobName) {
        User user = userService.getUser(userId);
        Job job = jobRepository.findByName(jobName)
                .orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));

        List<CertificationSimple> certificationSimpleList = certificationRepositoryCustomImpl.findByJobAndFavorite(user, isFavorite, job.getId());

        return CertificationListResponse.of(certificationSimpleList);

    }

}
