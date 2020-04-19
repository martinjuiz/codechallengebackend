package com.codechallengebackend.demo.bank.model;

public class GetTransactionStatusResponse {

    private String reference;

    private String status;

    public GetTransactionStatusResponse(String reference, String status) {
        this.reference = reference;
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
