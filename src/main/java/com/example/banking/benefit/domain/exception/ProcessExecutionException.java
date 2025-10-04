package com.example.banking.benefit.domain.exception;

/**
 * 處理節點執行異常
 */
public class ProcessExecutionException extends FlowExecutionException {
    public ProcessExecutionException(String message, String flowId, String nodeId, Throwable cause) {
        super(message, flowId, nodeId, cause);
    }

    public ProcessExecutionException(String message, String flowId, String nodeId) {
        super(message, flowId, nodeId);
    }
    
    public ProcessExecutionException(String message) {
        super(message, null, null);
    }
    
    public ProcessExecutionException(String message, Throwable cause) {
        super(message, null, null, cause);
    }
}