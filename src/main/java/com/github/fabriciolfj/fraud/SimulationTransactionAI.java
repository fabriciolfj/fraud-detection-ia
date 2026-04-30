package com.github.fabriciolfj.fraud;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

public class SimulationTransactionAI {

    static void main() {
        final ChatModel model = AnthropicChatModel.builder()
                .apiKey("")
                .modelName("claude-sonnet-4-5-20250929")
                .build();

        final Transaction transaction = AiServices.builder(Transaction.class)
                .chatModel(model)
                .build();

        IO.println(transaction.extract("meu nome fabricio, fiz uma transacao em 4 de julho 2025, para minha conta 188221," +
                "no valor de 25 reais"));

    }
}
