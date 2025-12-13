package com.example.cashcard.cashcard.service;

import java.util.UUID;

import com.example.cashcard.cashcard.domain.ApprovalStatus;
import com.example.cashcard.cashcard.domain.CardHolderData;
import com.example.cashcard.cashcard.domain.EnrichedTransaction;
import com.example.cashcard.cashcard.domain.Transaction;

public class EnrichmentService {
    public EnrichedTransaction enrichTransaction(Transaction transaction)
    {
        return new EnrichedTransaction(
            transaction.id(),
            transaction.cashCard(),
            ApprovalStatus.APPROVED,
            new CardHolderData(
                UUID.randomUUID(),
                transaction.cashCard().owner(),
                "123 Main Street"
            )
        );
    }
}
