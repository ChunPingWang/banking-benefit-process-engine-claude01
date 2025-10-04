package com.example.banking.benefit.domain.model.result;

/**
 * 流程執行狀態
 */
public enum ExecutionStatus {
    SUCCESS,
    FAILURE,
    PAUSED,
    TERMINATED,
    IN_PROGRESS;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}