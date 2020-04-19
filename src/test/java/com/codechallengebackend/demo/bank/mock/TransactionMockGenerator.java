package com.codechallengebackend.demo.bank.mock;

import com.codechallengebackend.demo.bank.domain.Transaction;

import static com.codechallengebackend.demo.bank.mock.IbanMockGenerator.IBAN_SAMPLE_1;

public class TransactionMockGenerator {


    public static Transaction transactionWithReference() {
        return new Transaction(
                "reference_001",
                IBAN_SAMPLE_1,
                "2019-07-16T16:55:42.000Z",
                100.20D,
                1.3D,
                Transaction.TransactionStatus.PENDING,
                "Card payment");
    }

    public static Transaction transactionWithEmptyReference() {
        return new Transaction(
                "",
                IBAN_SAMPLE_1,
                "2019-07-16T16:55:42.000Z",
                100.20D,
                1.3D,
                Transaction.TransactionStatus.PENDING,
                "Card payment");
    }

    public static Transaction transactionWithoutOptionalFields() {
        return new Transaction(
                "",
                IBAN_SAMPLE_1,
                "",
                100.20D,
                1.3D,
                Transaction.TransactionStatus.PENDING,
                "");
    }

    public static Transaction transactionWithInvalidReference() {
        return new Transaction(
                "ref...with...invalid___?format",
                IBAN_SAMPLE_1,
                "",
                100.20D,
                1.3D,
                Transaction.TransactionStatus.PENDING,
                "");
    }

    public static Transaction transactionWithInvalidIban() {
        return new Transaction(
                "ref1234",
                "ES9720385778983000760236",
                "2019-07-16T16:55:42.000Z",
                100.20D,
                1.3D,
                Transaction.TransactionStatus.PENDING,
                "Some transaction");
    }
}
