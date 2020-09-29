package com.codechallengebackend.demo.bank.adapter;

import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.exception.InvalidTransactionDetailsException;
import com.codechallengebackend.demo.bank.model.Channel;
import com.codechallengebackend.demo.bank.repository.TransactionRepository;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;

public class TransactionServiceImpl implements TransactionService {

    private static final String TRANSACTION_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction create(Transaction transaction) {
        if(StringUtils.isEmpty(transaction.getReference())) {
            transaction.setReference(generateReference());
        }

        if(StringUtils.isEmpty(transaction.getDate())) {
            SimpleDateFormat sdf = new SimpleDateFormat(TRANSACTION_DATE_FORMAT);
            transaction.setDate(sdf.format(new Date()));
        }

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
                    .parse(transaction.getDate(), DateTimeFormatter.ofPattern(TRANSACTION_DATE_FORMAT))
                    .toLocalDate().atStartOfDay();

            BiFunction<Double, Double, Double> parseAmount = (amount, fee) -> (double) Math.round(amount - fee);

            if(transactionDate.isBefore(now) && (Channel.ATM.equals(Channel.valueOf(channel)) || Channel.CLIENT.equals(Channel.valueOf(channel)))) {
                transaction.setStatus(Transaction.TransactionStatus.SETTLED);
                transaction.setAmount(parseAmount.apply(transaction.getAmount(), transaction.getFee()));
                transaction.setFee(null);
            }
            else if(transactionDate.isAfter(now) && Channel.CLIENT.equals(Channel.valueOf(channel))) {
                transaction.setStatus(Transaction.TransactionStatus.FUTURE);
                transaction.setAmount(parseAmount.apply(transaction.getAmount(), transaction.getFee()));
                transaction.setFee(null);
            }
            else if(transactionDate.isAfter(now) && Channel.ATM.equals(Channel.valueOf(channel))) {
                transaction.setStatus(Transaction.TransactionStatus.PENDING);
                transaction.setAmount(parseAmount.apply(transaction.getAmount(), transaction.getFee()));
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
