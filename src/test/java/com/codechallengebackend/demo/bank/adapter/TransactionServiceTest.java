package com.codechallengebackend.demo.bank.adapter;

import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.domain.validator.TransactionValidator;
import com.codechallengebackend.demo.bank.exception.InvalidTransactionDetailsException;
import com.codechallengebackend.demo.bank.mock.IbanMockGenerator;
import com.codechallengebackend.demo.bank.mock.TransactionMockGenerator;
import com.codechallengebackend.demo.bank.model.Channel;
import com.codechallengebackend.demo.bank.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig(TestBeanConfiguration.class)
public class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionValidator transactionValidator;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionServiceImpl(transactionRepository, transactionValidator);
    }

    @Test
    public void WhenTransactionReferenceFormatIsInvalid_ThenSystemRejectsTheTransaction() {
        doThrow(InvalidTransactionDetailsException.class).when(transactionValidator).validate(any(Transaction.class));

        Transaction transaction = TransactionMockGenerator.transactionWithInvalidReference();

        Assertions.assertThrows(InvalidTransactionDetailsException.class,
                () -> transactionService.create(transaction));
    }

    @Test
    public void WhenTransactionIbanFormatIsInvalid_ThenSystemRejectsTheTransaction() {
        doThrow(InvalidTransactionDetailsException.class).when(transactionValidator).validate(any(Transaction.class));

        Transaction transaction = TransactionMockGenerator.transactionWithInvalidIban();

        Assertions.assertThrows(InvalidTransactionDetailsException.class,
                () -> transactionService.create(transaction));
    }

    @Test
    public void WhenTransactionDateFormatIsInvalid_ThenSystemRejectsTheTransaction() {

    }

    @Test
    public void WhenTransactionAmountFormatIsInvalid_ThenSystemRejectsTheTransaction() {

    }

    @Test
    public void WhenTransactionFeeFormatIsInvalid_ThenSystemRejectsTheTransaction() {

    }

    @Test
    public void WhenTransactionDescriptionFormatIsInvalid_ThenSystemRejectsTheTransaction() {

    }

    @Test
    public void WhenTransactionReferenceIsNotProvided_ThenSystemGeneratesOne() {
        Transaction transaction = TransactionMockGenerator.transactionWithEmptyReference();

        doNothing().when(transactionValidator).validate(any(Transaction.class));
        Transaction newTransaction = TransactionMockGenerator.transactionWithReference();
        when(transactionRepository.insert(any(Transaction.class))).thenReturn(Optional.of(newTransaction));

        Transaction created = transactionService.create(transaction);
        Assert.notNull(created, "Something went wrong creating the transaction in the system");
        Assert.hasText(created.getReference(), "The system did not create a reference for the transaction");
    }

    @Test
    public void WhenTransactionReferenceIsProvided_ThenSystemReturnsSameValue() {

    }

    @Test
    public void WhenTransactionAmountIsGreaterThanAccountBalance_ThenSystemRejectsTheTransaction() {

    }

    @Test
    public void WhenTransactionExists_AndCheckStatus_ThenSystemFindRecord() {
        Transaction transaction = TransactionMockGenerator.transactionWithReference();
        when(transactionRepository.findByReference(anyString())).thenReturn(Optional.of(transaction));

        final Optional<Transaction> record = transactionService.checkStatus("12345A", Channel.CLIENT.name());
        Assert.isTrue(record.isPresent(), "Transaction could not be found in the system!");
    }

    @Test
    public void WhenTransactionDoesNotExist_AndCheckStatus_ThenSystemDoesNotFindRecord() {
        when(transactionRepository.findByReference(anyString())).thenReturn(Optional.empty());

        final Optional<Transaction> record = transactionService.checkStatus("12345A", Channel.CLIENT.name());
        Assert.isTrue(record.isEmpty(), "System returned a wrong record for the given reference");
    }

    @Test
    public void WhenSearchingByAccount_ThenSystemReturnsListOfAccounts() {
        final Transaction transaction1 = TransactionMockGenerator.transactionWithReference();
        final Transaction transaction2 = TransactionMockGenerator.transactionWithReference();
        final var transactions = List.of(transaction1, transaction2);
        when(transactionRepository.findByAccount(anyString(), anyString())).thenReturn(Optional.of(transactions));

        final var records = transactionService.findByAccount(IbanMockGenerator.IBAN_SAMPLE_1, "amount:asc");
        Assert.isTrue(records.isPresent(), "System did not return results for the given criteria");
    }

    @Test
    public void WhenSearchingByAccount_AndSortingByUnsupportedField_ThenSystemDoesNotSortResults() {
        final Transaction transaction1 = TransactionMockGenerator.transactionWithReference();
        final Transaction transaction2 = TransactionMockGenerator.transactionWithReference();
        final var transactions = List.of(transaction1, transaction2);
        when(transactionRepository.findByAccount(anyString(), anyString())).thenReturn(Optional.of(transactions));

        final var records = transactionService.findByAccount(IbanMockGenerator.IBAN_SAMPLE_1, "other:asc");
        Assert.isTrue(records.isPresent(), "System did not return results for the given criteria");
    }

    @Test
    public void WhenSearchingByAccount_AndNoResults_ThenEmptyResponseIsReturned() {
        when(transactionRepository.findByAccount(anyString(), anyString())).thenReturn(Optional.empty());

        final var records = transactionService.findByAccount(IbanMockGenerator.IBAN_SAMPLE_3, "amount:asc");
        Assert.isTrue(records.isEmpty(), "System returned unexpected results");
    }
}