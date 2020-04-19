package com.codechallengebackend.demo.bank.adapter;

import com.codechallengebackend.demo.bank.adapter.AccountServiceImpl;
import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.Account;
import com.codechallengebackend.demo.bank.domain.AccountService;
import com.codechallengebackend.demo.bank.mock.AccountMockGenerator;
import com.codechallengebackend.demo.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.util.Assert;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringJUnitWebConfig(TestBeanConfiguration.class)
public class AccountServiceTest {

    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private static final String DEFAULT_ACCOUNTS_NOT_FOUND_ERROR = "At least one account has to be available!";

    @BeforeEach
    public void setUp() {
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void WhenRequestIsSent_ThenSystemReturnsListOfAccounts() {
        List<Account> accounts = AccountMockGenerator.listAccounts();
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> availableAccounts = accountService.findAll();
        Assert.notNull(availableAccounts, DEFAULT_ACCOUNTS_NOT_FOUND_ERROR);
        Assert.notEmpty(availableAccounts, DEFAULT_ACCOUNTS_NOT_FOUND_ERROR);
        Assert.noNullElements(availableAccounts, DEFAULT_ACCOUNTS_NOT_FOUND_ERROR);
    }
}
