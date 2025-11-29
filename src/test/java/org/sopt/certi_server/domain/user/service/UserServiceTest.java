package org.sopt.certi_server.domain.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
<<<<<<< HEAD
import org.junit.jupiter.api.Nested;
=======
>>>>>>> 798bacb (#170 [feat] 마이페이지 홈화면 API 구현)
import org.junit.jupiter.api.Test;
import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.acquisition.entity.enums.CardType;
import org.sopt.certi_server.domain.acquisition.entity.enums.SmallCardType;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentLikeRepository;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentRepository;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.user.dto.response.GetMyPageInfoResponse;
import org.sopt.certi_server.domain.user.dto.request.UpdateUserRequest;
import org.sopt.certi_server.domain.user.dto.response.GetMyPageInfoResponse;
import org.sopt.certi_server.domain.user.dto.response.PersonalInformationResponse;
import org.sopt.certi_server.domain.user.entity.University;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.sopt.certi_server.domain.user.repository.UniversityRepository;
import org.sopt.certi_server.domain.user.repository.UserJobRepository;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CertificationRepository certificationRepository;
    @Autowired
    private CertificationCommentRepository certificationCommentRepository;
    @Autowired
    private CertificationCommentLikeRepository certificationCommentLikeRepository;
    @Autowired
    private AcquisitionRepository acquisitionRepository;
    @Autowired
    private UserPreCertificationRepository userPreCertificationRepository;
    @Autowired
    private UserJobRepository userJobRepository;
    @Autowired
    private JobRepository jobRepository; // Job 저장을 위해 추가
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private MajorImplRepository majorImplRepository;

    private User testUser;

    private Job testJob;
    private Certification testCertification;
    private University testUniversity;
    private MajorImpl testMajor;
    @BeforeEach
    void setUp(){

        testUniversity = universityRepository.findById(1L).orElseThrow();
        testMajor = majorImplRepository.findMajorImplByName("전산학/컴퓨터공학").orElseThrow();
        testUser = userRepository.save(
                User.builder()
                        .email("lee@gmail.com")
                        .nickname("이성민")
                        .track("공학계열")
                        .grade("1학년")
                        .major(testMajor)
                        .university(testUniversity)
                        .build()
        );
        testCertification = certificationRepository.findById(1L).orElseThrow();
        acquisitionRepository.save(
                Acquisition.builder()
                        .user(testUser)
                        .certification(testCertification)
                        .cardType(CardType.THIRD)
                        .smallCardType(SmallCardType.THIRD)
                        .build()
        );
        testJob = jobRepository.findByName("IT/인터넷").orElseThrow();
        userJobRepository.save(UserJob.createUserJob(testUser, testJob));
    }

    @Test
    @DisplayName("[성공] 마이페이지 홈 내용을 유저의 현 상황에 맞게 조회를 성공한다.")
    void get_my_page_home(){
        GetMyPageInfoResponse myPageInfoResponse = userService.getMyPageInfoResponse(testUser.getId());

        Assertions.assertThat(myPageInfoResponse.name()).isEqualTo("이성민");
        Assertions.assertThat(myPageInfoResponse.email()).isEqualTo("lee@gmail.com");
        Assertions.assertThat(myPageInfoResponse.jobResponse().jobList()).contains("IT/인터넷");
        Assertions.assertThat(myPageInfoResponse.acCount()).isEqualTo(1);
        Assertions.assertThat(myPageInfoResponse.upCount()).isEqualTo(0);
        Assertions.assertThat(myPageInfoResponse.fCount()).isEqualTo(0);
    }


    @Nested
    @DisplayName("개인 정보 수정")
    class UpdateUserInformation{

        @Test
        @DisplayName("[성공] 개인정보 수정을 성공한다")
        void update_user_information(){
            // Given
            UpdateUserRequest request = new UpdateUserRequest(
                    "이성민",
                    "leesung2925@gmail.com",
                    "이뿡빵",
                    LocalDate.of(2000, 2, 29)
            );

            // When
            userService.updateUserInformation(testUser.getId(), request);

            // Then
            PersonalInformationResponse personalInformationResponse = userService.getPersonalInformationResponse(testUser.getId());

            Assertions.assertThat(personalInformationResponse.name()).isEqualTo("이성민");
            Assertions.assertThat(personalInformationResponse.email()).isEqualTo("leesung2925@gmail.com");
            Assertions.assertThat(personalInformationResponse.birthDate()).isEqualTo(LocalDate.of(2000, 2, 29));
            Assertions.assertThat(personalInformationResponse.nickName()).isEqualTo("이뿡빵");
        }


    }

    @Nested
    @DisplayName("닉네임 검사")
    class ValidateNickname{
        @Test
        @DisplayName("[성공] 닉네임에 욕설이 포함되어 있다.")
        void nickname_contains_profanity(){

            // Given
            String nickname = "시발이성민";

            // When
            NicknameValidationResponse response = userService.validateNickname(nickname);

            // Then
            Assertions.assertThat(response.isAvailable()).isFalse();
            Assertions.assertThat(response.reason()).isEqualTo(NicknameValidationType.PROFANITY.getMessage());
        }

        @Test
        @DisplayName("[성공] 닉네임이 공백으로 이루어져있다.")
        void nickname_only_blank(){

            // Given
            String nickname = "   ";

            // When
            NicknameValidationResponse response = userService.validateNickname(nickname);

            // Then
            Assertions.assertThat(response.isAvailable()).isFalse();
            Assertions.assertThat(response.reason()).isEqualTo(NicknameValidationType.EMPTY.getMessage());
        }

        @Test
        @DisplayName("[성공] 닉네임의 길이가 너무 길다")
        void nickname_too_long(){

            // Given
            String nickname = "안녕하세요저는이성민입니다";

            // When
            NicknameValidationResponse response = userService.validateNickname(nickname);

            // Then
            Assertions.assertThat(response.isAvailable()).isFalse();
            Assertions.assertThat(response.reason()).isEqualTo(NicknameValidationType.TOO_LONG.getMessage());
        }

        @Test
        @DisplayName("[성공] 중복되는 닉네임이 존재한다")
        void nickname_is_duplicated(){

            // Given
            String nickname = "이성민";

            // When
            NicknameValidationResponse response = userService.validateNickname(nickname);

            // Then
            Assertions.assertThat(response.isAvailable()).isFalse();
            Assertions.assertThat(response.reason()).isEqualTo(NicknameValidationType.DUPLICATE.getMessage());
        }
    }


}
