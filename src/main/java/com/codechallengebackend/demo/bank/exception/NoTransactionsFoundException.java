package com.codechallengebackend.demo.bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NoTransactionsFoundException extends RuntimeException {

    @Getter
    private HttpStatus statusCode;

    public NoTransactionsFoundException() {
        super();
    }

    public NoTransactionsFoundException(final String message) {
        super(message);
    }

    public NoTransactionsFoundException(final HttpStatus statusCode, final String message) {

        super(message);
        this.statusCode = statusCode;
    }
}
