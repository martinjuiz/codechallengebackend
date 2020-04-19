package com.codechallengebackend.demo.bank.application;

import com.codechallengebackend.demo.bank.domain.AccountService;
import com.codechallengebackend.demo.bank.model.GetAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<GetAccountResponse>> getAccounts() {
        List<GetAccountResponse> accounts = new ArrayList<>();
        final var allAccounts = accountService.findAll();
        allAccounts.forEach( account -> accounts.add( GetAccountResponse.of(account) ));

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
}
