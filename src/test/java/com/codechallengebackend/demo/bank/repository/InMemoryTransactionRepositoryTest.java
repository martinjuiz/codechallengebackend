package com.codechallengebackend.demo.bank.repository;

import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.mock.TransactionMockGenerator;
import com.codechallengebackend.demo.bank.repository.impl.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig(TestBeanConfiguration.class)
public class InMemoryTransactionRepositoryTest {

    public static final int INSERT_SUCCESSFUL_OPERATION_RESULT = 1;
    @Mock
    private JdbcTemplate jdbcTemplate;

    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        transactionRepository = new InMemoryTransactionRepository(jdbcTemplate);
    }

    @Test
    public void WhenTransactionIsProvided_ThenSystemCreatesNewRecord() {
        final Transaction transaction = TransactionMockGenerator.transactionWithReference();
        when(jdbcTemplate.update(anyString(), any(Object.class))).thenReturn(INSERT_SUCCESSFUL_OPERATION_RESULT);
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(transaction);

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
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(transaction);

        final Optional<Transaction> record = transactionRepository.findByReference("12345A");
        Assert.isTrue(record.isPresent(), "An existing transaction must return a valid reference");
    }
}
