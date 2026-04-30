package com.github.fabriciolfj.fraud;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Collections;

public class SimulationInterfaceIA {

    static void main() {
        ChatModel model = AnthropicChatModel.builder()
                .apiKey("")
                .modelName("claude-sonnet-4-5-20250929")
                .build();

        //Trivia trivia = AiServices.create(Trivia.class, model);
        Trivia trivia = AiServices.builder(Trivia.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(new WeatherContentRetriever())
                .tools(Collections.singleton(new EmailService()))
                .build();

        final String result = trivia.question("Japan");

        IO.println(result);

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();


        IO.println(assistant.chat(1, "hello"));
    }
}
