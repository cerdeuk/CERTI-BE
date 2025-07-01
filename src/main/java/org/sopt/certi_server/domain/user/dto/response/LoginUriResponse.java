package org.sopt.certi_server.domain.user.dto.response;

public record LoginUriResponse(String uri) {

    public static LoginUriResponse of(String uri){
        return new LoginUriResponse(uri);
    }
}
