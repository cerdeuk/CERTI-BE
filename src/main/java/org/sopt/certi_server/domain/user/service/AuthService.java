package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.user.dto.request.SignupRequest;
import org.sopt.certi_server.domain.user.dto.response.AuthResponse;
import org.sopt.certi_server.domain.user.dto.response.JwtResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.domain.user.dto.response.SignUpResponse;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.sopt.certi_server.domain.user.entity.UserMajorImpl;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.domain.user.repository.UserJobRepository;
import org.sopt.certi_server.domain.user.repository.UserMajorImplRepository;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.sopt.certi_server.global.jwt.domain.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final UserJobRepository userJobRepository;
    private final MajorImplRepository majorImplRepository;
    private final UserMajorImplRepository userMajorImplRepository;
    private final JwtService jwtService;
    private final KakaoService kakaoService;
    private final UniversityService universityService;

    public AuthResponse login(OAuthUserInformation userInfo) {
        return userRepository.findByEmail(userInfo.email())
                .map(this::handleExistingUser)
                .orElseGet(() -> handleNewUser(userInfo));
    }

    private AuthResponse handleNewUser(OAuthUserInformation userInfo) {
        String preSignupToken = jwtService.generatePreSignupToken(userInfo.email());
        return AuthResponse.ofNotRegisteredUser(preSignupToken, userInfo);
    }

    private AuthResponse handleExistingUser(final User user) {
        JwtResponse jwtResponse = jwtService.issueToken(user.getId());
        return AuthResponse.ofRegisteredUser(user.getId(), user.getNickname(), jwtResponse);
    }

    @Transactional
    public SignUpResponse register(final String authorization, SignupRequest request) {
        log.info(authorization);
        jwtService.validatePreSignupToken(authorization);

        User newUser = convertDtoToEntity(request);

        userRepository.save(newUser);

        List<Job> jobs = request.jobs().stream()
                .map(jobStr -> {
                    Job job = jobRepository.findByName(jobStr)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));
                    userJobRepository.save(UserJob.createUserJob(newUser, job));
                    return job;
                })
                .toList();

        log.info(request.major());

        MajorImpl major = majorImplRepository.findMajorImplByName(request.major())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MAJOR_NOT_FOUND));

        userMajorImplRepository.save(UserMajorImpl.createUserMajorImpl(newUser, major));

        JwtResponse token = jwtService.issueToken(newUser.getId());
        return SignUpResponse.of(
                newUser,
                major,
                jobs,
                token
        );
    }

    public JwtResponse reIssueToken(String authorization) {
        return jwtService.reissueToken(authorization);
    }

    public SocialService getSocialServiceByType(SocialType socialType) {
        return switch (socialType) {
            case KAKAO -> kakaoService;
            case APPLE -> null;
        };
    }

    private User convertDtoToEntity(SignupRequest request) {
        MajorImpl majorImpl = majorImplRepository.findMajorImplByName(request.major())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MAJOR_NOT_FOUND));


        return User.builder()
                .email(request.userInformation().email())
                .nickname(request.userInformation().nickname())
                .profileImageUrl(request.userInformation().profileImageUrl())
                .track(request.track())
                .grade(request.grade())
                .major(majorImpl)
                .university(universityService.getUniversityByName(request.university()))
                .build();
    }

}
