package com.example.banking.benefit.domain.model.common;

/**
 * 流程執行狀態
 */
public enum ExecutionStatus {
    SUCCESS("成功"),
    FAILURE("失敗"),
    PAUSED("暫停"),
    TERMINATED("中止"),
    IN_PROGRESS("執行中");

    private final String description;

    ExecutionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}