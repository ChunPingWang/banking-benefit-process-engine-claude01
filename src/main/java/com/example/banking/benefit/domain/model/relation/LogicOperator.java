package com.example.banking.benefit.domain.model.relation;

/**
 * 邏輯運算子列舉
 */
public enum LogicOperator {
    AND("且"),
    OR("或");

    private final String description;

    LogicOperator(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}