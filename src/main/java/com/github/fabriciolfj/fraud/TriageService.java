package com.github.fabriciolfj.fraud;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface TriageService {

    @SystemMessage(
            "Analise se o sentimento expresso no texto"
    )
    @UserMessage("""
            Sua tarefa e analisar a mensagem e classificar como positiva, negativa ou neutra
            
            ---
            {{review}}
            ---
            """)
    Evaluation triage(final String review);
}
