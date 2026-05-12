package com.github.fabriciolfj.fraud.dto;

import org.apache.commons.math3.ml.clustering.Clusterable;

public record ClusterableEmbeddedMessage(News news,
                                         double[] embedding) implements Clusterable {

    @Override
    public double[] getPoint() {
        return new double[0];
    }
}
