package com.github.fabriciolfj.fraud.models;

public record FraudResponse(String txId, boolean fraud) { }
