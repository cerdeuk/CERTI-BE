package org.sopt.certi_server.domain.userprecertification.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.domain.userprecertification.dto.request.CreateUserPreCertificationRequest;
import org.sopt.certi_server.domain.userprecertification.dto.request.PatchPreCertificationRequest;
import org.sopt.certi_server.domain.userprecertification.dto.response.DayDotRes;
import org.sopt.certi_server.domain.userprecertification.dto.response.DayScheduleRes;
import org.sopt.certi_server.domain.userprecertification.dto.response.MonthCalendarRes;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimple;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimpleListResponse;
import org.sopt.certi_server.domain.userprecertification.dto.response.ScheduleICertificationRes;
import org.sopt.certi_server.domain.userprecertification.entity.Location;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.sopt.certi_server.domain.userprecertification.entity.enums.IconType;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.BusinessException;
import org.sopt.certi_server.global.error.exception.ForbiddenException;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserPreCertificationService {

    private final UserService userService;
    private final CertificationService certificationService;
    private final UserPreCertificationRepository userPreCertificationRepository;
    private final AcquisitionRepository acquisitionRepository;

    public PreCertificationSimpleListResponse getPreCertificationListDataByUserId(Long userId) {
        return new PreCertificationSimpleListResponse(userPreCertificationRepository.findPreCertificationsByUserIdOrderByNearestTestDate(userId).stream()
            .map(PreCertificationSimple::from)
            .toList());
    }

    @Transactional
    public boolean createNewPreCertification(final Long userId, final CreateUserPreCertificationRequest request) {
        User user = userService.getUser(userId);
        Certification certification = certificationService.getCertification(request.certificationId());

        if(acquisitionRepository.existsByUserAndCertification(user, certification)){
            log.info("이미 취득한 자격증에 대해 취득 예정 시도");
            throw new BusinessException(ErrorCode.DUPLICATED_ACQUISITION);
        }

        if (userPreCertificationRepository.existsByUserAndCertification(user, certification)) {
            return false;
        }

        IconType iconType = userPreCertificationRepository.findFirstByUserOrderByCreatedTimeDesc(user)
                .map(userPreCertification -> IconType.issueNextIconType(userPreCertification.getIconType().getIndex()))
                .orElseGet(IconType::issueRandomIconType);

        userPreCertificationRepository.save(UserPreCertification.create(
                user,
                certification,
                iconType,
                Location.builder()
                        .city(request.city())
                        .state(request.state())
                        .build(),
                request.testDate()
                ));

        return true;
    }

    @Transactional
    public void deletePreCertification(Long userId, Long certificationId) {
        User user = userService.getUser(userId);
        Certification certification = certificationService.getCertification(certificationId);

        UserPreCertification userPreCertification = userPreCertificationRepository.findByUserAndCertification(user, certification)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRECERTIFICATION_NOT_FOUND));

        userPreCertificationRepository.delete(userPreCertification);
    }

    @Transactional
    public void patchPreCertification(Long userId, Long userPreCertificationId, PatchPreCertificationRequest request) {

        User user = userService.getUser(userId);
        UserPreCertification upc = userPreCertificationRepository.findById(userPreCertificationId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRECERTIFICATION_NOT_FOUND)
        );

        if(upc.getUser() != user) throw new ForbiddenException(ErrorCode.ACCESS_DENIED);

        upc.changeUserPreCertification(
                Location.builder()
                        .city(request.city())
                        .state(request.state())
                        .build(),
                request.testDate()
        );
    }

    public MonthCalendarRes getMonthCalendar(Long userId, int year, int month) {
        LocalDate first = LocalDate.of(year, month, 1);
        LocalDate last = first.withDayOfMonth(first.lengthOfMonth());

        LocalDateTime start = first.atStartOfDay();
        LocalDateTime end = last.atTime(23, 59, 59);

        List<DayDotRes> days = userPreCertificationRepository.findMonthDots(userId, start, end).stream()
            .map(p -> new DayDotRes(p.getDay(), p.getCount()))
            .toList();

        List<DayDotRes> dayDotResList = days.isEmpty()
            ? null
            : days.stream().map(d -> new  DayDotRes(d.day(), d.count())).toList();

        return new MonthCalendarRes(year, month, days);
    }

    public DayScheduleRes getDaySchedules(Long userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<UserPreCertification> list =
            userPreCertificationRepository.findDayItems(userId, start, end);

        List<ScheduleICertificationRes> items = list.isEmpty()
            ? null
            : list.stream().map(this::toScheduleItem).toList();

        return new DayScheduleRes(date, items);
    }

    private ScheduleICertificationRes toScheduleItem(UserPreCertification upc) {

        String certificationName = upc.getCertification().getName();
        String certificationType = upc.getCertification().getCertificationType() == null
            ? null
            : upc.getCertification().getCertificationType().getKoreanName();

        String description = upc.getCertification().getDescription();

        String location = upc.getLocation() == null ? null : upc.getLocation().toString(); // Location 설계에 맞게 수정
        String time = upc.getTestDate() == null ? null : upc.getTestDate().toString();

        return new ScheduleICertificationRes(
            upc.getId(),
            certificationName,
            certificationType,
            description,
            location,
            time
        );
    }


}
