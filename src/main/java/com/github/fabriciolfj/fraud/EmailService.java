package com.github.fabriciolfj.fraud;

import dev.langchain4j.agent.tool.Tool;

public class EmailService {

    @Tool("send given content by email")
    public void sendEmail(final String content) {

    }
}
