package com.codechallengebackend.demo.bank.repository;

import com.codechallengebackend.demo.bank.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<Account> findAll();

    Optional<Account> findByIBAN(String iban);

    void updateBalance(Account account);
}
