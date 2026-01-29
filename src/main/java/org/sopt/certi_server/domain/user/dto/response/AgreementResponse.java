package org.sopt.certi_server.domain.user.dto.response;

public record AgreementResponse(
        boolean isAdAgreed,
        boolean isPvAgreed
) {
    public static AgreementResponse of(boolean isAdAgreed, boolean isPvAgreed){
        return new AgreementResponse(isAdAgreed, isPvAgreed);
    };
}
