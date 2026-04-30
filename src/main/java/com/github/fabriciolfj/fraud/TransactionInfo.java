package com.github.fabriciolfj.fraud;

import dev.langchain4j.model.output.structured.Description;

import java.time.LocalDate;

public record TransactionInfo(
        @Description("full name") String name,
        @Description("IBAN value") String iban,
        @Description("Date of the transaction") LocalDate transactionDate,
        @Description("Amount in dollars of the transaction") double amount
        ) {
}
