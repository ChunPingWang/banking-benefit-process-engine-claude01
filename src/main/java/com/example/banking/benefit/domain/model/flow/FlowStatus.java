package com.example.banking.benefit.domain.model.flow;

/**
 * Flow 的狀態列舉
 */
public enum FlowStatus {
    DRAFT("草稿"),
    ACTIVE("啟用"),
    INACTIVE("停用");

    private final String description;

    FlowStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}