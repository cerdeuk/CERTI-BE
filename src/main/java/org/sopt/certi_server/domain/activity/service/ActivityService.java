package org.sopt.certi_server.domain.activity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.sopt.certi_server.domain.activity.dto.request.CreateActivityRequest;
import org.sopt.certi_server.domain.activity.dto.response.ActivityDetailResponse;
import org.sopt.certi_server.domain.activity.entity.Activity;
import org.sopt.certi_server.domain.activity.repository.ActivityRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.ForbiddenException;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {
	private final ActivityRepository activityRepository;
	private final UserService userService;

	@Transactional
	public void createActivity(Long userId, CreateActivityRequest request) {
		User user = userService.getUser(userId);

		Activity activity = Activity.builder()
			.place(request.place())
			.startAt(request.startAt())
			.endAt(request.endAt())
			.name(request.name())
			.user(user)
			.description(request.description())
			.place(request.place())
			.build();

		activityRepository.save(activity);
	}

	public List<ActivityDetailResponse> getActivityList(Long userId){
		User user = userService.getUser(userId);

		List<Activity> activityList = activityRepository.findByUser(user);

		List<ActivityDetailResponse> activityDetailResponses = activityList.stream()
			.map(ActivityDetailResponse::from)
			.toList();

		return activityDetailResponses;
	}

	@Transactional
	public void deleteActivity(Long activityId, Long userId){
		User user = userService.getUser(userId);
		Activity activity = activityRepository.findById(activityId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

		//추가적인 NPE 방지
		if(!Objects.equals(activity.getUser().getId(), userId)){
			throw new ForbiddenException(ErrorCode.ACCESS_DENIED);
		}

		activityRepository.deleteById(activityId);
	}
}
