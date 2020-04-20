package com.codechallengebackend.demo.bank.application;

import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.exception.NoTransactionsFoundException;
import com.codechallengebackend.demo.bank.model.*;
import com.codechallengebackend.demo.bank.model.validation.IBANValidated;
import com.codechallengebackend.demo.bank.model.validation.SortClauseValidated;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionService transactionService;

    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping(path = "searching", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchTransactionResponse> searchTransactions(@IBANValidated @RequestParam("account_iban") String account,
                                                                        @SortClauseValidated @RequestParam("sort") String sortBy) {

        final List<Transaction> transactions = transactionService.findByAccount(account, sortBy)
                .orElseThrow(NoTransactionsFoundException::new);

        List<TransactionResponse> listTransactions = new ArrayList<>();
        transactions.forEach(transaction -> {
            TransactionResponse transactionResponse = new TransactionResponse(
                    transaction.getReference(), transaction.getIban(), transaction.getDate(),
                    transaction.getAmount(), transaction.getFee(), transaction.getDescription()
            );

            listTransactions.add(transactionResponse);
        });

        SearchTransactionResponse response = new SearchTransactionResponse(listTransactions);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetTransactionStatusResponse> checkTransactionStatus(@RequestParam("reference") String reference,
                                                                               @RequestParam("channel")   String channel) {

        final GetTransactionStatusResponse response = transactionService.checkStatus(reference, channel)
                .map(transaction -> new GetTransactionStatusResponse(transaction.getReference(),
                        transaction.getStatus().name(),
                        transaction.getAmount(), transaction.getFee()))
                .orElseGet(() -> new GetTransactionStatusResponse(reference, Transaction.TransactionStatus.INVALID.name()));

        if(Transaction.TransactionStatus.INVALID.name().equals(response.getStatus())) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateTransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest body) {

        Transaction transaction = new Transaction(body.getReference(), body.getIban(),
                body.getDate(), body.getAmount(), body.getFee(),
                body.getDescription());

        final Transaction created = transactionService.create(transaction);

        CreateTransactionResponse response = new CreateTransactionResponse(created.getReference());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
