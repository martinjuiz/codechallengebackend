package com.codechallengebackend.demo.bank.adapter;

import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.exception.InvalidTransactionDetailsException;
import com.codechallengebackend.demo.bank.model.Channel;
import com.codechallengebackend.demo.bank.repository.TransactionRepository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    public Transaction create(Transaction transaction) {
        if(StringUtils.isEmpty(transaction.getReference())) {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 10) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            transaction.setReference(salt.toString());
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
