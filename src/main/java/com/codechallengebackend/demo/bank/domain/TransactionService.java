package com.codechallengebackend.demo.bank.domain;

import java.util.Optional;

/**
 *
 *
 */
public interface TransactionService {


    Transaction create(Transaction transaction);

    Optional<Transaction> checkStatus(String reference);
}
