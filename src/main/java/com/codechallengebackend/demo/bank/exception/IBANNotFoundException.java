package com.codechallengebackend.demo.bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class IBANNotFoundException extends RuntimeException {

    @Getter
    private HttpStatus statusCode;

    public IBANNotFoundException() {
        super();
    }

    public IBANNotFoundException(final String message) {
        super(message);
    }

    public IBANNotFoundException(final HttpStatus statusCode, final String message) {

        super(message);
        this.statusCode = statusCode;
    }
}
