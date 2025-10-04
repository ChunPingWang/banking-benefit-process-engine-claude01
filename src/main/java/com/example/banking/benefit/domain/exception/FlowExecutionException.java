package com.example.banking.benefit.domain.exception;

/**
 * 流程執行異常的基礎類別
 */
public class FlowExecutionException extends RuntimeException {
    private final String flowId;
    private final String nodeId;

    public FlowExecutionException(String message) {
        this(message, null, null, null);
    }
    
    public FlowExecutionException(String message, Throwable cause) {
        this(message, null, null, cause);
    }
    
    public FlowExecutionException(String message, String flowId, String nodeId, Throwable cause) {
        super(message, cause);
        this.flowId = flowId;
        this.nodeId = nodeId;
    }

    public FlowExecutionException(String message, String flowId, String nodeId) {
        this(message, flowId, nodeId, null);
    }

    public String getFlowId() {
        return flowId;
    }

    public String getNodeId() {
        return nodeId;
    }
}