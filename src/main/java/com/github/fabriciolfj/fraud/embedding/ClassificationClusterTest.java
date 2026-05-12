package com.github.fabriciolfj.fraud.embedding;

import com.github.fabriciolfj.fraud.dto.ClusterableEmbeddedMessage;
import com.github.fabriciolfj.fraud.dto.News;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassificationClusterTest {

    private static final String REGEXP = "\"([^\"]*)\"";

    public static List<News> readNews() {
        Path file = Paths.get("src/main/resources/news-title.txt");
        List<News> news = new ArrayList<>();

        String content = null;
        try {
            content = Files.readString(file);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(content);

        while(matcher.find()) {
            String quoteString = matcher.group();
            news.add(new News(quoteString));
        }

        return news;
    }

    public static List<ClusterableEmbeddedMessage> calculate(List<News> newsList) {
        var cluster = new ArrayList<ClusterableEmbeddedMessage>();

        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("nomic-embed-text")
                .build();

        for(News news: newsList) {
            var content = embeddingModel.embed(news.title()).content();
            var clusterableEmbeddedMessage = new ClusterableEmbeddedMessage(
                    news,
                    content.vectorAsList()
                            .stream()
                            .mapToDouble(Float::doubleValue)
                            .toArray());

            cluster.add(clusterableEmbeddedMessage);
        }

        return cluster;
    }
}
