package com.github.fabriciolfj.fraud.embedding;

import com.github.fabriciolfj.fraud.enums.SentimentCategory;
import dev.langchain4j.classification.EmbeddingModelTextClassifier;
import dev.langchain4j.classification.TextClassifier;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassificationOllamaEmbeddingTest {

    static Map<SentimentCategory, List<String>> examples = new HashMap<>();

    static {
        examples.put(SentimentCategory.ANGER, List.of(
                "Estou completamente furioso com essa situação!",
                "Não aguento mais essa incompetência!",
                "Isso é inadmissível, estou com raiva!",
                "Me deixa em paz, estou com raiva!"
        ));

        examples.put(SentimentCategory.JOY, List.of(
                "Estou tão feliz, foi o melhor dia da minha vida!",
                "Que notícia incrível, estou radiante!",
                "Adoro esse momento, me sinto ótimo!",
                "Estou comemorando, consegui o que queria!"
        ));

        examples.put(SentimentCategory.DISGUST, List.of(
                "Que nojo, não consigo nem olhar pra isso!",
                "Isso é repugnante, me dá asco!",
                "Que coisa mais horrível e asquerosa!",
                "Sinto repulsa só de pensar nisso!"
        ));

        examples.put(SentimentCategory.FEAR, List.of(
                "Estou apavorado, não sei o que vai acontecer!",
                "Tenho muito medo dessa situação!",
                "Sinto um frio na barriga de tanto terror!",
                "Estou tremendo de medo agora!"
        ));

        examples.put(SentimentCategory.SADNESS, List.of(
                "Estou muito triste, não consigo parar de chorar!",
                "Sinto um vazio enorme no peito!",
                "Nada faz sentido, estou completamente abatido!",
                "Perdi algo importante e a dor é grande!"
        ));

        examples.put(SentimentCategory.ENVY, List.of(
                "Por que ele conseguiu e eu não? Não é justo!",
                "Queria muito ter o que ela tem!",
                "Fico com raiva de ver o sucesso dos outros!",
                "Por que tudo parece ser fácil para ele?"
        ));

        examples.put(SentimentCategory.ENNUI, List.of(
                "Que tédio, nada me anima mais!",
                "Tudo parece monótono e sem graça!",
                "Não tenho vontade de fazer nada, que mesmice!",
                "Estou entediado demais, a vida está sem sentido!"
        ));

        examples.put(SentimentCategory.EMBARRASSMENT, List.of(
                "Que vergonha, queria sumir nesse momento!",
                "Fiquei vermelho de tanto constrangimento!",
                "Nunca me senti tão envergonhado na vida!",
                "Que situação humilhante, não sei onde me enfiar!"
        ));
    }

    static void main() {
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("nomic-embed-text")
                .build();

        TextClassifier<SentimentCategory> classifier = new EmbeddingModelTextClassifier<>(embeddingModel, examples);
        IO.println(classifier.classify("nao quero esperar"));
        IO.println(classifier.classifyWithScores("nao quero esperar"));
    }
}

