package org.sopt.certi_server.domain.comment.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.comment.dto.response.CertificationCommentResponse;
import org.sopt.certi_server.domain.comment.dto.request.CommentRegisterRequest;
import org.sopt.certi_server.domain.comment.dto.response.CommentCreateResponse;
import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.comment.entity.CertificationCommentLike;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentLikeRepository;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserJob;
import org.sopt.certi_server.domain.user.repository.UserJobRepository;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.sopt.certi_server.global.error.exception.UnauthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CertificationCommentService {

    private final UserService userService;
    private final CertificationService certificationService;
    private final CertificationCommentRepository certificationCommentRepository;
    private final CertificationCommentLikeRepository certificationCommentLikeRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final UserPreCertificationRepository userPreCertificationRepository;
    private final UserJobRepository userJobRepository;
    private final EntityManager em;

    /**
     * 댓글 등록 메서드
     *
     * @param userId
     * @param request
     */
    @Transactional
    public CommentCreateResponse registerComment(
            final Long userId,
            final CommentRegisterRequest request
    ){
        User user = userService.getUser(userId);
        Certification certification = certificationService.getCertification(request.certificationId());

        boolean acquisitionExists = acquisitionRepository.existsByUserAndCertification(user, certification);
        boolean userPreExists = userPreCertificationRepository.existsByUserAndCertification(user, certification);


        // 취득 예정, 취득 상태가 아닌 사용자는 댓글을 달 수 없음
        if(!acquisitionExists && !userPreExists){
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }


        CertificationComment newCertificationComment = CertificationComment.builder()
                .user(user)
                .certification(certification)
                .content(request.content())
                .build();

        em.flush();
        certificationCommentRepository.save(newCertificationComment);

        return new CommentCreateResponse(newCertificationComment.getId());
    }


    /**
     * 자격증의 댓글을 조회하는 메서드
     *
     * @param certificationId
     * @param pageble
     * @return
     */
    public Page<CertificationCommentResponse> getCommentsByCertification(
            final Long userId,
            final Long certificationId,
            final Pageable pageble
    ){
        Page<CertificationComment> commentPage = certificationCommentRepository.findByCertificationId(certificationId, userId, pageble);

        List<User> users = commentPage.getContent().stream()
                .map(CertificationComment::getUser)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 사용자 희망 직무 정보 조회
        List<UserJob> userJobs = userJobRepository.findWithJobByUserIn(users);

        // 사용자 취득 예정 정보 조회
        List<UserPreCertification> userPreCertifications = userPreCertificationRepository.findByCertificationUserIn(certificationId, users);

        // 사용자 취득 완료 정보 조회
        List<Acquisition> acquisitions = acquisitionRepository.findByCertificationUserIn(certificationId, users);

        // 조회 API 호출자가 해당 댓글에 좋아요 눌렀는지 여부
        User caller = userService.getUser(userId);
        Certification certification = certificationService.getCertification(certificationId);

        List<Long> likeCommentIds = certificationCommentLikeRepository.findLikedCommentIdsByCertificationAndUser(caller, certification);

        // O(n) 조회를 위해 Set으로 변환
        Set<Long> likeCommentIdsSet = new HashSet<>(likeCommentIds);


        // DTO 조립을 위한 Map 생성 (O(1) 조회를 위함)

        // Job Map 생성 (Key: userId, Value: List<JobName>)
        Map<Long, List<String>> userJobMap = userJobs.stream()
                .collect(Collectors.groupingBy(
                        userJob -> userJob.getUser().getId(),
                        Collectors.mapping(
                                userJob -> userJob.getJob().getName(), // job 이름 (가정)
                                Collectors.toList()
                        )
                ));

        // State Map 생성 (Key: userId, Value: "취득 예정" or "취득 완료")
        Map<Long, String> userStateMap = new HashMap<>();
        userPreCertifications.forEach(upc ->
                userStateMap.put(upc.getUser().getId(), "취득 예정")
        );
        acquisitions.forEach(acq ->
                userStateMap.put(acq.getUser().getId(), "취득 완료")
        );

        // [5단계] DTO 최종 조립
        return commentPage.map(comment -> {
            User user = comment.getUser();
            boolean isLike = likeCommentIdsSet.contains(comment.getId());
            if (user == null) { // (알수없음) 탈퇴 사용자 처리
                return CertificationCommentResponse.from(comment, null, null, isLike);
            }

            // 4-1. Job Map에서 조회
            List<String> jobNames = userJobMap.getOrDefault(user.getId(), Collections.emptyList());

            // 4-2. State Map에서 조회
            String state = userStateMap.get(user.getId()); // 없으면 null

            return CertificationCommentResponse.from(comment, state, jobNames.get(0), isLike);
        });
    }


    /**
     * 댓글 삭제 메서드
     *
     * @param commentId
     * @param userId
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId){

        CertificationComment findComment = certificationCommentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND)
        );

        // 탈퇴 사용자 고려, 댓글 삭제 메서드에 대한 접근 권한 제어
        if (findComment.getUser() == null || !Objects.equals(findComment.getUser().getId(), userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 댓글 - 좋아요 먼저 삭제
        certificationCommentLikeRepository.deleteAllByCertificationComment(findComment);

        // 댓글 삭제
        certificationCommentRepository.delete(findComment);
    }

    @Transactional
    public void toggleCommentLike(
            final Long userId,
            final Long commentId
    ){

        User user = userService.getUser(userId);
        CertificationComment comment = certificationCommentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND)
        );

        boolean isExist = certificationCommentLikeRepository.existsByUserAndCertificationComment(user, comment);

        if(isExist){
            // 좋아요 취소
            CertificationCommentLike commentLike = certificationCommentLikeRepository.findByUserAndCertificationComment(user, comment).orElseThrow(
                    () -> new NotFoundException(ErrorCode.COMMENT_LIKE_NOT_FOUND)
            );
            certificationCommentLikeRepository.delete(commentLike);
            certificationCommentRepository.decrementLikeCount(commentId);
        }else{
            // 좋아요
            CertificationCommentLike newCommentLike = CertificationCommentLike.builder()
                    .user(user)
                    .certificationComment(comment)
                    .build();

            certificationCommentLikeRepository.save(newCommentLike);
            certificationCommentRepository.incrementLikeCount(commentId);
        }
    }

    public CertificationComment getComment(Long commentId){
        return certificationCommentRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
