package com.codechallengebackend.demo.bank.controller.model;

import com.codechallengebackend.demo.bank.controller.model.validation.IBANValidated;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateTransactionRequest {

    private String reference;

    @NotNull
    @NotEmpty
    @IBANValidated
    @JsonProperty("account_iban")
    private String iban;

    private String date;

    @NotNull
    private Double amount;

    private Double fee;

    private String description;

    public CreateTransactionRequest() {};

    public CreateTransactionRequest(String reference, @NotNull @NotEmpty String iban, String date, @NotNull Double amount, Double fee, String description) {
        this.reference = reference;
        this.iban = iban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
