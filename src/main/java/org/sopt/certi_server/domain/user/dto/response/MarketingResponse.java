package org.sopt.certi_server.domain.user.dto.response;

public record MarketingResponse(
        boolean isAdAgreed
) {
    public static MarketingResponse of(boolean isAdAgreed){
        return new MarketingResponse(isAdAgreed);
    };
}
