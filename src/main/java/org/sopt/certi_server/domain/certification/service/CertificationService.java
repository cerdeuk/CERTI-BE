package org.sopt.certi_server.domain.certification.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
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
import org.sopt.certi_server.domain.user.entity.enums.TrackType;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CertificationService {

    private final AgencyRepository agencyRepository;
    private final MajorRepository majorRepository;
    private final JobRepository jobRepository;
    private final CertificationRepository certificationRepository;
    private final CertificationMajorRepository certificationMajorRepository;
    private final CertificationJobRepository certificationJobRepository;
    private final CertificationRepositoryCustomImpl certificationRepositoryCustomImpl;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final UserPreCertificationRepository userPreCertificationRepository;
    private final CertificationTrackRepository certificationTrackRepository;


    public CertificationDetailResponse getCertificationDetail(final Long userId, final Long certificationId) {
        User user = userService.getUser(userId);
        Certification certification = getCertification(certificationId);

        boolean acExists = acquisitionRepository.existsByUserAndCertification(user, certification);
        boolean prExists = userPreCertificationRepository.existsByUserAndCertification(user, certification);

        String state = "NORMAL";
        if(acExists) state = "ACQUISITION";
        if(prExists) state = "ANTICIPATED";

        return CertificationDetailResponse.from(certification, state);
    }

    @Transactional
    public void createCertification(CertificationCreateRequest request) {
        Certification newCertification = convertDtoToEntity(request);

        certificationRepository.save(newCertification);

        TrackType trackType = request.trackType();

        CertificationTrack ct = CertificationTrack.builder()
                .certification(newCertification)
                .track(trackType)
                .build();

        certificationTrackRepository.save(ct);
    }


//    @Cacheable(
//            value = "certification_recommend",
//            key = "#userId"
//    )
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

                    Certification certification = certificationMajors == null ?
                            Objects.requireNonNull(certificationJobs.get(0).get(QCertificationJob.certificationJob)).getCertification() :
                            Objects.requireNonNull(certificationMajors.get(0).get(QCertificationMajor.certificationMajor)).getCertification();

                    Favorite favorite = certificationMajors == null ?
                            certificationJobs.get(0).get(QFavorite.favorite) :
                            certificationMajors.get(0).get(QFavorite.favorite);

                    double majorScore = certificationMajors != null ? reverseProductScore(certificationMajors.stream()
                            .map(tuple -> Objects.requireNonNull(tuple.get(QCertificationMajor.certificationMajor)).getWeight())) : 0;
                    double jobScore = certificationJobs != null ? reverseProductScore(certificationJobs.stream()
                            .map(tuple -> Objects.requireNonNull(tuple.get(QCertificationJob.certificationJob)).getWeight())) : 0;

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
        return certificationRepository.findByIdWithTags(certificationId)
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


    public CertificationListResponse getCertificationByJobList(final Long userId, final boolean isFavorite, final String jobName) {
        User user = userService.getUser(userId);
        Job job = jobRepository.findByName(jobName)
                .orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));

        List<CertificationSimple> certificationSimpleList = certificationRepositoryCustomImpl.findByJobAndFavorite(user, isFavorite, job.getId());

        return CertificationListResponse.of(certificationSimpleList);

    }

    public CertificationListResponse getCertificationByTrackList(final Long userId, final boolean isFavorite, final String track) {
        User user = userService.getUser(userId);

        TrackType trackType = TrackType.from(track);

        List<CertificationSimple> certificationSimpleList =
            certificationRepositoryCustomImpl.findByTrackAndFavorite(user, isFavorite, trackType);

        return CertificationListResponse.of(certificationSimpleList);
    }



    public List<CertificationRankResponse> getCertificationJob(final Long userId){
        User user = userService.getUser(userId);
        List<String> jobList = userService.getUserJob(userId).jobList();
        if (jobList.isEmpty()) {
            throw new NotFoundException(ErrorCode.JOB_NOT_FOUND);
        }
        String jobName = jobList.get(0);
        Job job = jobRepository.findByName(jobName)
            .orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));

        Pageable top3 = PageRequest.of(0, 3);
        List<Certification> certList =
            certificationRepository.findTopByJobOrderByFavoriteCount(job.getId(), top3);

        AtomicInteger rank = new AtomicInteger(1);

        return certList.stream()
            .map(cert -> new CertificationRankResponse(rank.getAndIncrement(), cert))
            .toList();
    }

    public List<CertificationRankResponse> getCertificationTrack(final Long userId){
        User user = userService.getUser(userId);
        TrackType trackType = user.getTrack();

        Pageable top3 = PageRequest.of(0, 3);

        List<Certification> certificationList = certificationRepository.findTopCertificationsByTrack(trackType, top3);

        AtomicInteger rank = new AtomicInteger(1);

        return certificationList.stream()
            .map(c -> new CertificationRankResponse(rank.getAndIncrement(), c))
            .toList();
    }

}
