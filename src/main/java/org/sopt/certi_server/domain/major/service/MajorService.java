package org.sopt.certi_server.domain.major.service;

import java.util.List;

import org.sopt.certi_server.domain.major.dto.response.GetMajorListResponse;
import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.major.repository.MajorRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MajorService {
	private final MajorImplRepository majorImplRepository;
	private final MajorRepository majorRepository;

	public GetMajorListResponse getMajorList(final String keyword) {
		List<MajorImpl> majorList = majorImplRepository.findByNameContainingIgnoreCase(keyword);
		List<String> majorNameList = majorList.stream()
			.map(MajorImpl::getName)
			.toList();
		return GetMajorListResponse.of(majorNameList);
	}

	public Major getMajorByName(final String majorName) {
		Major major = majorRepository.findByName(majorName)
			.orElseThrow(()-> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

		return major;
	}
}
