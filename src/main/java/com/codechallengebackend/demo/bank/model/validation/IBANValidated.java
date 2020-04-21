package com.codechallengebackend.demo.bank.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IBANParameterValidation.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IBANValidated {

    String message() default "IBAN account format is incorrect!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String propName() default "";

    String[] values() default {};
}
