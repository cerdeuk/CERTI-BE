package org.sopt.certi_server.domain.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.springframework.web.bind.annotation.RequestParam;

public record LoginUriRequest(@NotEmpty(message = "socialType을 반드시 지정해주어야 합니다.")  String socialType) {

}
