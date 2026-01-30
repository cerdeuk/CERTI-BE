package org.sopt.certi_server.domain.comment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "certification_comment")
@Getter
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE certification_comment SET deleted_at = NOW() WHERE certification_comment_id = ?")
public class CertificationComment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private CertificationComment(User user, Certification certification, String content){
        this.user = user;
        this.certification = certification;
        this.content = content;
    }

}
