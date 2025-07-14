package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.response.GetUniversityListResponse;
import org.sopt.certi_server.domain.user.entity.University;
import org.sopt.certi_server.domain.user.repository.UniversityRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UniversityService {
    private final UniversityRepository universityRepository;

    public GetUniversityListResponse getUniversityList(final String keyword) {
        List<University> universityList = universityRepository.findAllByNameContaining(keyword);
        List<String> universityNameList = universityList.stream()
                .map(University::getName)
                .toList();
        return GetUniversityListResponse.of(universityNameList);
    }

    public University getUniversityByName(String name) {
        return universityRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorCode.UNIVERSITY_NOT_FOUND));
    }
}
