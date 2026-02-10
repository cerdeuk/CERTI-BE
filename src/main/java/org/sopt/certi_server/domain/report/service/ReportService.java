package org.sopt.certi_server.domain.report.service;

import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.comment.service.CertificationCommentService;
import org.sopt.certi_server.domain.report.dto.CommentReportRequest;
import org.sopt.certi_server.domain.report.entity.CommentReport;
import org.sopt.certi_server.domain.report.repository.ReportRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.discord.dto.DiscordWebhookEvent;
import org.sopt.certi_server.global.discord.dto.MessageFormatter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
	private final ReportRepository reportRepository;
	private final CertificationCommentService certificationCommentService;
	private final UserService userService;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public void createCommentReport(final CommentReportRequest commentReportRequest, final Long userId, final Long commentId) {
		User user = userService.getUser(userId);
		CertificationComment comment = certificationCommentService.getComment(commentId);
		CommentReport report = CommentReport.createCommentReport(comment, user, commentReportRequest.content());
		reportRepository.save(report);

		String message = MessageFormatter.formatCommentReport(report, commentReportRequest.shouldBlockUser());


		eventPublisher.publishEvent(new DiscordWebhookEvent(message));
	}
}
