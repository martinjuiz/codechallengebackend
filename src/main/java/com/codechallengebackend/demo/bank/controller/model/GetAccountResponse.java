package com.codechallengebackend.demo.bank.controller.model;

import com.codechallengebackend.demo.bank.domain.Account;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAccountResponse {

    private String id;

    @JsonProperty("account_iban")
    private String iban;

    private String name;

    private Double balance;

    public GetAccountResponse(String id, String iban, String name, Double balance) {
        this.id = id;
        this.iban = iban;
        this.name = name;
        this.balance = balance;
    }

    public static GetAccountResponse of(Account account) {
        return new GetAccountResponse(
                account.getId(), account.getIban(), account.getName(),
                account.getBalance());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
