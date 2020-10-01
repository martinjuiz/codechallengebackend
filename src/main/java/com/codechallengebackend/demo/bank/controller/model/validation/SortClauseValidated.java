package com.codechallengebackend.demo.bank.controller.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortClauseParameterValidation.class)
@Target( { ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SortClauseValidated {

    String message() default "Sort clause not supported!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String propName() default "";

    String[] values() default {};
}
