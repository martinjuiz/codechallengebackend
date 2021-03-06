package com.codechallengebackend.demo.bank.service;

import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.Account;
import com.codechallengebackend.demo.bank.exception.IBANNotFoundException;
import com.codechallengebackend.demo.bank.exception.InsufficientAccountBalanceException;
import com.codechallengebackend.demo.bank.mock.AccountMockGenerator;
import com.codechallengebackend.demo.bank.mock.IbanMockGenerator;
import com.codechallengebackend.demo.bank.repository.AccountRepository;
import com.codechallengebackend.demo.bank.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @Test
    public void WhenTransactionAmountIsEqualsOrLessThanAccountBalance_ThenTransactionIsAccepted() {
        Account account = AccountMockGenerator.account(UUID.randomUUID().toString());
        when(accountRepository.findByIBAN(anyString())).thenReturn(Optional.of(account));

        accountService.canBeDebited(IbanMockGenerator.IBAN_SAMPLE_2, 10D);

        verify(accountRepository, times(1)).findByIBAN(anyString());
    }

    @Test
    public void WhenIBANDoesNotExist_ThenSystemThrowsException() {
        when(accountRepository.findByIBAN(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(IBANNotFoundException.class,
                () -> accountService.canBeDebited(IbanMockGenerator.IBAN_SAMPLE_2, 10D));
    }

    @Test
    public void WhenTransactionAmountIsGreaterThanAccountBalance_ThenTransactionIsRejected() {
        Account account = AccountMockGenerator.account(UUID.randomUUID().toString());
        when(accountRepository.findByIBAN(anyString())).thenReturn(Optional.of(account));

        Assertions.assertThrows(InsufficientAccountBalanceException.class,
                () -> accountService.canBeDebited(IbanMockGenerator.IBAN_SAMPLE_2, -100000D));
    }

    @Test
    public void WhenTransactionAmountIsPositive_ThenAccountIsCredited() {
        Account account = AccountMockGenerator.account(UUID.randomUUID().toString());
        when(accountRepository.findByIBAN(anyString())).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).updateBalance(any(Account.class));

        accountService.updateBalance(IbanMockGenerator.IBAN_SAMPLE_2, 100D);

        verify(accountRepository, times(1)).findByIBAN(anyString());
    }
}
