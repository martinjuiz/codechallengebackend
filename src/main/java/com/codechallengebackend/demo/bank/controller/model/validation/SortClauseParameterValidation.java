package com.codechallengebackend.demo.bank.controller.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.function.Predicate;

public class SortClauseParameterValidation implements ConstraintValidator<SortClauseValidated, String> {

    private static final List<String> SUPPORTED_SORT_FIELDS = List.of("amount");
    private static final int SORT_CLAUSE_ARRAY_LENGTH = 2;

    @Override
    public void initialize(SortClauseValidated sortClauseValidated) {

    }

    @Override
    public boolean isValid(String sortField, ConstraintValidatorContext constraintValidatorContext) {

        Predicate<String> hasCorrectFormat = (sortingBy) -> sortingBy.contains(":") && sortingBy.split(":").length == SORT_CLAUSE_ARRAY_LENGTH;

        Predicate<String> isSupportedField = (sortingClause) -> SUPPORTED_SORT_FIELDS.stream()
                .anyMatch(field -> field.equalsIgnoreCase(sortingClause.split(":")[0]));

        Predicate<String> directionIsAscOrDesc = (sortingBy) -> sortingBy.split(":")[1].equalsIgnoreCase("asc") ||
                sortingBy.split(":")[1].equalsIgnoreCase("desc");

        return hasCorrectFormat.test(sortField) &&
                isSupportedField.test(sortField) &&
                    directionIsAscOrDesc.test(sortField);
    }
}
