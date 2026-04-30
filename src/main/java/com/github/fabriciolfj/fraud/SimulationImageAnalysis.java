package com.github.fabriciolfj.fraud;


import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class SimulationImageAnalysis {

    static void main() throws Exception {

        ChatModel model = AnthropicChatModel
                .builder()
                .apiKey("")
                .modelName("claude-sonnet-4-5-20250929")
                .build();

        ImageAnalyzer analyzer = AiServices.builder(ImageAnalyzer.class)
                .chatModel(model)
                .build();

        // carrega a imagem e converte para base64
        byte[] imageBytes = Files.readAllBytes(Path.of("src/main/resources/img_1.png"));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        ImageAnalysisResult result = analyzer.analyzeErrorImage(base64Image);

        IO.println(result);
    }
}