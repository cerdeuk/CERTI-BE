package org.sopt.certi_server.domain.user.dto.response;

import lombok.Builder;
import org.sopt.certi_server.domain.user.dto.type.NicknameValidationType;

@Builder
public record NicknameValidationResponse(
        boolean isAvailable,
        String reason
) {
    public static NicknameValidationResponse from(NicknameValidationType type){
        return NicknameValidationResponse.builder()
                .isAvailable(type.isSuccess())
                .reason(type.getMessage())
                .build();
    }

}
