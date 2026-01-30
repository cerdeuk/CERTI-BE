package org.sopt.certi_server.domain.report.entity;

import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk_user_comment",
			columnNames = {"certification_comment_id", "user_id"}
		)
	}
)
@Getter
public class CommentReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = CertificationComment.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "certification_comment_id", nullable = false)
	private CertificationComment certificationComment;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public CommentReport(CertificationComment certificationComment, User user) {
		this.certificationComment = certificationComment;
		this.user = user;
	}

	public static CommentReport createCommentReport(final CertificationComment certificationComment, final User user) {
		return new CommentReport(certificationComment, user);
	}
}
