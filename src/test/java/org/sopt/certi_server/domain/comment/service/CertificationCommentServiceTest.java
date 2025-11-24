package org.sopt.certi_server.domain.comment.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.comment.dto.request.CommentRegisterRequest;
import org.sopt.certi_server.domain.comment.dto.response.CertificationCommentResponse;
import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.comment.entity.CertificationCommentLike;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentLikeRepository;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentRepository;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.major.repository.MajorRepository;
import org.sopt.certi_server.domain.user.entity.University;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.sopt.certi_server.domain.user.entity.enums.Grade;
import org.sopt.certi_server.domain.user.entity.enums.TrackType;
import org.sopt.certi_server.domain.user.repository.UniversityRepository;
import org.sopt.certi_server.domain.user.repository.UserJobRepository;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.sopt.certi_server.global.error.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Slf4j
class CertificationCommentServiceTest {

    // 실제 Bean 주입
    @Autowired
    private CertificationCommentService certificationCommentService;

    // @Mock 대신 @Autowired로 실제 Repository Bean 주입 (데이터 준비 및 검증용)
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

    // 공통 테스트 데이터 (엔티티)
    private User testUser;
    private User otherUser;
    private Certification testCertification;
    private Job testJob;
    private CertificationComment testComment;
    private University testUniversity;
    private MajorImpl testMajor;

    @BeforeEach
    void setUp() {

        testUniversity = universityRepository.findById(1L).orElseThrow();
        testMajor = majorImplRepository.findMajorImplByName("전산학/컴퓨터공학").orElseThrow();
        // Mocking 대신 실제 DB에 데이터 저장
        testUser = userRepository.save(User.builder()
                .email("lee@gmail.com")
                .nickname("테스트 유저")
                .track("공학계열")
                .grade("1학년")
                .major(testMajor)
                .university(testUniversity)
                .build()
        );
        otherUser = userRepository.save(User.builder()
                .email("hong@gmail.com")
                .nickname("타인")
                .track("공학계열")
                .grade("1학년")
                .major(testMajor)
                .university(testUniversity)
                .build()
        );

        log.info(TrackType.EDUCATION.name());
        log.info(Grade.FRESHMAN.name());

        testCertification = certificationRepository.findById(1L).orElseThrow();
        testJob = jobRepository.findByName("IT/인터넷").orElseThrow();

        // @BeforeEach에서 생성된 댓글 (기본 댓글)
        testComment = certificationCommentRepository.save(
                CertificationComment.builder()
                        .user(testUser)
                        .certification(testCertification)
                        .content("테스트 댓글")
                        .build()
        );
    }

    @Nested
    @DisplayName("댓글 등록 (registerComment)")
    class RegisterComment {

        @Test
        @DisplayName("[성공] 댓글 등록에 성공한다.")
        void registerComment_Success() {
            // Given
            Long userId = testUser.getId();
            Long certificationId = testCertification.getId();


            // 사용자는 해당 자격증에 취득을 한 상태
            acquisitionRepository.save(Acquisition.builder()
                    .user(testUser)
                    .certification(testCertification)
                    .build());

            CommentRegisterRequest request = new CommentRegisterRequest("새 댓글 내용", certificationId);

            // When
            certificationCommentService.registerComment(userId, request);

            // Then
            // verify() 대신, DB를 직접 조회하여 검증
            List<CertificationComment> allComments = certificationCommentRepository.findAll();
            assertThat(allComments).hasSize(2); // @BeforeEach 1개 + test 1개

            CertificationComment savedComment = allComments.get(1); // 2번째 댓글
            assertThat(savedComment.getUser().getId()).isEqualTo(userId);
            assertThat(savedComment.getCertification().getId()).isEqualTo(certificationId);
            assertThat(savedComment.getContent()).isEqualTo("새 댓글 내용");
        }

        @Test
        @DisplayName("[실패] 댓글 등록 시 사용자를 찾을 수 없다.")
        void registerComment_Fail_UserNotFound() {
            // Given
            Long invalidUserId = 9999L; // DB에 없는 유저 ID
            CommentRegisterRequest request = new CommentRegisterRequest("새 댓글 내용", testCertification.getId());

            // When & Then
            // 서비스 로직 내 userService.getUser() (내부적으로 userRepository.findById)가 예외 발생
            assertThrows(NotFoundException.class, () -> {
                certificationCommentService.registerComment(invalidUserId, request);
            });

            // DB에 댓글이 추가되지 않았는지 검증
            assertThat(certificationCommentRepository.count()).isEqualTo(1); // @BeforeEach에서 만든 1개
        }
    }

    @Nested
    @DisplayName("댓글 삭제 (deleteComment)")
    class DeleteComment {

        @Test
        @DisplayName("[성공] 자신의 댓글을 삭제한다.")
        void deleteComment_Success() {
            // Given
            Long commentId = testComment.getId();
            Long userId = testUser.getId(); // 댓글 작성자

            // 삭제될 댓글의 좋아요도 미리 생성 (연관관계 삭제 테스트)
            certificationCommentLikeRepository.save(
                    CertificationCommentLike.builder()
                            .user(testUser)
                            .certificationComment(testComment)
                            .build()
            );

            // When
            certificationCommentService.deleteComment(commentId, userId);

            // Then
            // 1. 댓글이 DB에서 삭제되었는지 검증
            assertThat(certificationCommentRepository.findById(commentId)).isEmpty();
            // 2. 연관된 '좋아요'도 DB에서 삭제되었는지 검증
            assertThat(certificationCommentLikeRepository.count()).isZero();
        }

        @Test
        @DisplayName("[실패] 타인의 댓글을 삭제하려 하면 UnauthorizedException이 발생한다.")
        void deleteComment_Fail_Unauthorized() {
            // Given
            Long commentId = testComment.getId();
            Long otherUserId = otherUser.getId(); // 댓글 작성자가 아닌 ID

            // When & Then
            assertThrows(UnauthorizedException.class, () -> {
                certificationCommentService.deleteComment(commentId, otherUserId);
            });

            // DB에서 댓글이 삭제되지 않았는지 검증
            assertThat(certificationCommentRepository.findById(commentId)).isPresent();
        }
    }

    @Nested
    @DisplayName("좋아요 토글 (toggleCommentLike)")
    class ToggleCommentLike {

        @Test
        @DisplayName("[성공] '좋아요'를 누른다 (신규).")
        void toggleLike_Success_DoLike() {
            // Given
            Long userId = testUser.getId();
            Long commentId = testComment.getId();

            // When
            certificationCommentService.toggleCommentLike(userId, commentId);

            // Then
            // 1. '좋아요'가 DB에 저장되었는지 검증
            assertThat(certificationCommentLikeRepository.count()).isEqualTo(1);
            // 2. '좋아요 카운트'가 원자적으로 증가했는지 DB에서 직접 조회
            CertificationComment updatedComment = certificationCommentRepository.findById(commentId).get();
            assertThat(updatedComment.getLikeCount()).isEqualTo(1L); // 초기 0L -> 1L
        }

        @Test
        @DisplayName("[성공] '좋아요'를 취소한다 (기존).")
        void toggleLike_Success_DoUnlike() {
            // Given
            Long userId = testUser.getId();
            Long commentId = testComment.getId();

            // 좋아요 미리 눌러두기
            certificationCommentRepository.save(testComment);
            certificationCommentService.toggleCommentLike(testUser.getId(), testComment.getId());

            // When
            certificationCommentService.toggleCommentLike(userId, commentId);

            // Then
            // 1. '좋아요'가 DB에서 삭제되었는지 검증
            assertThat(certificationCommentLikeRepository.count()).isZero();
            // 2. '좋아요 카운트'가 원자적으로 감소했는지 DB에서 직접 조회
            CertificationComment updatedComment = certificationCommentRepository.findById(commentId).get();
            assertThat(updatedComment.getLikeCount()).isEqualTo(0L); // 1L -> 0L
        }
    }

    @Nested
    @DisplayName("댓글 페이징 조회 (getCommentsByCertification)")
    class GetComments {

        @Test
        @DisplayName("[성공] N+1 없이 직무, 취득 상태를 모두 포함하여 조회한다.")
        void getComments_Success_WithAllData() {
            // Given
            Long userId = testUser.getId();
            Long certificationId = testCertification.getId();
            Pageable pageable = PageRequest.of(0, 10);

            // 1. 직무 정보 DB에 저장
            userJobRepository.save(UserJob.builder().user(testUser).job(testJob).build());

            // 2. 취득 예정 정보 DB에 저장
            // (userPreCertificationRepository.save(...)); // UserPreCertification 엔티티 필요

            // 3. 취득 완료 정보 DB에 저장
            acquisitionRepository.save(Acquisition.builder().user(testUser).certification(testCertification).build());

            // 4. 좋아요 마킹
            certificationCommentLikeRepository.save(CertificationCommentLike.builder()
                    .user(testUser)
                    .certificationComment(testComment)
                    .build());

            // When
            Page<CertificationCommentResponse> responsePage = certificationCommentService.getCommentsByCertification(userId, certificationId, pageable);

            // Then
            // 1. DTO가 올바르게 조립되었는지 검증
            assertThat(responsePage.getContent()).hasSize(1);
            CertificationCommentResponse responseDto = responsePage.getContent().get(0);

            // 2. Map<Long, List<String>> userJobMap 검증
            assertThat(responseDto.userJob()).isEqualTo("IT/인터넷");

            // 3. Map<Long, String> userStateMap 검증
            // (PreCert를 저장 안 했으므로 "취득 완료"만 나옴)
            assertThat(responseDto.state()).isEqualTo("취득 완료");

            // 4. 기본 정보 검증
            assertThat(responseDto.nickName()).isEqualTo(testUser.getNickname());
            assertThat(responseDto.content()).isEqualTo(testComment.getContent());

            // 5. 좋아요 마킹 되어있는지 검증
            assertThat(responseDto.isLike()).isTrue();
        }
    }
}
