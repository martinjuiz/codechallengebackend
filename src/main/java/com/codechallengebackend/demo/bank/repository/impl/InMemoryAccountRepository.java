package com.codechallengebackend.demo.bank.repository.impl;

import com.codechallengebackend.demo.bank.domain.Account;
import com.codechallengebackend.demo.bank.repository.AccountRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class InMemoryAccountRepository implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_ACCOUNT_QUERY = "SELECT id, iban, name, balance, balance_inc_pending, overdraft, pending FROM account";

    public InMemoryAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findAll() {

        return jdbcTemplate.query(SELECT_ALL_ACCOUNT_QUERY, (resultSet, i) -> new Account(
                resultSet.getString("id"),
                resultSet.getString("iban"),
                resultSet.getString("name"),
                resultSet.getDouble("balance"), resultSet.getDouble("balance_inc_pending"),
                resultSet.getDouble("overdraft"), resultSet.getDouble("pending")
        ));
    }
}
