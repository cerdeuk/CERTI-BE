package org.sopt.certi_server.domain.user.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty String code, @NotEmpty String socialType) {
}
