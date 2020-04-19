package com.codechallengebackend.demo.bank.controller;

import com.codechallengebackend.demo.bank.application.TransactionsController;
import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.mock.CreateTransactionRequestMockGenerator;
import com.codechallengebackend.demo.bank.mock.TransactionMockGenerator;
import com.codechallengebackend.demo.bank.model.CreateTransactionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(TestBeanConfiguration.class)
public class TransactionsControllerTest {

    private static final String TRANSACTIONS_URI = "/transactions";

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionsController transactionsController;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(transactionsController)
//                .setControllerAdvice(new ExceptionController(objectMapper))
                .build();
    }

    @Test
    public void WhenTransactionHasNoReference_ThenSystemGeneratesOne() throws Exception {
        CreateTransactionRequest request = CreateTransactionRequestMockGenerator.transactionWithoutReferenceOk();

        final Transaction created = TransactionMockGenerator.transactionWithReference();
        when(transactionService.create(any(Transaction.class))).thenReturn(created);

        final String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(TRANSACTIONS_URI)
                .accept(APPLICATION_JSON_VALUE)
                .content(jsonRequest)
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void WhenTransactionIsNotStored_AndCheckStatus_ThenStatusIsInvalid() throws Exception {
        when(transactionService.checkStatus(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get(TRANSACTIONS_URI.concat("?reference=12345A&channel=CLIENT")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void WhenTransactionIsStored_AndCheckStatusFromAtmChannel_AndDateIsBeforeToday_ThenStatusIsSettled_AndAmountIsSubstracted() {

    }

    @Test
    public void WhenTransactionIsStored_AndCheckStatusFromClientChannel_AndDateIsBeforeToday_ThenStatusIsSettled_AndAmountIsSubstracted() {

    }
}
