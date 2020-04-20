package com.codechallengebackend.demo.bank.domain;

import java.util.List;
import java.util.Optional;

/**
 *
 *
 */
public interface TransactionService {

    Transaction create(Transaction transaction);

    Optional<Transaction> checkStatus(String reference, String channel);

    Optional<List<Transaction>> findByAccount(String account, String sortBy);
}
