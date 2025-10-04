package com.example.banking.benefit.domain.model.common;

/**
 * 流程執行結果
 */
public class ExecutionResult {
    private final String flowId;
    private final String executionId;
    private final ExecutionStatus status;
    private final String message;
    private final Object data;

    public ExecutionResult(String flowId, String executionId, ExecutionStatus status, String message, Object data) {
        this.flowId = flowId;
        this.executionId = executionId;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getFlowId() {
        return flowId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    /**
     * 建立成功結果
     */
    public static ExecutionResult success(String flowId, String executionId, Object data) {
        return new ExecutionResult(flowId, executionId, ExecutionStatus.SUCCESS, "執行成功", data);
    }

    /**
     * 建立失敗結果
     */
    public static ExecutionResult failure(String flowId, String executionId, String message) {
        return new ExecutionResult(flowId, executionId, ExecutionStatus.FAILURE, message, null);
    }

    /**
     * 建立暫停結果
     */
    public static ExecutionResult paused(String flowId, String executionId) {
        return new ExecutionResult(flowId, executionId, ExecutionStatus.PAUSED, "流程已暫停", null);
    }

    /**
     * 建立中止結果
     */
    public static ExecutionResult terminated(String flowId, String executionId, String reason) {
        return new ExecutionResult(flowId, executionId, ExecutionStatus.TERMINATED, reason, null);
    }
}