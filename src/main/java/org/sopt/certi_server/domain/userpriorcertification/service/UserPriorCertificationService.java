package org.sopt.certi_server.domain.userpriorcertification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.sopt.certi_server.domain.certification.entity.Category;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationCategory;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.domain.userpriorcertification.dto.response.GetPriorCertificaitonResponse;
import org.sopt.certi_server.domain.userpriorcertification.dto.response.GetPriorCertificationDetailResponse;
import org.sopt.certi_server.domain.userpriorcertification.entity.UserPriorCertification;
import org.sopt.certi_server.domain.userpriorcertification.repository.UserPriorCertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPriorCertificationService {
	private final UserPriorCertificationRepository userPriorCertificationRepository;
	private final UserService userService;
	private final CertificationService certificationService;
	private final CertificationRepository certificationRepository;

	@Transactional
	public String createPriorCertification(final Long userId, final Long certificationId){
		Certification certification = certificationService.getCertification(certificationId);
		User user = userService.getUser(userId);

		UserPriorCertification userPriorCertification = UserPriorCertification.builder()
			.user(user)
			.certification(certification)
			.build();

		userPriorCertificationRepository.save(userPriorCertification);

		return userPriorCertification.getCertification().getName();
	}

	public UserPriorCertification getPriorCertification(final Long userId, final Long priorCertificationId){
		User user = userService.getUser(userId);
		UserPriorCertification userPriorCertification = userPriorCertificationRepository.findByUserAndPriorCertificationId(user, priorCertificationId)
			.orElseThrow(()-> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

		return userPriorCertification;
	}

	public GetPriorCertificationDetailResponse getPriorCertificationDetail(final Long userId, final Long priorCertificationId){
		User user = userService.getUser(userId);
		UserPriorCertification userPriorCertification = getPriorCertification(userId, priorCertificationId);

		List<Category> categories = certificationRepository.getCategoriesByCertificationId(priorCertificationId);
		List<String> categoriesNames = categories.stream().map(Category::getName).toList();

		return GetPriorCertificationDetailResponse.from(userPriorCertification.getCertification(), categoriesNames);
	}

	public List<GetPriorCertificaitonResponse> getPriorCertificaitonList(final Long userId) {
		User user = userService.getUser(userId);
		List<UserPriorCertification> userPriorCertificationList = userPriorCertificationRepository.findByUserOrderByPriorCertificationIdAsc(user);
		List<GetPriorCertificaitonResponse> responses = userPriorCertificationList.stream()
			.map(UserPriorCertification::getCertification)
			.map(GetPriorCertificaitonResponse::from)
			.toList();

		return responses;

	}

	@Transactional
	public void deletePriorCertification(final Long userId, final Long priorCertificationId){
		User user = userService.getUser(userId);
		UserPriorCertification userPriorCertification = getPriorCertification(userId, priorCertificationId);

		userPriorCertificationRepository.delete(userPriorCertification);
	}

}
