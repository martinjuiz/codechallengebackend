package com.codechallengebackend.demo.bank.controller;

import com.codechallengebackend.demo.bank.application.AccountController;
import com.codechallengebackend.demo.bank.application.ExceptionController;
import com.codechallengebackend.demo.bank.config.TestBeanConfiguration;
import com.codechallengebackend.demo.bank.domain.Account;
import com.codechallengebackend.demo.bank.domain.AccountService;
import com.codechallengebackend.demo.bank.mock.AccountMockGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(TestBeanConfiguration.class)
public class AccountControllerTest {

    private static final String ACCOUNTS_URI = "/accounts";

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .setControllerAdvice(new ExceptionController(objectMapper))
                .build();
    }

    @Test
    public void WhenRequestIsSent_ThenSystemReturnsAccount() throws Exception {
        List<Account> accounts = AccountMockGenerator.listAccounts();
        when(accountService.findAll()).thenReturn(accounts);

        mockMvc.perform(get(ACCOUNTS_URI))
                .andExpect(status().isOk());
    }
}
