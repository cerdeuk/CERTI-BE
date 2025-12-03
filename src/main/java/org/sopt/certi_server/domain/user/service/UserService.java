package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.activity.repository.ActivityRepository;
import org.sopt.certi_server.domain.favorite.repository.FavoriteRepository;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.user.dto.request.UpdateUserRequest;
import org.sopt.certi_server.domain.user.dto.response.GetJobResponse;
import org.sopt.certi_server.domain.user.dto.response.GetMyPageInfoResponse;
import org.sopt.certi_server.domain.user.dto.response.GetUserResponse;
import org.sopt.certi_server.domain.user.dto.response.PersonalInformationResponse;
import org.sopt.certi_server.domain.user.entity.University;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.sopt.certi_server.domain.user.repository.CareerRepository;
import org.sopt.certi_server.domain.user.repository.UserJobRepository;
import org.sopt.certi_server.domain.user.repository.UserMajorImplRepository;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMajorImplRepository userMajorImplRepository;
    private final MajorImplRepository majorImplRepository;
    private final UserJobRepository userJobRepository;
    private final JobRepository jobRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final UserPreCertificationRepository userPreCertificationRepository;
    private final FavoriteRepository favoriteRepository;
    private final CareerRepository careerRepository;
    private final ActivityRepository activityRepository;

    public User getUser(final Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public GetUserResponse getHomeUser(final Long userId) {
        User user = getUser(userId);
        List<MajorImpl> majorList = majorImplRepository.findMajorImplByUser(user);

        if(majorList.isEmpty()){
            throw new NotFoundException(ErrorCode.MAJOR_NOT_FOUND);
        }

        int percentage = calculateResumeProgress(user);
        return GetUserResponse.from(user, majorList.get(0), percentage);
    }

    public GetJobResponse getUserJob(final Long userId) {
        getUser(userId);
        List<UserJob> userJobList = userJobRepository.findAllByUserId(userId);
        List<String> jobNameList = userJobList.stream()
                .map(UserJob::getJob)
                .map(Job::getName)
                .toList();

        return GetJobResponse.of(jobNameList);
    }

    @Transactional
    public void updateUserJob(final Long userId, final List<String> jobNameList) {
        User user = getUser(userId);

        //기존 값 삭제
        userJobRepository.deleteAllByUser(user);

        //새로운 값으로 갱신
        List<UserJob> userJobList = jobNameList.stream()
                .map(jobName -> {
                    Job job = jobRepository.findByName(jobName)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));
                    return UserJob.builder()
                            .job(job)
                            .user(user)
                            .build();
                })
                .toList();

        userJobRepository.saveAll(userJobList);
    }

    public int calculateResumeProgress(final User user) {
        int acqCount = acquisitionRepository.countByUser(user);
        log.info(acqCount + " acquisitions");
        int careerCount = careerRepository.countByUser(user);
        log.info(careerCount + " careers");
        int activityCount = activityRepository.countByUser(user);
        log.info(activityCount + " activities");

        int total = acqCount + careerCount + activityCount;
        log.info(total + " total acquisitions");

        if (total >= 0 && total < 14) {
            return total * 7 + 5;
        }

        return 96;
    }

    public GetMyPageInfoResponse getMyPageInfoResponse(final Long userId){


        // user 정보(닉네임, 이메일)
        User user = getUser(userId);

        // 직무 정보
        GetJobResponse jobResponse = getUserJob(userId);

        // 취득 예정, 취득, 즐겨찾기 자격증 개수
        int upCount = userPreCertificationRepository.countByUser(user);
        int acCount = acquisitionRepository.countByUser(user);
        int fCount = favoriteRepository.countByUser(user);

        return GetMyPageInfoResponse.from(user, jobResponse, upCount, acCount, fCount);

    }

    public PersonalInformationResponse getPersonalInformationResponse(final Long userId){
        User user = getUser(userId);
        return PersonalInformationResponse.from(user);
    }

    @Transactional
    public void updateUserInformation(final Long userId, final UpdateUserRequest request) {
        User user = getUser(userId);

        NicknameValidationType type = validateKeyword(request.nickName());
        if(type != NicknameValidationType.VALID){
            throw new IllegalArgumentException("올바르지 않은 닉네임 형식입니다.");
        }

        user.changeUser(
                request.name(),
                request.nickName(),
                request.email(),
                request.birthDate()
        );
    }

    public NicknameValidationResponse validateNickname(String nickname) {

        NicknameValidationType type = validateKeyword(nickname);
        return NicknameValidationResponse.from(type);
    }

    public NicknameValidationType validateKeyword(String keyword) {

        // 공백 검사
        if (keyword.isEmpty() || keyword.isBlank()){
                return NicknameValidationType.EMPTY;
        }

        // 길이 검사
        if(keyword.length() > 7){
                return NicknameValidationType.TOO_LONG;
        }

        // 중복 검사
        if (userRepository.existsByNickname(keyword)){
                return NicknameValidationType.DUPLICATE;
        }

        // 욕설 검사
        if (profanityFilter.containsProfanity(keyword)){
                return NicknameValidationType.PROFANITY;

        }

        return NicknameValidationType.VALID;
    }


    @Transactional
    public void changeUniversity(final Long userId, final String universityName) {
        User user = getUser(userId);

        University university = universityRepository.findByName(universityName)
                .orElseThrow(() -> new NotFoundException(ErrorCode.UNIVERSITY_NOT_FOUND));

        user.changeUniversity(university);
    }

    @Transactional
    public void changeMajor(final Long userId, final String majorName) {
        User user = getUser(userId);

        MajorImpl mi = majorImplRepository.findMajorImplByName(majorName)
                .orElseThrow(() ->  new NotFoundException(ErrorCode.MAJOR_NOT_FOUND));

        user.changeMajor(mi);
    }
}
