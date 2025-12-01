package org.sopt.certi_server.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import org.sopt.certi_server.global.annotation.NoProfanity;

import java.time.LocalDate;

public record UpdateUserRequest(
        @NoProfanity
        String name,
        @Email(message = "올바르지 않은 이메일 형식입니다.")
        String email,
        @NotEmpty(message = "닉네임은 공백이 될 수 없습니다.")
        @NoProfanity
        String nickName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        @Past(message = "생년월일은 과거 날짜여야 합니다.")
        LocalDate birthDate
) {

}
