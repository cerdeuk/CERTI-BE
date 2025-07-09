package org.sopt.certi_server.domain.admin.dto.response;

import java.util.List;

public record AdminCertificationListResponse(List<AdminCertificationResponse> data) {

    public static AdminCertificationListResponse of(List<AdminCertificationResponse> data){
        return new AdminCertificationListResponse(data);
    }
}
