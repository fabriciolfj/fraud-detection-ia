package com.github.fabriciolfj.fraud.embedding;

import com.github.fabriciolfj.fraud.dto.ClusterableEmbeddedMessage;
import com.github.fabriciolfj.fraud.dto.News;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.anthropic.AnthropicChatModelName;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.service.AiServices;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassificationClusterTest {

    private static final String REGEXP = "\"([^\"]*)\"";

    private static final double MAXIMUM_NEIGHBORHOOD_RAIUS = 0.9;
    private static final int MINIMUM_POINTS_PER_CLUSTER = 6;

    static void main() {
        DBSCANClusterer<ClusterableEmbeddedMessage> clusterer =
                new DBSCANClusterer<>(MAXIMUM_NEIGHBORHOOD_RAIUS, MINIMUM_POINTS_PER_CLUSTER);

        final List<ClusterableEmbeddedMessage> points = calculate(readNews());
        final List<? extends Cluster<ClusterableEmbeddedMessage>> clusters = clusterer.cluster(points);

        IO.println(createSummarize(clusters));
    }

    public static String createSummarize(List<? extends Cluster<ClusterableEmbeddedMessage>> clusters) {
        var key = System.getenv("API_KEY");
        ChatModel model = AnthropicChatModel
                .builder()
                .apiKey(key)
                .modelName(AnthropicChatModelName.CLAUDE_SONNET_4_6)
                .build();

        SumarizerService sumarizerService = AiServices.create(SumarizerService.class, model);

        StringBuilder dataTemplate = new StringBuilder();

         for(final Cluster<ClusterableEmbeddedMessage> cluster: clusters) {
             List<ClusterableEmbeddedMessage> clusterPoints = cluster.getPoints();

             String appendedTiles = clusterPoints
                     .stream()
                     .map(c -> c.news().title())
                     .collect(Collectors.joining("\n"));

             String clusterSummary = sumarizerService.sumarize(appendedTiles);

             dataTemplate
                     .append("{name: \"")
                     .append(clusterSummary.replace("\"", "\\\"")
                             .replace("\n", " "))
                     .append("\", value: ")
                     .append(clusterPoints.size())
                     .append("},\n");
         }

        return dataTemplate.toString().trim();
    }


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
