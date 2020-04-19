package com.codechallengebackend.demo.bank.domain;

public class Transaction {

    private String reference;

    private String iban;

    private String date;

    private Double amount;

    private Double fee;

    private TransactionStatus status;

    private String description;

    public enum TransactionStatus {
        PENDING,
        SETTLED,
        FUTURE,
        INVALID
    }

    public Transaction(String reference, String iban, String date, Double amount, Double fee, TransactionStatus status, String description) {
        this.reference = reference;
        this.iban = iban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.status = status;
        this.description = description;
    }

    public Transaction(String reference, String iban, String date, Double amount, Double fee, String description) {
        this.reference = reference;
        this.iban = iban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.status = TransactionStatus.PENDING;
        this.description = description;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
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
