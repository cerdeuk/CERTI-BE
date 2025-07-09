package org.sopt.certi_server.domain.acquisition.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.acquisition.entity.enums.CardType;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionResponse;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionDetailResponse;
import org.sopt.certi_server.domain.certification.entity.Category;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.sopt.certi_server.domain.acquisition.entity.enums.CardType.*;
import static org.sopt.certi_server.domain.acquisition.entity.enums.CardType.CARD_TOTAL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AcquisitionService {
	private final AcquisitionRepository acquisitionRepository;
	private final UserService userService;
	private final CertificationService certificationService;


	@Transactional
	public String createAcquisition(final Long userId, final Long certificationId){
		log.info("userId : ", userId);
		Certification certification = certificationService.getCertification(certificationId);
		User user = userService.getUser(userId);

		CardType cardType = acquisitionRepository.findFirstByUserOrderByCreatedTimeDesc(user)
				.map(acquisition -> {
					int index = acquisition.getCardType().getIndex();
					return issueNextCardType(index);
				}).orElseGet(CardType::issueRandomCardType);


		Acquisition acquisition = Acquisition.builder()
			.user(user)
			.certification(certification)
			.cardType(cardType)
			.build();

		acquisitionRepository.save(acquisition);

		return acquisition.getCertification().getName();
	}

	public Acquisition getAcquisition(final Long userId, final Long acquisitionId){
		User user = userService.getUser(userId);
		Acquisition acquisition = acquisitionRepository.findByUserAndId(user, acquisitionId)
			.orElseThrow(()-> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

		return acquisition;
	}

	public GetAcquisitionDetailResponse getAcquisitionDetail(final Long userId, final Long certificationId){
		User user = userService.getUser(userId);
		Acquisition acquisition = getAcquisition(userId, certificationId);

		return GetAcquisitionDetailResponse.from(acquisition);
	}

	public List<GetAcquisitionResponse> getAcquisitionList(final Long userId) {
		User user = userService.getUser(userId);
		List<Acquisition> userPriorCertificationList = acquisitionRepository.findByUserOrderByIdAsc(user);
		List<GetAcquisitionResponse> responses = userPriorCertificationList.stream()
			.map(GetAcquisitionResponse::from)
			.toList();

		return responses;

	}

	@Transactional
	public void deleteAcquisition(final Long userId, final Long priorCertificationId){
		User user = userService.getUser(userId);
		Acquisition userPriorCertification = getAcquisition(userId, priorCertificationId);

		acquisitionRepository.delete(userPriorCertification);
	}

}
