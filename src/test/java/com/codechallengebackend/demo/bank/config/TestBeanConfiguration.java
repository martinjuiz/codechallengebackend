package com.codechallengebackend.demo.bank.config;

import com.codechallengebackend.demo.bank.adapter.AccountServiceImpl;
import com.codechallengebackend.demo.bank.domain.AccountService;
import com.codechallengebackend.demo.bank.domain.validator.TransactionValidator;
import com.codechallengebackend.demo.bank.repository.AccountRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

import static org.mockito.Mockito.mock;

@TestConfiguration
@TestPropertySource
public class TestBeanConfiguration {

    @Bean
    public TransactionValidator transactionValidator() {
        return new TransactionValidator();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(mock(DataSource.class));
    }

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl(mock(AccountRepository.class));
    }
}
