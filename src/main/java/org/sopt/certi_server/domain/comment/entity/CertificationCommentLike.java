package org.sopt.certi_server.domain.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "certification_comment_like",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "certification_comment_id"})
        }
)
public class CertificationCommentLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_comment_id", nullable = false)
    private CertificationComment certificationComment;

    @Builder
    private CertificationCommentLike(User user, CertificationComment certificationComment) {
        this.user = user;
        this.certificationComment = certificationComment;
    }
}
