package org.sopt.certi_server.domain.user.dto.response;

import lombok.Builder;
import org.sopt.certi_server.domain.user.entity.User;

import java.time.LocalDate;

@Builder
public record PersonalInformationResponse(
        String nickName,
        String name,
        String email,
        LocalDate birthDate
) {

    public static PersonalInformationResponse from(User user){
        return PersonalInformationResponse.builder()
                .name(user.getName())
                .nickName(user.getNickname())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .build();
    }
}
