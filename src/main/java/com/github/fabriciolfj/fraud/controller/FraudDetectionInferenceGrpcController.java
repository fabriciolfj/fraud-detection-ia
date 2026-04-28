package com.github.fabriciolfj.fraud.controller;

import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import com.github.fabriciolfj.fraud.models.FraudResponse;
import com.github.fabriciolfj.fraud.models.TransactionDetails;
import io.grpc.stub.StreamObserver;
import org.acme.stub.FraudDetectionGrpc;
import org.acme.stub.FraudRes;
import org.acme.stub.TxDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import java.util.function.Supplier;

@GrpcService
public class FraudDetectionInferenceGrpcController extends FraudDetectionGrpc.FraudDetectionImplBase {


    @Autowired
    private Supplier<Predictor<TransactionDetails, Boolean>> predictorSupplier;

    @Override
    public void predict(TxDetails request, StreamObserver<FraudRes> responseObserver) {
        final TransactionDetails td = new TransactionDetails(
                request.getTxId(),
                request.getDistanceFromLastTransaction(),
                request.getRatioToMedianPrice(),
                request.getUsedChip(),
                request.getUsedPinNumber(),
                request.getOnlineOrder()
        );

        try(var p = predictorSupplier.get()) {
            boolean fraud = p.predict(td);
            FraudRes fraudResponse = FraudRes.newBuilder()
                    .setTxId(td.txId())
                    .setFraud(fraud)
                    .build();

            responseObserver.onNext(fraudResponse);
            responseObserver.onCompleted();
        } catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }
}
