package com.codechallengebackend.demo.bank.adapter;

import com.codechallengebackend.demo.bank.domain.Account;
import com.codechallengebackend.demo.bank.domain.AccountService;
import com.codechallengebackend.demo.bank.exception.IBANNotFoundException;
import com.codechallengebackend.demo.bank.exception.InsufficientAccountBalanceException;
import com.codechallengebackend.demo.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Override
    public void canBeDebited(String iban, Double transactionAmount) {
        final var account = accountRepository.findByIBAN(iban);

        if(account.isEmpty()) {
            throw new IBANNotFoundException("IBAN account not found!");
        }

        account.ifPresent( acc -> {
            if(transactionAmount < 0 && acc.getBalance() < Math.abs(transactionAmount)) {
                throw new InsufficientAccountBalanceException("Account does not have enough founds to accept the transaction");
            }
        });
    }

    @Override
    public void updateBalance(String iban, Double transactionAmount) {
        final var account = accountRepository.findByIBAN(iban);

        if(account.isEmpty()) {
            throw new IBANNotFoundException("IBAN account not found!");
        }

        account.ifPresent( acc -> {
            // Is a debit operation and account does not have enough founds to cover the transaction
            if(transactionAmount < 0 && acc.getBalance() < Math.abs(transactionAmount)) {
                throw new InsufficientAccountBalanceException("Account does not have enough founds to accept the transaction");
            }

            Double balanceUpdated;
            if(transactionAmount < 0) {
                balanceUpdated = acc.getBalance() - Math.abs(transactionAmount);
            }
            else {
                balanceUpdated = acc.getBalance() + transactionAmount;
            }

            BigDecimal bd = new BigDecimal(balanceUpdated).setScale(2, RoundingMode.HALF_UP);
            acc.setBalance(bd.doubleValue());

            accountRepository.updateBalance(acc);
        });
    }
}
