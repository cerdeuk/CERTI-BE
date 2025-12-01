package org.sopt.certi_server.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.sopt.certi_server.global.valid.ProfanityValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProfanityValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoProfanity {
    String message() default "비속어가 포함되어 있습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
