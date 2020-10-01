package com.codechallengebackend.demo.bank.controller.model.validation;

import com.codechallengebackend.demo.bank.domain.Transaction;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class TransactionStatusParameterValidation implements ConstraintValidator<TransactionStatusAnnotation, String> {

    @Override
    public void initialize(TransactionStatusAnnotation transactionStatus) {

    }

    @Override
    public boolean isValid(String transactionStatus, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Transaction.TransactionStatus.values())
                .anyMatch(ch -> ch.name().equals(transactionStatus));
    }
}
