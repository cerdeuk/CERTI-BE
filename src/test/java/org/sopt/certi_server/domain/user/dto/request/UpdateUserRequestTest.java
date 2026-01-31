package org.sopt.certi_server.domain.user.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UpdateUserRequestTest {

    @Autowired
    Validator validator;

    @Test
    @DisplayName("비속어가 포함된 단어를 검증한다.")
    void fail_profanity_nickname(){
        // Given
        UpdateUserRequest request = new UpdateUserRequest(
                "이성민",
                "leesung2925@gmail.com",
                "시발놈",
                LocalDate.of(2000,2,29),
                "https://community.linkareer.com/written_test/2629945"
        );

        // When
        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations)
                .extracting("message")
                .containsExactly("비속어가 포함되어 있습니다.");
    }
}
