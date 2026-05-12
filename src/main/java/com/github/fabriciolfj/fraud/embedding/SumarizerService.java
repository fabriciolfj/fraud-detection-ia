package com.github.fabriciolfj.fraud.embedding;

import dev.langchain4j.service.SystemMessage;

public interface SumarizerService {

    @SystemMessage("""
            Sumarize a lista de noticias.
            Nao foi dada a sentença completa delas.
            Apenas de o topic direto em 7 palavras ou menos
            """)
    String sumarize(String appendedMessages);
}
