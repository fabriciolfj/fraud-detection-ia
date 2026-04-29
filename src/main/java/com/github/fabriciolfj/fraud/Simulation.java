package com.github.fabriciolfj.fraud;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.AugmentationRequest;
import dev.langchain4j.rag.AugmentationResult;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.query.Metadata;

public class Simulation {

    static void main() {
        ChatModel model = AnthropicChatModel.builder()
                .apiKey("")
                .modelName("claude-sonnet-4-5-20250929")
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();

        UserMessage userMessage = UserMessage.userMessage("ola");
        Metadata metadata = Metadata.from(userMessage, null, null);
        DefaultRetrievalAugmentor rArgumentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(new WeatherContentRetriever())
                .build();

        AugmentationRequest aRequest = new AugmentationRequest(userMessage, metadata);
        AugmentationResult aResult = rArgumentor.augment(aRequest);

        model.chat(aResult.chatMessage());

        chatMemory.add(userMessage);

        final ChatResponse response = model.chat(chatMemory.messages());
        chatMemory.add(response.aiMessage());

        IO.println(response.aiMessage().text());

    }
}
