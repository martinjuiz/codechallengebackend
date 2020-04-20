package com.codechallengebackend.demo.bank.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTransactionStatusResponse {

    private String reference;

    private String status;

    private Double amount;

    private Double fee;

    public GetTransactionStatusResponse() {}

    public GetTransactionStatusResponse(String reference, String status, Double amount, Double fee) {
        this.reference = reference;
        this.status = status;
        this.amount = amount;
        this.fee = fee;
    }

    public GetTransactionStatusResponse(String reference, String status, Double amount) {
        this.reference = reference;
        this.status = status;
        this.amount = amount;
    }

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }
}
