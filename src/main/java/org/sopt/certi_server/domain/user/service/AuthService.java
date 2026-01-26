package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.activity.repository.ActivityRepository;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentLikeRepository;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentRepository;
import org.sopt.certi_server.domain.favorite.repository.FavoriteRepository;
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
import org.sopt.certi_server.domain.user.repository.CareerRepository;
import org.sopt.certi_server.domain.user.repository.UserJobRepository;
import org.sopt.certi_server.domain.user.repository.UserMajorImplRepository;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.sopt.certi_server.global.jwt.domain.service.JwtService;
import org.sopt.certi_server.global.jwt.domain.service.TokenService;
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
    private final GoogleService googleService;
    private final UniversityService universityService;
    private final TokenService tokenService;
    private final UserService userService;
    private final UserPreCertificationRepository userPreCertificationRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final FavoriteRepository favoriteRepository;
    private final CareerRepository careerRepository;
    private final ActivityRepository activityRepository;
    private final CertificationCommentRepository certificationCommentRepository;
    private final CertificationCommentLikeRepository certificationCommentLikeRepository;
    private final AppleOAuthService appleOAuthService;

    public AuthResponse login(OAuthUserInformation userInfo) {
        return userRepository.findBySocialTypeAndSocialId(userInfo.socialType(), userInfo.socialId())
                .map(this::handleExistingUser)
                .orElseGet(() -> handleNewUser(userInfo));
    }

    private AuthResponse handleNewUser(OAuthUserInformation userInfo) {
        String preSignupToken = jwtService.generatePreSignupToken(userInfo.socialId());
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
        tokenService.saveRefreshToken(newUser.getId(), token.refreshToken());

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
            case GOOGLE -> googleService;
            case APPLE -> appleOAuthService;
        };
    }

    private User convertDtoToEntity(SignupRequest request) {
        MajorImpl majorImpl = majorImplRepository.findMajorImplByName(request.major())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MAJOR_NOT_FOUND));


        return User.builder()
                .email(request.userInformation().email())
                .name(request.userInformation().name())
                .nickname(request.nickname())
                .profileImageUrl(request.userInformation().profileImageUrl())
                .track(request.track())
                .grade(request.grade())
                .major(majorImpl)
                .university(universityService.getUniversityByName(request.university()))
                .socialType(request.userInformation().socialType())
                .socialId(request.userInformation().socialId())
                .marketingAgree(false)
                .build();
    }

    @Transactional
    public void withdraw(final Long userId){
        User user = userService.getUser(userId);

        //좋아요 삭제
        certificationCommentLikeRepository.deleteAllByUser(user);

        //댓글 삭제
        certificationCommentRepository.deleteAllByUser(user);

        //희망 직무 삭제
        userJobRepository.deleteAllByUser(user);

        //사용자 전공 삭제
        userMajorImplRepository.deleteAllByUser(user);

        //사용자 취득 자격증 삭제
        userPreCertificationRepository.deleteAllByUser(user);

        //사용자 취득 예정 자격증 삭제
        acquisitionRepository.deleteAllByUser(user);

        //즐겨찾기 항목 삭제
        favoriteRepository.deleteAllByUser(user);

        //경력 사항 삭제
        careerRepository.deleteAllByUser(user);

        //대내외 활동 삭제
        activityRepository.deleteALlByUser(user);

        //토큰 삭제
        tokenService.deleteRefreshToken(userId);

        //사용자 탈퇴
        userRepository.delete(user);

        log.info("정상적으로 탈퇴되었습니다");
    }

}
