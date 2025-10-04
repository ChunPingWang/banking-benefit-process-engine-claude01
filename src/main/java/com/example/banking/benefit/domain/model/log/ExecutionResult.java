package com.example.banking.benefit.domain.model.log;

/**
 * 執行結果列舉
 */
public enum ExecutionResult {
    PASS("通過"),
    FAIL("失敗"),
    SUCCESS("成功"),
    ERROR("錯誤");

    private final String description;

    ExecutionResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}