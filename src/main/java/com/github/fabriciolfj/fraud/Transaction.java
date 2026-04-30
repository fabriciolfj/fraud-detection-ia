package com.github.fabriciolfj.fraud;

import dev.langchain4j.service.UserMessage;

public interface Transaction {

    @UserMessage("extract information about a transaction from {{it}}")
    TransactionInfo extract(final String message);
}
