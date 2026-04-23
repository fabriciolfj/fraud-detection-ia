package com.github.fabriciolfj.fraud.models;

public record TransactionDetails(String txId,
                                 float distanceFomLastTransaction,
                                 float ratioToMedianPrice,
                                 boolean usedChip,
                                 boolean usePinNumber,
                                 boolean onlineOrder) { }
