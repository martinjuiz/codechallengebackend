package com.codechallengebackend.demo.bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class InsufficientAccountBalanceException extends RuntimeException {

    @Getter
    private HttpStatus statusCode;

    public InsufficientAccountBalanceException() {
        super();
    }

    public InsufficientAccountBalanceException(final String message) {
        super(message);
    }

    public InsufficientAccountBalanceException(final HttpStatus statusCode, final String message) {

        super(message);
        this.statusCode = statusCode;
    }
}
