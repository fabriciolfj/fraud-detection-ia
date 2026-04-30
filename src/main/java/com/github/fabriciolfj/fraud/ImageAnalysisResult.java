package com.github.fabriciolfj.fraud;

public record ImageAnalysisResult(
        String category,
        String errorDetected,
        String description,
        String suggestedFix
) {}