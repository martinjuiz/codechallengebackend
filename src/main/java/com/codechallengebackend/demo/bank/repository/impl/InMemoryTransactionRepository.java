package com.codechallengebackend.demo.bank.repository.impl;

import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class InMemoryTransactionRepository implements TransactionRepository {

    private static final String INSERT_TRANSACTION_QUERY = "INSERT INTO transaction (reference, iban, date, amount, fee, status, description) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public InMemoryTransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Transaction> insert(Transaction transaction) {
        Optional<Transaction> created = Optional.empty();

        final int result = jdbcTemplate.update(INSERT_TRANSACTION_QUERY,
                transaction.getReference(), transaction.getIban(), transaction.getDate(),
                transaction.getAmount(), transaction.getFee(),
                transaction.getStatus().name(), transaction.getDescription());

        if(result == 1) {
            created = findByReference(transaction.getReference());
        }

        return created;
    }

    @Override
    public Optional<Transaction> findByReference(String reference) {
        Optional<Transaction> transaction = Optional.empty();
        final List<Transaction> result = jdbcTemplate.query("SELECT reference, iban, date, amount, fee, status, description FROM transaction where reference = ?",
                new Object[]{reference},
                (resultSet, i) -> new Transaction(
                        resultSet.getString("reference"),
                        resultSet.getString("iban"),
                        resultSet.getString("date"),
                        resultSet.getDouble("amount"),
                        resultSet.getDouble("fee"),
                        Transaction.TransactionStatus.valueOf(resultSet.getString("status")),
                        resultSet.getString("description")));

        if(result != null && result.size() == 1) {
            transaction = Optional.ofNullable(result.get(0));
        }

        return transaction;
    }

    @Override
    public Optional<List<Transaction>> findByAccount(String account, String sortBy) {
        final var sortArray = sortBy.split(":");
        final var field = sortArray[0];
        final var direction = sortArray[1];

        final List<Transaction> transactions = jdbcTemplate
                .query("SELECT reference, iban, date, amount, fee, status, description FROM transaction where iban = ? order by "
                        .concat(field.toLowerCase().concat(" ")
                        .concat(direction.toLowerCase())),
                    new Object[]{account},
                    (resultSet, i) -> new Transaction(
                            resultSet.getString("reference"),
                            resultSet.getString("iban"),
                            resultSet.getString("date"),
                            resultSet.getDouble("amount"),
                            resultSet.getDouble("fee"),
                            Transaction.TransactionStatus.valueOf(resultSet.getString("status")),
                            resultSet.getString("description")));

        return Optional.ofNullable(transactions);
    }
}
