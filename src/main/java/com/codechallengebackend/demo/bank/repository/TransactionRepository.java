package com.codechallengebackend.demo.bank.repository;

import com.codechallengebackend.demo.bank.domain.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> insert(Transaction transaction);

    Optional<Transaction> findByReference(String reference);
}
