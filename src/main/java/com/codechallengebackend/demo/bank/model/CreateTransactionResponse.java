package com.codechallengebackend.demo.bank.model;

public final class CreateTransactionResponse {

    private String reference;

    public CreateTransactionResponse() {}

    public CreateTransactionResponse(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
