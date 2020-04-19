package com.codechallengebackend.demo.bank.infrastructure;

import com.codechallengebackend.demo.bank.adapter.AccountServiceImpl;
import com.codechallengebackend.demo.bank.adapter.TransactionServiceImpl;
import com.codechallengebackend.demo.bank.domain.AccountService;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.domain.validator.TransactionValidator;
import com.codechallengebackend.demo.bank.repository.AccountRepository;
import com.codechallengebackend.demo.bank.repository.TransactionRepository;
import com.codechallengebackend.demo.bank.repository.impl.InMemoryAccountRepository;
import com.codechallengebackend.demo.bank.repository.impl.InMemoryTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class BeanConfiguration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public AccountRepository accountRepository() {
        return new InMemoryAccountRepository(jdbcTemplate);
    }

    @Bean
    public TransactionRepository transactionRepository() {
        return new InMemoryTransactionRepository(jdbcTemplate);
    }

    @Bean
    public TransactionValidator transactionValidator() {
        return new TransactionValidator();
    }

    @Bean
    public AccountService accountService(@Autowired JdbcTemplate jdbcTemplate) {
        return new AccountServiceImpl(accountRepository());
    }

    @Bean
    public TransactionService transactionService(@Autowired JdbcTemplate jdbcTemplate) {
        return new TransactionServiceImpl(transactionRepository(), transactionValidator());
    }
}
