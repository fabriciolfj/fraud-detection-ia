package com.github.fabriciolfj.fraud;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ImageAnalyzer {

    @SystemMessage("""
            You are an expert software engineer.
            Analyze the provided screenshot and identify:
            - The error or issue visible
            - The category: PERSISTENCE, UI, EVENT or GENERIC
            - A short description of the problem
            """)
    @UserMessage(value = {
            "Analyze this screenshot and classify the programming issue.",
            "Image (base64): {{base64Image}}"
    })
    ImageAnalysisResult analyzeErrorImage(@V("base64Image") String base64Image);
}
