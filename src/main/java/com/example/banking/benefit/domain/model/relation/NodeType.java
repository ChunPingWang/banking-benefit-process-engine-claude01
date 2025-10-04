package com.example.banking.benefit.domain.model.relation;

/**
 * 節點類型列舉
 */
public enum NodeType {
    DECISION("決策節點"),
    PROCESS("處理節點");

    private final String description;

    NodeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}