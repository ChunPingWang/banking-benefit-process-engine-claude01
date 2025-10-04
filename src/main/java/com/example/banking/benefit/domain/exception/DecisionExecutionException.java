package com.example.banking.benefit.domain.exception;

/**
 * 決策執行異常
 */
public class DecisionExecutionException extends FlowExecutionException {
    public DecisionExecutionException(String message, String flowId, String nodeId, Throwable cause) {
        super(message, flowId, nodeId, cause);
    }

    public DecisionExecutionException(String message, String flowId, String nodeId) {
        super(message, flowId, nodeId);
    }
}