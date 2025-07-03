package org.sopt.certi_server.domain.certification.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.category.entity.Category;
import org.sopt.certi_server.domain.certification.dto.response.CertificationDetailResponse;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationService {

    private final CertificationRepository certificationRepository;

    public CertificationDetailResponse getCertificationDetail(Long certificationId){
        Certification certification = certificationRepository.findById(certificationId).orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
        List<Category> categories = certificationRepository.getCategoriesByCertificationId(certificationId);
        return CertificationDetailResponse.from(certification, categories);
    }

}
