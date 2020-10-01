package com.codechallengebackend.demo.bank.controller.model;

import java.util.List;

public class SearchTransactionResponse {

    private List<TransactionResponse> transactions;

    public SearchTransactionResponse() {}

    public SearchTransactionResponse(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }

    public List<TransactionResponse> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }
}
