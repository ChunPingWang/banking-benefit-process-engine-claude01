package com.example.banking.benefit.domain.model.result;

import com.example.banking.benefit.domain.model.flow.FlowId;
import java.util.Map;
import java.util.HashMap;

/**
 * 執行結果值物件
 */
public class ExecutionResult {
    private final FlowId flowId;
    private final String executionId;
    private final ExecutionStatus status;
    private final String message;
    private final Map<String, Object> variables;
    
    private ExecutionResult(FlowId flowId, String executionId, ExecutionStatus status, String message, Map<String, Object> variables) {
        this.flowId = flowId;
        this.executionId = executionId;
        this.status = status;
        this.message = message;
        this.variables = variables != null ? new HashMap<>(variables) : new HashMap<>();
    }
    
    public static ExecutionResult success(FlowId flowId, String executionId, Map<String, Object> variables) {
        return new ExecutionResult(flowId, executionId, ExecutionStatus.SUCCESS, null, variables);
    }
    
    public static ExecutionResult failure(FlowId flowId, String executionId, String message) {
        return new ExecutionResult(flowId, executionId, ExecutionStatus.FAILURE, message, null);
    }
    
    public static ExecutionResult paused(FlowId flowId, String executionId) {
        return new ExecutionResult(flowId, executionId, ExecutionStatus.PAUSED, null, null);
    }
    
    public static ExecutionResult terminated(FlowId flowId, String executionId, String message) {
        return new ExecutionResult(flowId, executionId, ExecutionStatus.TERMINATED, message, null);
    }
    
    public FlowId getFlowId() {
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
    
    public Map<String, Object> getVariables() {
        return new HashMap<>(variables);
    }
}

