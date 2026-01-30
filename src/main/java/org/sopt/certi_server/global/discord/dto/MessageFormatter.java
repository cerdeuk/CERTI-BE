package org.sopt.certi_server.global.discord.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.report.entity.CommentReport;
import org.sopt.certi_server.domain.user.entity.User;

public class MessageFormatter {

	private static final String COMMENT_REPORT_MESSAGE =
		"```[%s] 댓글 %d 신고가 접수되었습니다.\n\n" +
			"[신고자]\n" +
			"유저 아이디 : %d\n" +
			"유저 닉네임 : %s\n\n" +
			"[신고된 댓글 작성자]\n" +
			"유저 아이디 : %d\n" +
			"유저 닉네임 : %s\n\n" +
			"[신고된 댓글 내용]\n%s\n```";

	public static String formatCommentReport(CommentReport report) {
		CertificationComment comment = report.getCertificationComment();
		User reporter = report.getUser();
		User commentWriter = comment.getUser();

		return String.format(
			COMMENT_REPORT_MESSAGE,
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			comment.getId(),
			reporter.getId(),
			reporter.getNickname(),
			commentWriter.getId(),
			commentWriter.getNickname(),
			comment.getContent()
		);
	}
}
