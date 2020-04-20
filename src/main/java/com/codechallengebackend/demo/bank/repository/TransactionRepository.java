package com.codechallengebackend.demo.bank.repository;

import com.codechallengebackend.demo.bank.domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> insert(Transaction transaction);

    Optional<Transaction> findByReference(String reference);

    Optional<List<Transaction>> findByAccount(String account, String sortBy);
}
