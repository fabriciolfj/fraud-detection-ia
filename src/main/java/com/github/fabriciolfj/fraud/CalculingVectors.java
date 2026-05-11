package com.github.fabriciolfj.fraud;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel;

import java.util.Arrays;

public class CalculingVectors {

    static void main() {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

        var responseCar = embeddingModel.embed("car");
        var responseCat = embeddingModel.embed("cat");
        var responseKitten = embeddingModel.embed("kitten");

        float[] carVector = responseCar.content().vector();
        float[] catVector = responseCat.content().vector();
        float[] kittenVector = responseKitten.content().vector();

        IO.println(Arrays.toString(carVector));
        IO.println(Arrays.toString(catVector));
        IO.println(Arrays.toString(kittenVector));
    }
}
