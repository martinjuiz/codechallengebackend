package com.codechallengebackend.demo.bank.domain;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    void canBeDebited(String iban, Double transactionAmount);

    void updateBalance(String iban, Double transactionAmount);
}
