package com.example.banking.benefit.domain.model.decision;

/**
 * 決策節點的類型列舉
 */
public enum DecisionType {
    JAVA_CLASS("Java 類別"),
    SPEL("SpEL 表達式");

    private final String description;

    DecisionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}