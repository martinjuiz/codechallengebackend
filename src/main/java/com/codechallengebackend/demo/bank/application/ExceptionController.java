package com.codechallengebackend.demo.bank.application;

import com.codechallengebackend.demo.bank.exception.IBANNotFoundException;
import com.codechallengebackend.demo.bank.exception.InsufficientAccountBalanceException;
import com.codechallengebackend.demo.bank.exception.InvalidTransactionDetailsException;
import com.codechallengebackend.demo.bank.exception.NoTransactionsFoundException;
import com.codechallengebackend.demo.bank.model.error.ApiErrorResponse;
import com.codechallengebackend.demo.bank.model.error.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExceptionController {

    private final ObjectMapper objectMapper;

    public ExceptionController(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(NoTransactionsFoundException.class)
    public ResponseEntity<String> noTransactionsFoundException(final NoTransactionsFoundException exception) throws JsonProcessingException {

        return parseError(new ApiErrorResponse(ErrorCode.NO_TRANSACTIONS_FOUND.name(),
                "The system returned no results with the given criteria"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTransactionDetailsException.class)
    public ResponseEntity<String> invalidTransactionDetailsException(final InvalidTransactionDetailsException exception) throws JsonProcessingException {

        return parseError(new ApiErrorResponse(ErrorCode.NO_TRANSACTIONS_FOUND.name(),
                "The system found an error processing the request"),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(IBANNotFoundException.class)
    public ResponseEntity<String> ibanNotFoundException(final IBANNotFoundException exception) throws JsonProcessingException {

        return parseError(new ApiErrorResponse(ErrorCode.NO_IBAN_FOUND.name(),
                exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientAccountBalanceException.class)
    public ResponseEntity<String> insufficientAccountBalanceException(final InsufficientAccountBalanceException exception) throws JsonProcessingException {

        return parseError(new ApiErrorResponse(ErrorCode.INSUFFICIENT_ACCOUNT_BALANCE.name(),
                exception.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<String> parseError(final ApiErrorResponse errorResponse, final HttpStatus status) throws JsonProcessingException {

        final String exceptionAsString = objectMapper.writeValueAsString(errorResponse);
        log.error("The service threw an exception:\n".concat(exceptionAsString));

        return new ResponseEntity<>(exceptionAsString, status);
    }
}
