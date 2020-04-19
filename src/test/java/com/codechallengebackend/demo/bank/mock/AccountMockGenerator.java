package com.codechallengebackend.demo.bank.mock;

import com.codechallengebackend.demo.bank.domain.Account;

import java.util.List;
import java.util.UUID;

public class AccountMockGenerator {

    public static Account account(String id) {
        return new Account(
                id,
                "",
                "",
                100D, 100D,
                1000D, 0D
        );
    }

    public static List<Account> listAccounts() {
        return List.of(
                account(UUID.randomUUID().toString()),
                account(UUID.randomUUID().toString()));
    }
}
