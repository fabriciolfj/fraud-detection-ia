package com.github.fabriciolfj.fraud.model;

import java.time.LocalDate;

public record TransactionInfo(String fullName, String iban, LocalDate transactionDate, double amount) {
}
