package org.sopt.certi_server.domain.major.service;

import java.util.List;

import org.sopt.certi_server.domain.major.dto.response.GetMajorListResponse;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MajorService {
	private final MajorImplRepository majorImplRepository;

	public GetMajorListResponse getMajorList(final String keyword) {
		List<MajorImpl> majorList = majorImplRepository.findByNameContainingIgnoreCase(keyword);
		List<String> majorNameList = majorList.stream()
			.map(MajorImpl::getName)
			.toList();
		return GetMajorListResponse.of(majorNameList);
	}
}
