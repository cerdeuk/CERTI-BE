package org.sopt.certi_server.domain.major.dto.response;

import java.util.List;

public record GetMajorListResponse(
	List<String> majorNameList
) {
	public static GetMajorListResponse of(List<String> majorNameList) {
		return new GetMajorListResponse(majorNameList);
	}
}
