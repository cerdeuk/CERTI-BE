package org.sopt.certi_server.domain.user.service;

import java.util.List;

import org.sopt.certi_server.domain.user.dto.request.CreateCareerRequest;
import org.sopt.certi_server.domain.user.dto.response.CareerDetailResponse;
import org.sopt.certi_server.domain.user.entity.Career;
import org.sopt.certi_server.domain.user.repository.CareerRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CareerService {
	private final CareerRepository careerRepository;
	private final UserService userService;

	public List<CareerDetailResponse> getCareerList(final Long userId){
		//사용자 검증
		User user = userService.getUser(userId);

		//userId로 경력사항 조회
		List<Career> careers = careerRepository.findByUserId(userId);

		return careers.stream()
			.map(CareerDetailResponse::from)
			.toList();
	}

	@Transactional
	public void createCareer(final Long userId, final CreateCareerRequest request) {
		User user = userService.getUser(userId);

		Career career = Career.builder()
			.user(user)
			.startAt(request.startAt())
			.endAt(request.endAt())
			.place(request.place())
			.description(request.description())
			.name(request.name())
			.build();

		careerRepository.save(career);
	}

	@Transactional
	public void deleteCareer(final Long userId, final Long careerId) {
		User user = userService.getUser(userId);

		Career career = careerRepository.findByUserIdAndId(userId, careerId)
			.orElseThrow(()-> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

		log.info("Deleting career " + careerId);
		careerRepository.delete(career);
	}
}
