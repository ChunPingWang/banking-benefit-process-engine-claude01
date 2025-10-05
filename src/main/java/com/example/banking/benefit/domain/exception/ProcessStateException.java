package com.example.banking.benefit.domain.exception;

/**
 * 流程狀態處理相關例外
 */
public class ProcessStateException extends FlowExecutionException {
    
    public static final String ERROR_CODE_STATE_NOT_FOUND = "STATE_001";
    public static final String ERROR_CODE_INVALID_STATE = "STATE_002";
    public static final String ERROR_CODE_TRANSITION_ERROR = "STATE_003";
    public static final String ERROR_CODE_STATE_EXECUTION_ERROR = "STATE_004";

    public ProcessStateException(String message, String flowId, String nodeId) {
        super(message, flowId, nodeId);
    }

    public ProcessStateException(String message, String flowId, String nodeId, Throwable cause) {
        super(message, flowId, nodeId, cause);
    }

    public static ProcessStateException stateNotFound(String flowId, String nodeId, String stateId) {
        return new ProcessStateException(
            String.format("找不到狀態: %s", stateId),
            flowId,
            nodeId
        );
    }

    public static ProcessStateException invalidState(String flowId, String nodeId, String reason) {
        return new ProcessStateException(
            String.format("無效的狀態: %s", reason),
            flowId,
            nodeId
        );
    }

    public static ProcessStateException transitionError(String flowId, String nodeId, String fromState, String toState, String reason) {
        return new ProcessStateException(
            String.format("狀態轉換錯誤 %s -> %s: %s", fromState, toState, reason),
            flowId,
            nodeId
        );
    }

    public static ProcessStateException stateExecutionError(String flowId, String nodeId, String state, String message, Throwable cause) {
        return new ProcessStateException(
            String.format("狀態執行錯誤 %s: %s", state, message),
            flowId,
            nodeId,
            cause
        );
    }
}