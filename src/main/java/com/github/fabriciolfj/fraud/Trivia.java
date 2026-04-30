package com.github.fabriciolfj.fraud;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface Trivia {

    @SystemMessage("return the capital of given country, respond only the city")
    @UserMessage("What is the capital of {{country}}")
    String question(@V("country") String country);
}
