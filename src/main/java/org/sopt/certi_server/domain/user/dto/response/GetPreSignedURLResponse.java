package org.sopt.certi_server.domain.user.dto.response;

public record GetPreSignedURLResponse(
        String preSignedURL,
        String publicURL
) {
    public static GetPreSignedURLResponse of(
            String preSignedURL,
            String publicURL
    ){
        return new GetPreSignedURLResponse(
                preSignedURL,
                publicURL
        );
    }

}
