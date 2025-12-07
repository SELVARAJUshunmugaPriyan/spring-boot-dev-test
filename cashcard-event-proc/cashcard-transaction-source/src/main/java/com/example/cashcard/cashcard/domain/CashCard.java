package com.example.cashcard.cashcard.domain;

public record CashCard(
    Long id,
    String owner,
    Double amountRequestedForAuth
) {}
