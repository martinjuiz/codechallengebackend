package com.codechallengebackend.demo.bank.application;

import com.codechallengebackend.demo.bank.domain.Transaction;
import com.codechallengebackend.demo.bank.domain.TransactionService;
import com.codechallengebackend.demo.bank.model.CreateTransactionRequest;
import com.codechallengebackend.demo.bank.model.CreateTransactionResponse;
import com.codechallengebackend.demo.bank.model.GetTransactionStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionService transactionService;

    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetTransactionStatusResponse> createTransaction(@RequestParam("reference") String reference,
                                                                          @RequestParam("channel")   String channel) {

        final GetTransactionStatusResponse response = transactionService.checkStatus(reference)
                .map(transaction -> new GetTransactionStatusResponse(transaction.getReference(), transaction.getStatus().name()))
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
