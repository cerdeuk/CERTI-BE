package org.sopt.certi_server.domain.user.dto.response;

public record JwtResponse(String accessToken, String refreshToken) {

    public static JwtResponse of(String accessToken, String refreshToken){
        return new JwtResponse(accessToken, refreshToken);
    }
}
