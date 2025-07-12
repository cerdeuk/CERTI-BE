package org.sopt.certi_server.domain.user.dto.response;

import java.util.List;

public record GetUniversityListResponse(
        List<String> universityNameList
) {
    public static GetUniversityListResponse of(List<String> universityNameList) {
        return new GetUniversityListResponse(universityNameList);
    }
}
