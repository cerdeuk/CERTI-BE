package org.sopt.certi_server.domain.user.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.acquisition.service.AcquisitionService;
import org.sopt.certi_server.domain.activity.repository.ActivityRepository;
import org.sopt.certi_server.domain.activity.service.ActivityService;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.user.dto.request.UpdateJobRequest;
import org.sopt.certi_server.domain.user.dto.response.GetJobResponse;
import org.sopt.certi_server.domain.user.dto.response.GetUserResponse;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.sopt.certi_server.domain.user.entity.UserMajorImpl;
import org.sopt.certi_server.domain.user.repository.CareerRepository;
import org.sopt.certi_server.domain.user.repository.UserJobRepository;
import org.sopt.certi_server.domain.user.repository.UserMajorImplRepository;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.BusinessException;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMajorImplRepository userMajorImplRepository;
    private final MajorImplRepository majorImplRepository;
    private final UserJobRepository userJobRepository;
    private final JobRepository jobRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final CareerRepository careerRepository;
    private final ActivityRepository activityRepository;

    public User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public GetUserResponse getHomeUser(final Long userId){
        User user = getUser(userId);
        UserMajorImpl userMajor = userMajorImplRepository.findByUserId(userId);
        MajorImpl majorImpl = majorImplRepository.findById(userMajor.getMajorImpl().getId())
            .orElseThrow(()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        int percentage = calculateResumeProgress(user);
        return GetUserResponse.from(user, majorImpl, percentage);
    }

    public GetJobResponse getUserJob(final Long userId){
        getUser(userId);
        List<UserJob> userJobList = userJobRepository.findAllByUserId(userId);
        List<String> jobNameList = userJobList.stream()
            .map(UserJob::getJob)
            .map(Job::getName)
            .toList();

        return GetJobResponse.of(jobNameList);
    }

    @Transactional
    public void updateUserJob(final Long userId, final List<String> jobNameList){
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

    public int calculateResumeProgress(User user){
        int acqCount = acquisitionRepository.countByUser(user);
        int careerCount = careerRepository.countByUser(user);
        int activityCount = activityRepository.countByUser(user);

        int total = acqCount + careerCount + activityCount;

        if(total > 0 && total < 14){
            return total * 7 + 5;
        }

        return 96;
    }

}
