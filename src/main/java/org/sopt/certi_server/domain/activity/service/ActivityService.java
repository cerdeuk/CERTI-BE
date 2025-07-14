package org.sopt.certi_server.domain.activity.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.activity.dto.request.CreateActivityRequest;
import org.sopt.certi_server.domain.activity.dto.response.ActivityDetailResponse;
import org.sopt.certi_server.domain.activity.repository.ActivityRepository;
import org.sopt.certi_server.domain.user.entity.Activity;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.ForbiddenException;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserService userService;

    @Transactional
    public void createActivity(final Long userId, CreateActivityRequest request) {
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

    public List<ActivityDetailResponse> getActivityList(final Long userId) {
        User user = userService.getUser(userId);

        List<Activity> activityList = activityRepository.findByUser(user);

        return activityList.stream()
                .map(ActivityDetailResponse::from)
                .toList();
    }

    @Transactional
    public void deleteActivity(final Long activityId, final Long userId) {
        User user = userService.getUser(userId);
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

        // 해당 activity가 user의 소유인지 확인
        if (!Objects.equals(activity.getUser().getId(), userId)) {
            throw new ForbiddenException(ErrorCode.ACCESS_DENIED);
        }

        activityRepository.deleteById(activityId);
    }
}
