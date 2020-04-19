package com.codechallengebackend.demo.bank.model;

import com.codechallengebackend.demo.bank.domain.Account;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAccountResponse {

    private String id;

    @JsonProperty("account_iban")
    private String iban;

    private String name;

    private Double balance;

    @JsonProperty("balance_including_pending")
    private Double balanceIncPending;

    private Double overdraft;

    private Double pending;

    public GetAccountResponse(String id, String iban, String name, Double balance, Double balanceIncPending, Double overdraft, Double pending) {
        this.id = id;
        this.iban = iban;
        this.name = name;
        this.balance = balance;
        this.balanceIncPending = balanceIncPending;
        this.overdraft = overdraft;
        this.pending = pending;
    }

    public static GetAccountResponse of(Account account) {
        return new GetAccountResponse(
                account.getId(), account.getIban(), account.getName(),
                account.getBalance(), account.getBalanceIncPending(), account.getOverdraft(), account.getPending()
        );
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

    public Double getBalanceIncPending() {
        return balanceIncPending;
    }

    public void setBalanceIncPending(Double balanceIncPending) {
        this.balanceIncPending = balanceIncPending;
    }

    public Double getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(Double overdraft) {
        this.overdraft = overdraft;
    }

    public Double getPending() {
        return pending;
    }

    public void setPending(Double pending) {
        this.pending = pending;
    }
}
