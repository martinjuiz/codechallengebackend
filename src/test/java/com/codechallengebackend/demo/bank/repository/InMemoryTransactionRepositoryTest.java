package com.codechallengebackend.demo.bank.repository;

import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.mock.IbanMockGenerator;
import com.codechallengebackend.demo.bank.mock.TransactionMockGenerator;
import com.codechallengebackend.demo.bank.repository.impl.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig(TestBeanConfiguration.class)
public class InMemoryTransactionRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private TransactionRepository transactionRepository;

    private static final int INSERT_SUCCESSFUL_OPERATION_RESULT = 1;

    @BeforeEach
    public void setUp() {
        transactionRepository = new InMemoryTransactionRepository(jdbcTemplate);
    }

    @Test
    public void WhenTransactionIsProvided_ThenSystemCreatesNewRecord() {
        final Transaction transaction = TransactionMockGenerator.transactionWithReference();
        when(jdbcTemplate.update(anyString(), any(Object.class))).thenReturn(INSERT_SUCCESSFUL_OPERATION_RESULT);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(List.of(transaction));

        final Optional<Transaction> created = transactionRepository.insert(transaction);
        Assert.isTrue(created.isPresent(), "The transaction could not be registered in the system");
    }

    @Test
    public void WhenReferenceIsProvided_AndTransactionDoesNotExist_ThenSystemReturnsNoResult() {
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(null);

        final Optional<Transaction> record = transactionRepository.findByReference("12345A");
        Assert.isTrue(record.isEmpty(), "A non-existing transaction cannot return a valid reference");
    }

    @Test
    public void WhenReferenceIsProvided_AndTransactionExists_ThenSystemReturnsOneRecord() {
        final Transaction transaction = TransactionMockGenerator.transactionWithReference();
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(List.of(transaction));

        final Optional<Transaction> record = transactionRepository.findByReference("12345A");
        Assert.isTrue(record.isPresent(), "An existing transaction must return a valid reference");
    }

    @Test
    public void WhenFilterByExistingAccount_AndSortByAmountAsc_ThenSystemReturnsListOfTransactions() {
        final Transaction transaction1 = TransactionMockGenerator.transactionWithReference();
        final Transaction transaction2 = TransactionMockGenerator.transactionWithReference();
        final var transactions = List.of(transaction2, transaction1);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(transactions);

        final Optional<List<Transaction>> records = transactionRepository.findByAccount(IbanMockGenerator.IBAN_SAMPLE_1, "amount:asc");
        Assert.isTrue(records.isPresent(), "System returned no results for the given criteria");
    }

    @Test
    public void WhenFilterByExistingAccount_AndSortByAmountDesc_ThenSystemReturnsListOfTransactions() {
        final Transaction transaction1 = TransactionMockGenerator.transactionWithReference();
        final Transaction transaction2 = TransactionMockGenerator.transactionWithReference();
        final var transactions = List.of(transaction1, transaction2);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(transactions);

        final Optional<List<Transaction>> records = transactionRepository.findByAccount(IbanMockGenerator.IBAN_SAMPLE_2, "amount:desc");
        Assert.isTrue(records.isPresent(), "System returned no results for the given criteria");
    }

    @Test
    public void WhenFilterByNonExistingAccount_ThenSystemReturnsEmptyListOfTransactions() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(null);

        final Optional<List<Transaction>> records = transactionRepository.findByAccount("32426761", "amount:asc");
        Assert.isTrue(records.isEmpty(), "System returned unexpected results");
    }
}
