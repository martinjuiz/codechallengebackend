package com.codechallengebackend.demo.bank.domain;

public class Account {

    private String id;

    private String iban;

    private String name;

    private Double balance;

    public Account(String id, String iban, String name, Double balance) {
        this.id = id;
        this.iban = iban;
        this.name = name;
        this.balance = balance;
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
