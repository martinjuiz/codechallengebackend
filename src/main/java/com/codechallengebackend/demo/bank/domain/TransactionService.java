package com.codechallengebackend.demo.bank.domain;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public interface TransactionService {

    default String generateReference() {
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }

        return salt.toString();
    }

    Transaction create(Transaction transaction);

    Optional<Transaction> checkStatus(String reference, String channel);

    Optional<List<Transaction>> findByAccount(String account, String sortBy);
}
