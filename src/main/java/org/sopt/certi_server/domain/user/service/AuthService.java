package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.user.dto.request.SignupRequest;
import org.sopt.certi_server.domain.user.dto.response.AuthResponse;
import org.sopt.certi_server.domain.user.dto.response.JwtResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.domain.user.entity.enums.TrackType;
import org.sopt.certi_server.domain.user.repository.UserJobRepository;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.sopt.certi_server.global.jwt.domain.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final UserJobRepository userJobRepository;
    private final MajorImplRepository majorImplRepository;
    private final JwtService jwtService;
    private final KakaoService kakaoService;

    public AuthResponse login(OAuthUserInformation userInfo){
        return userRepository.findByEmail(userInfo.email())
                .map(this::handleExistingUser)
                .orElseGet(() -> handleNewUser(userInfo));
    }

    private AuthResponse handleNewUser(OAuthUserInformation userInfo) {
        String preSignupToken = jwtService.generatePreSignupToken(userInfo.email());
        return AuthResponse.ofNotRegisteredUser(preSignupToken, userInfo);
    }

    private AuthResponse handleExistingUser(User user) {
        JwtResponse jwtResponse = jwtService.issueToken(user.getId());
        return AuthResponse.ofRegisteredUser(user.getId(), jwtResponse);
    }

    @Transactional
    public AuthResponse register(String authorization, SignupRequest request) {

        jwtService.validatePreSignupToken(authorization);

        User newUser = convertDtoToEntity(request);

        userRepository.save(newUser);

        request.jobs()
                .forEach(str -> {
                    Job job = jobRepository.findByName(str)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));
                    userJobRepository.save(UserJob.createUserJob(newUser, job));
                });

        JwtResponse token = jwtService.issueToken(newUser.getId());
        return AuthResponse.ofRegisteredUser(newUser.getId(), token);
    }

    public JwtResponse reIssueToken(String authorization) {
        return jwtService.reissueToken(authorization);
    }

    public SocialService getSocialServiceByType(SocialType socialType) {
        return switch (socialType){
            case KAKAO -> kakaoService;
            case APPLE -> null;
        };
    }

    private User convertDtoToEntity(SignupRequest request){
        MajorImpl majorImpl = majorImplRepository.findMajorImplByName(request.major())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MAJOR_NOT_FOUND));


        return User.builder()
                .email(request.userInformation().email())
                .nickname(request.userInformation().nickname())
                .profileImageUrl(request.userInformation().profileImageUrl())
                .track(request.track())
                .grade(request.grade())
                .major(majorImpl)
                .universityName(request.university())
                .build();
    }

}
