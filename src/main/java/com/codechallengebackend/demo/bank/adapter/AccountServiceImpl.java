package com.codechallengebackend.demo.bank.adapter;

import com.codechallengebackend.demo.bank.domain.Account;
import com.codechallengebackend.demo.bank.domain.AccountService;
import com.codechallengebackend.demo.bank.repository.AccountRepository;

import java.util.List;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
