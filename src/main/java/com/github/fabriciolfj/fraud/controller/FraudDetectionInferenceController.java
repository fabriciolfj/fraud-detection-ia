package com.github.fabriciolfj.fraud.controller;

import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import com.github.fabriciolfj.fraud.models.FraudResponse;
import com.github.fabriciolfj.fraud.models.TransactionDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
@RequestMapping("/inference")
public class FraudDetectionInferenceController {

    public final Supplier<Predictor<TransactionDetails, Boolean>> predictorSupplier;

    public FraudDetectionInferenceController(Supplier<Predictor<TransactionDetails, Boolean>> predictorSupplier) {
        this.predictorSupplier = predictorSupplier;
    }

    @PostMapping
    public FraudResponse detectFraud(@RequestBody final TransactionDetails transactionDetails) throws TranslateException {
        try(var p = predictorSupplier.get()) {
            boolean isFraud = p.predict(transactionDetails);
            return new FraudResponse(transactionDetails.txId(), isFraud);
        }
    }
}

