package com.codechallengebackend.demo.bank.repository;

import com.codechallengebackend.demo.bank.domain.Account;

import java.util.List;

public interface AccountRepository {

    List<Account> findAll();
}
