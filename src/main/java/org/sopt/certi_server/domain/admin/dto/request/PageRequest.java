package org.sopt.certi_server.domain.admin.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Pageable;

public record PageRequest(
        @PositiveOrZero(message = "페이지 번호는 0 또는 음수일 수 없습니다.") int page,
        @PositiveOrZero(message = "페이지 사이즈는 0 또는 음수일 수 없습니다.") int size

) {

    public Pageable toPageable(){

        return org.springframework.data.domain.PageRequest.of(this.page, this.size);
    }
}
