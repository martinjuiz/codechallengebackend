package com.codechallengebackend.demo.bank.adapter;

import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.domain.validator.TransactionValidator;
import com.codechallengebackend.demo.bank.exception.InvalidTransactionDetailsException;
import com.codechallengebackend.demo.bank.model.Channel;
import com.codechallengebackend.demo.bank.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    public Optional<Transaction> checkStatus(String reference, String channel) {
        Transaction transaction = null;
        final var record = transactionRepository.findByReference(reference);
        if(record.isPresent()) {
            transaction = record.get();
            final var now = LocalDate.now().atStartOfDay();
            final var transactionDate = LocalDateTime
                    .parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                    .toLocalDate().atStartOfDay();

            if(transactionDate.isBefore(now) && (Channel.ATM.equals(Channel.valueOf(channel)) || Channel.CLIENT.equals(Channel.valueOf(channel)))) {
                transaction.setStatus(Transaction.TransactionStatus.SETTLED);
                transaction.setAmount(transaction.getAmount() - transaction.getFee());
                transaction.setFee(null);
            }
            else if(transactionDate.isAfter(now) && Channel.CLIENT.equals(Channel.valueOf(channel))) {
                transaction.setStatus(Transaction.TransactionStatus.FUTURE);
                transaction.setAmount(transaction.getAmount() - transaction.getFee());
                transaction.setFee(null);
            }
            else if(transactionDate.isAfter(now) && Channel.ATM.equals(Channel.valueOf(channel))) {
                transaction.setStatus(Transaction.TransactionStatus.PENDING);
                transaction.setAmount(transaction.getAmount() - transaction.getFee());
                transaction.setFee(null);
            }
            else if(transactionDate.isAfter(now) && Channel.INTERNAL.equals(Channel.valueOf(channel))) {
                transaction.setStatus(Transaction.TransactionStatus.FUTURE);
            }
            else if(isTransactionExecutedToday(transactionDate) && (Channel.ATM.equals(Channel.valueOf(channel)) || Channel.CLIENT.equals(Channel.valueOf(channel)))) {
                transaction.setStatus(Transaction.TransactionStatus.PENDING);
                transaction.setAmount(transaction.getAmount() - transaction.getFee());
                transaction.setFee(null);
            }
            else if(transactionDate.isBefore(now) && Channel.INTERNAL.equals(Channel.valueOf(channel))) {
                transaction.setStatus(Transaction.TransactionStatus.SETTLED);
            }
            else if(isTransactionExecutedToday(transactionDate) && Channel.INTERNAL.equals(Channel.valueOf(channel))) {
                transaction.setStatus(Transaction.TransactionStatus.PENDING);
            }

        }

        return Optional.ofNullable(transaction);
    }

    @Override
    public Optional<List<Transaction>> findByAccount(String account, String sortBy) {

        return transactionRepository.findByAccount(account, sortBy);
    }

    private boolean isTransactionExecutedToday(LocalDateTime transactionDate) {
        final var now = LocalDateTime.now();
        return transactionDate.getYear() == now.getYear() &&
               transactionDate.getMonth() == now.getMonth() &&
               transactionDate.getDayOfMonth() == now.getDayOfMonth();
    }
}
