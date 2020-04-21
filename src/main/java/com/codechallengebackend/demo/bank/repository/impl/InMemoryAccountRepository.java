package com.codechallengebackend.demo.bank.repository.impl;

import com.codechallengebackend.demo.bank.domain.Account;
import com.codechallengebackend.demo.bank.repository.AccountRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_ACCOUNT_QUERY =
            "SELECT id, iban, name, balance FROM account";

    private static final String SELECT_ACCOUNT_BY_IBAN_QUERY =
            "SELECT id, iban, name, balance FROM account WHERE iban = ?";

    private static final String UPDATE_ACCOUNT_BALANCE_QUERY =
            "UPDATE account SET balance = ? WHERE iban = ?";

    public InMemoryAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findAll() {

        return jdbcTemplate.query(SELECT_ALL_ACCOUNT_QUERY, (resultSet, i) -> new Account(
                resultSet.getString("id"),
                resultSet.getString("iban"),
                resultSet.getString("name"),
                resultSet.getDouble("balance")
        ));
    }

    @Override
    public Optional<Account> findByIBAN(String iban) {

        final var account = jdbcTemplate.queryForObject(SELECT_ACCOUNT_BY_IBAN_QUERY,
                new Object[] {iban},
                (resultSet, i) -> new Account(
                resultSet.getString("id"),
                resultSet.getString("iban"),
                resultSet.getString("name"),
                resultSet.getDouble("balance")
        ));

        return Optional.ofNullable(account);
    }

    @Override
    public void updateBalance(Account account) {

        jdbcTemplate.update(UPDATE_ACCOUNT_BALANCE_QUERY, preparedStatement -> {
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setString(2, account.getIban());
        });
    }
}
