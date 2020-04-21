package com.codechallengebackend.demo.bank.mock;

import com.codechallengebackend.demo.bank.model.CreateTransactionRequest;

import static com.codechallengebackend.demo.bank.mock.IbanMockGenerator.IBAN_SAMPLE_1;

public class CreateTransactionRequestMockGenerator {

    public static CreateTransactionRequest transactionWithoutReferenceOk() {
        return new CreateTransactionRequest(
                "", IBAN_SAMPLE_1, "", 100D, 0D, "Card payment"
        );
    }
}
