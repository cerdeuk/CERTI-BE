package org.sopt.certi_server.global.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.global.annotation.NoProfanity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfanityValidator implements ConstraintValidator<NoProfanity, String> {

    private final ProfanityFilter filter;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !filter.containsProfanity(value);
    }
}
