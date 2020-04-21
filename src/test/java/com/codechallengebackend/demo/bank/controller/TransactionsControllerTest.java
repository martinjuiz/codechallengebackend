package com.codechallengebackend.demo.bank.controller;

import com.codechallengebackend.demo.bank.application.ExceptionController;
import com.codechallengebackend.demo.bank.application.TransactionsController;
import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.AccountService;
import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.exception.InsufficientAccountBalanceException;
import com.codechallengebackend.demo.bank.mock.CreateTransactionRequestMockGenerator;
import com.codechallengebackend.demo.bank.mock.IbanMockGenerator;
import com.codechallengebackend.demo.bank.mock.TransactionMockGenerator;
import com.codechallengebackend.demo.bank.model.CreateTransactionRequest;
import com.codechallengebackend.demo.bank.model.GetTransactionStatusResponse;
import com.codechallengebackend.demo.bank.model.SearchTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(TestBeanConfiguration.class)
public class TransactionsControllerTest {

    private static final String TRANSACTIONS_URI = "/transactions";
    private static final String SEARCH_TRANSACTIONS_URI = "/transactions/searching?account_iban={account_iban}&sort={sort_by}";

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionsController transactionsController;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(transactionsController)
                .setControllerAdvice(new ExceptionController(objectMapper))
                .build();
    }

    @Test
    public void WhenTransactionAmountIsLessOrEqualsToAccountBalance_ThenSystemAcceptsTheTransaction() throws Exception {
        CreateTransactionRequest request = CreateTransactionRequestMockGenerator.transactionWithoutReferenceOk();

        final Transaction created = TransactionMockGenerator.transactionWithReference();
        doNothing().when(accountService).canBeDebited(anyString(), any(Double.class));
        when(transactionService.create(any(Transaction.class))).thenReturn(created);
        doNothing().when(accountService).updateBalance(anyString(), any(Double.class));

        final String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(TRANSACTIONS_URI)
                .accept(APPLICATION_JSON_VALUE)
                .content(jsonRequest)
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(accountService, times(1)).canBeDebited(anyString(), any(Double.class));
        verify(transactionService, times(1)).create(any(Transaction.class));
        verify(accountService, times(1)).updateBalance(anyString(), any(Double.class));
    }

    @Test
    public void WhenTransactionAmountIsGreaterThanAccountBalance_ThenSystemRejectsTheTransaction() throws Exception {
        CreateTransactionRequest request = CreateTransactionRequestMockGenerator.transactionWithoutReferenceOk();
        doThrow(InsufficientAccountBalanceException.class).when(accountService).canBeDebited(anyString(), any(Double.class));

        final String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(TRANSACTIONS_URI)
                .accept(APPLICATION_JSON_VALUE)
                .content(jsonRequest)
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());

        verify(accountService, times(1)).canBeDebited(anyString(), any(Double.class));
    }

    @Test
    public void WhenTransactionIsNotStored_AndCheckStatus_ThenStatusIsInvalid() throws Exception {
        when(transactionService.checkStatus(anyString(), anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get(TRANSACTIONS_URI.concat("?reference=12345A&channel=CLIENT")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void WhenTransactionIsStored_AndCheckStatus_ThenSystemReturnsReferenceAndStatus() throws Exception {
        final Transaction transaction = TransactionMockGenerator.transactionWithReference();
        when(transactionService.checkStatus(anyString(), anyString())).thenReturn(Optional.of(transaction));

        mockMvc.perform(get(TRANSACTIONS_URI.concat("?reference=12345A&channel=CLIENT")))
                .andExpect(status().isOk());
    }

    @Test
    public void WhenTransactionIsStored_AndCheckStatusFromAtmOrClientChannel_AndDateIsBeforeToday_ThenStatusIsSettled_AndAmountIsSubstracted() throws Exception {
        final Transaction transaction = TransactionMockGenerator.transactionSettled();
        when(transactionService.checkStatus(anyString(), anyString())).thenReturn(Optional.of(transaction));

        final var apiResponse = mockMvc.perform(get(TRANSACTIONS_URI.concat("?reference=12345A&channel=ATM")))
                .andReturn();

        final String contentAsString = apiResponse.getResponse().getContentAsString();
        final GetTransactionStatusResponse getTransactionStatusResponse = objectMapper.readValue(contentAsString, GetTransactionStatusResponse.class);

        Assert.notNull(getTransactionStatusResponse, "Invalid result: system returned empty response");
        Assert.notNull(getTransactionStatusResponse.getReference(), "Invalid result: system returned empty transaction reference");
        Assert.notNull(getTransactionStatusResponse.getStatus(), "Invalid result: system returned empty transaction status");
        Assert.notNull(getTransactionStatusResponse.getAmount(), "Invalid result: system returned empty transaction amount");
        Assert.isTrue(Transaction.TransactionStatus.SETTLED.name().equals(getTransactionStatusResponse.getStatus()), "System returned invalid transaction status");
    }

    @Test
    public void WhenTransactionIsStored_AndCheckStatusFromCInternalChannel_AndDateIsBeforeToday_ThenStatusIsSettled_AndReturnsAmountAndFee() throws Exception {
        final Transaction transaction = TransactionMockGenerator.transactionSettled();
        when(transactionService.checkStatus(anyString(), anyString())).thenReturn(Optional.of(transaction));

        final var apiResponse = mockMvc.perform(get(TRANSACTIONS_URI.concat("?reference=12345A&channel=ATM")))
                .andReturn();

        final String contentAsString = apiResponse.getResponse().getContentAsString();
        final GetTransactionStatusResponse getTransactionStatusResponse = objectMapper.readValue(contentAsString, GetTransactionStatusResponse.class);

        Assert.notNull(getTransactionStatusResponse, "Invalid result: system returned empty response");
        Assert.notNull(getTransactionStatusResponse.getReference(), "Invalid result: system returned empty transaction reference");
        Assert.notNull(getTransactionStatusResponse.getStatus(), "Invalid result: system returned empty transaction status");
        Assert.notNull(getTransactionStatusResponse.getAmount(), "Invalid result: system returned empty transaction amount");
        Assert.notNull(getTransactionStatusResponse.getFee(), "Invalid result: system returned empty transaction fee");
        Assert.isTrue(Transaction.TransactionStatus.SETTLED.name().equals(getTransactionStatusResponse.getStatus()), "System returned invalid transaction status");
    }

    @Test
    public void WhenExistingIbanIsProvided_AndSortByAmountAsc_ThenSystemReturnsListOfTransactions() throws Exception {
        final var transaction1 = TransactionMockGenerator.transactionSettled();
        final var transaction2 = TransactionMockGenerator.transactionSettled();
        final var transactions = List.of(transaction2, transaction1);
        when(transactionService.findByAccount(anyString(), anyString())).thenReturn(Optional.of(transactions));

        final var apiResponse = mockMvc
                .perform(get(SEARCH_TRANSACTIONS_URI
                        .replace("{account_iban}", IbanMockGenerator.IBAN_SAMPLE_1)
                        .replace("{sort_by}", "amount:asc")))
                .andReturn();

        final String contentAsString = apiResponse.getResponse().getContentAsString();
        final SearchTransactionResponse searchTransactionResponse = objectMapper.readValue(contentAsString, SearchTransactionResponse.class);

        Assert.notNull(searchTransactionResponse, "Invalid result: system returned empty response");
        Assert.notNull(searchTransactionResponse.getTransactions(), "Invalid result: system returned no transactions");
        Assert.notEmpty(searchTransactionResponse.getTransactions(), "Invalid result: system returned no transactions");
        Assert.isTrue(searchTransactionResponse.getTransactions().size() == 2, "Invalid result: system returned an unexpected result");
        Assert.isTrue(searchTransactionResponse.getTransactions().get(0).getReference().equals(transaction2.getReference()), "Sort operation failed!");
        Assert.isTrue(searchTransactionResponse.getTransactions().get(0).getIban().equals(transaction2.getIban()), "Filter operation failed!");
        Assert.isTrue(searchTransactionResponse.getTransactions().get(1).getReference().equals(transaction1.getReference()), "Sort operation failed!");
        Assert.isTrue(searchTransactionResponse.getTransactions().get(1).getIban().equals(transaction1.getIban()), "Filter operation failed!");
    }

    @Test
    public void WhenExistingIbanIsProvided_AndSortByAmountDesc_ThenSystemReturnsListOfTransactions() throws Exception {
        final var transaction1 = TransactionMockGenerator.transactionSettled();
        final var transaction2 = TransactionMockGenerator.transactionSettled();
        final var transactions = List.of(transaction1, transaction2);
        when(transactionService.findByAccount(anyString(), anyString())).thenReturn(Optional.of(transactions));

        final var apiResponse = mockMvc
                .perform(get(SEARCH_TRANSACTIONS_URI
                        .replace("{account_iban}", IbanMockGenerator.IBAN_SAMPLE_1)
                        .replace("{sort_by}", "amount:desc")))
                .andReturn();

        final String contentAsString = apiResponse.getResponse().getContentAsString();
        final SearchTransactionResponse searchTransactionResponse = objectMapper.readValue(contentAsString, SearchTransactionResponse.class);

        Assert.notNull(searchTransactionResponse, "Invalid result: system returned empty response");
        Assert.notNull(searchTransactionResponse.getTransactions(), "Invalid result: system returned no transactions");
        Assert.notEmpty(searchTransactionResponse.getTransactions(), "Invalid result: system returned no transactions");
        Assert.isTrue(searchTransactionResponse.getTransactions().size() == 2, "Invalid result: system returned an unexpected result");
        Assert.isTrue(searchTransactionResponse.getTransactions().get(0).getReference().equals(transaction1.getReference()), "Sort operation failed!");
        Assert.isTrue(searchTransactionResponse.getTransactions().get(0).getIban().equals(transaction1.getIban()), "Filter operation failed!");
        Assert.isTrue(searchTransactionResponse.getTransactions().get(1).getReference().equals(transaction2.getReference()), "Sort operation failed!");
        Assert.isTrue(searchTransactionResponse.getTransactions().get(1).getIban().equals(transaction2.getIban()), "Filter operation failed!");
    }

    @Test
    public void WhenNonExistingIbanIsProvided_ThenSystemReturnsEmptyListOfTransactions() throws Exception {
        when(transactionService.findByAccount(anyString(), anyString())).thenReturn(Optional.empty());

       mockMvc.perform(get(SEARCH_TRANSACTIONS_URI
                            .replace("{account_iban}", IbanMockGenerator.IBAN_SAMPLE_1)
                            .replace("{sort_by}", "amount:desc")))
                .andExpect(status().isNotFound());
    }
}
