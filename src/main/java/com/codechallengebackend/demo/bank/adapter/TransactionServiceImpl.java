package com.codechallengebackend.demo.bank.adapter;

import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.domain.validator.TransactionValidator;
import com.codechallengebackend.demo.bank.exception.InvalidTransactionDetailsException;
import com.codechallengebackend.demo.bank.repository.TransactionRepository;

import java.util.Optional;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
    }

    @Override
    public Transaction create(Transaction transaction) {

        transactionValidator.validate(transaction);

        return transactionRepository.insert(transaction)
                .orElseThrow(InvalidTransactionDetailsException::new);
    }

    @Override
    public Optional<Transaction> checkStatus(String reference) {
        return transactionRepository.findByReference(reference);
    }
}
