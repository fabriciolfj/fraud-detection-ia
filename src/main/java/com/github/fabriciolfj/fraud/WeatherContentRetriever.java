package com.github.fabriciolfj.fraud;

import com.google.gson.JsonObject;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;

import java.util.List;

public class WeatherContentRetriever implements ContentRetriever {

    @Override
    public List<Content> retrieve(Query query) {
        return List.of();
    }
}
