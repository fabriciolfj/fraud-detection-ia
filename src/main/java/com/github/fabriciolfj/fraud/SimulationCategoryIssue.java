package com.github.fabriciolfj.fraud;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.output.JsonSchemas;

public class SimulationCategoryIssue {

    static void main() {

        ChatModel model = AnthropicChatModel
                .builder()
                .apiKey("")
                .modelName("claude-sonnet-4-5-20250929")
                .responseFormat(ResponseFormat.builder()
                        .type(ResponseFormatType.JSON)
                        .jsonSchema(JsonSchemas.jsonSchemaFrom(IssueClassification.class).get())
                        .build())
                .build();

        LabelDetector labelDetector = AiServices.builder(LabelDetector.class)
                .chatModel(model)
                .build();

        IssueClassification label = labelDetector
                .categorizeIssue("when storing a user in a database it thows an exception");

        IssueClassification label2 = labelDetector.categorizeIssue("jdbc exception");

        IO.println(label);
        IO.println(label2);


    }
}
