package com.codechallengebackend.demo.bank.model.validation;

import org.apache.commons.validator.routines.IBANValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IBANParameterValidation implements ConstraintValidator<IBANValidated, String> {

    @Override
    public void initialize(IBANValidated ibanValidated) {

    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext constraintValidatorContext) {

        return IBANValidator.getInstance().isValid(iban);
    }
}
