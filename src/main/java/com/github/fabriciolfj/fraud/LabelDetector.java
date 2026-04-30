package com.github.fabriciolfj.fraud;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage("you are a bot in charge of categorizing issues from a bug tracker")
public interface LabelDetector {


    @UserMessage("""
            You are an expert software engineer specialized in categorizing programming problems.
            Analyze the given problem and classify it into exactly one of the following categories:
            
            - PERSISTENCE: problems related to database, jdbc, queries, ORM, migrations, data storage or retrieval
            - UI: problems related to frontend, components, layout, styling, user interaction or rendering
            - EVENT: problems related to messaging, queues, event-driven architecture, pub/sub, Kafka, RabbitMQ or async processing
            - GENERIC: problems that do not clearly fit into the above categories
            
            Rules:
            - Respond with only the category name in uppercase
            - Do not include explanations or extra text
            - If uncertain, prefer GENERIC
            
            INPUT {{issueTitle}}
            """)
    IssueClassification categorizeIssue(@V("issueTitle") String issueTitle);
}
