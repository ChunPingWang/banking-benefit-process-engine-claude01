package com.example.banking.benefit.domain.model.process;

/**
 * 處理節點的類型列舉
 */
public enum ProcessType {
    JAVA_CLASS("Java 類別"),
    SPEL("SpEL 表達式");

    private final String description;

    ProcessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}