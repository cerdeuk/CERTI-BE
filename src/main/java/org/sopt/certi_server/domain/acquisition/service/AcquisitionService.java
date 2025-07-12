package org.sopt.certi_server.domain.acquisition.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionDetailResponse;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionResponse;
import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.acquisition.entity.enums.CardType;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.ForbiddenException;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.sopt.certi_server.domain.acquisition.entity.enums.CardType.issueNextCardType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AcquisitionService {
    private final AcquisitionRepository acquisitionRepository;
    private final UserService userService;
    private final CertificationService certificationService;


    @Transactional
    public boolean createAcquisition(final Long userId, final Long certificationId) {
        Certification certification = certificationService.getCertification(certificationId);
        User user = userService.getUser(userId);

        //중복 여부 확인
        if (acquisitionRepository.existsByUserAndCertification(user, certification)) {
            return false;
        }

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

        return true;
    }

    public Acquisition getAcquisition(final Long acquisitionId) {
        return acquisitionRepository.findById(acquisitionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACQUISITION_NOT_FOUND));
    }

    public GetAcquisitionDetailResponse getAcquisitionDetail(final Long acquisitionId) {
        Acquisition acquisition = getAcquisition(acquisitionId);
        return GetAcquisitionDetailResponse.from(acquisition);
    }

    public List<GetAcquisitionResponse> getAcquisitionList(final Long userId) {
        User user = userService.getUser(userId);
        List<Acquisition> acquisitionList = acquisitionRepository.findByUserOrderByIdAsc(user);

        return acquisitionList.stream()
                .map(GetAcquisitionResponse::from)
                .toList();
    }

    @Transactional
    public void deleteAcquisition(final Long userId, final Long acquisitionId) {
        User findUser = userService.getUser(userId);
        Acquisition findAcquisition = getAcquisition(acquisitionId);

        if (!Objects.equals(findAcquisition.getUser().getId(), findUser.getId())) {
            throw new ForbiddenException(ErrorCode.ACCESS_DENIED);
        }

        acquisitionRepository.delete(findAcquisition);
    }
}
