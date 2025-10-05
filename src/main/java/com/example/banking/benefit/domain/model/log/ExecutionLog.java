package com.example.banking.benefit.domain.model.log;

import com.example.banking.benefit.domain.model.node.NodeType;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 執行日誌實體
 * 記錄流程執行的每個步驟及其結果
 */
public class ExecutionLog {
    private String logId;
    private String flowId;
    private String customerId;
    private LocalDateTime executionTime;
    private String nodeId;
    private NodeType nodeType;
    private ExecutionResult executionResult;
    private String resultData;
    private String errorMessage;
    private Integer executionDurationMs;

    private ExecutionLog(
            String flowId,
            String customerId,
            String nodeId,
            NodeType nodeType,
            ExecutionResult executionResult
    ) {
        this.logId = UUID.randomUUID().toString();
        this.flowId = flowId;
        this.customerId = customerId;
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.executionResult = executionResult;
        this.executionTime = LocalDateTime.now();
    }

    public static ExecutionLog create(
            String flowId,
            String customerId,
            String nodeId,
            NodeType nodeType,
            ExecutionResult executionResult
    ) {
        if (flowId == null || flowId.trim().isEmpty()) {
            throw new IllegalArgumentException("flowId must not be null or empty");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("customerId must not be null or empty");
        }
        if (nodeId == null || nodeId.trim().isEmpty()) {
            throw new IllegalArgumentException("nodeId must not be null or empty");
        }
        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType must not be null");
        }
        if (executionResult == null) {
            throw new IllegalArgumentException("executionResult must not be null");
        }
        return new ExecutionLog(flowId, customerId, nodeId, nodeType, executionResult);
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setExecutionDuration(int durationMs) {
        if (durationMs < 0) {
            throw new IllegalArgumentException("Duration must not be negative");
        }
        this.executionDurationMs = durationMs;
    }

    // Getters
    public String getLogId() { return logId; }
    public String getFlowId() { return flowId; }
    public String getCustomerId() { return customerId; }
    public LocalDateTime getExecutionTime() { return executionTime; }
    public String getNodeId() { return nodeId; }
    public NodeType getNodeType() { return nodeType; }
    public ExecutionResult getExecutionResult() { return executionResult; }
    public String getResultData() { return resultData; }
    public String getErrorMessage() { return errorMessage; }
    public Integer getExecutionDurationMs() { return executionDurationMs; }
}