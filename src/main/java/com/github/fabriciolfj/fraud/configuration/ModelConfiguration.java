package com.github.fabriciolfj.fraud.configuration;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import com.github.fabriciolfj.fraud.models.TransactionDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.function.Supplier;

@Configuration
public class ModelConfiguration {

    @Bean
    public Criteria<TransactionDetails, Boolean> criteria() {
        String modelLocation = Thread.currentThread().getContextClassLoader().getResource("model.onnx")
                .toExternalForm();

        return Criteria.builder()
                .setTypes(TransactionDetails.class, Boolean.class)
                .optModelUrls(modelLocation)
                .optTranslator(new TransactionTransformer(5.0f))
                .optEngine("OnnxRuntime")
                .build();
    }

    @Bean
    public ZooModel<TransactionDetails, Boolean> model(@Qualifier("criteria") final Criteria<TransactionDetails, Boolean> criteria) throws ModelNotFoundException, MalformedModelException, IOException {
        return criteria.loadModel();
    }

    @Bean
    public Supplier<Predictor<TransactionDetails, Boolean>> predictorSuplier(ZooModel<TransactionDetails, Boolean> model) {
        return model::newPredictor;
    }
}
