package com.codechallengebackend.demo.bank.repository;

import com.codechallengebackend.demo.bank.mock.AccountMockGenerator;
import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.Account;
import com.codechallengebackend.demo.bank.repository.impl.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.util.Assert;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig(TestBeanConfiguration.class)
public class InMemoryAccountRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private AccountRepository accountRepository;

    private static final String DEFAULT_ACCOUNTS_NOT_FOUND_ERROR = "At least one account has to be available!";


    @BeforeEach
    public void setUp() {
        accountRepository =  new InMemoryAccountRepository(jdbcTemplate);
    }

    @Test
    public void WhenRequestIsSent_ThenSystemReturnsListOfAccounts() {
        List<Account> accounts = AccountMockGenerator.listAccounts();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(accounts);

        List<Account> availableAccounts = accountRepository.findAll();
        Assert.notNull(availableAccounts, DEFAULT_ACCOUNTS_NOT_FOUND_ERROR);
        Assert.notEmpty(availableAccounts, DEFAULT_ACCOUNTS_NOT_FOUND_ERROR);
        Assert.noNullElements(availableAccounts, DEFAULT_ACCOUNTS_NOT_FOUND_ERROR);
    }
}
