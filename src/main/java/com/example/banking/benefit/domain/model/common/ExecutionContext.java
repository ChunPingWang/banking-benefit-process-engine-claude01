package com.example.banking.benefit.domain.model.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 運算內容物件
 * 包含流程執行時需要的所有資訊
 */
public class ExecutionContext implements BaseExecutionContext {
    private final String flowId;
    private final String customerId;
    private final CustomerData customerData;
    private final String executionId;
    private final ExecutionMetadata metadata;
    private final Map<String, Object> variables;

    private ExecutionContext(String flowId, String customerId, CustomerData customerData) {
        this.flowId = flowId;
        this.customerId = customerId;
        this.customerData = customerData;
        this.executionId = generateExecutionId();
        this.metadata = new ExecutionMetadata();
        this.variables = new HashMap<>();
    }

    public static ExecutionContext create(String flowId, String customerId, CustomerData customerData) {
        if (flowId == null || flowId.trim().isEmpty()) {
            throw new IllegalArgumentException("flowId must not be null or empty");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("customerId must not be null or empty");
        }
        if (customerData == null) {
            throw new IllegalArgumentException("customerData must not be null");
        }
        return new ExecutionContext(flowId, customerId, customerData);
    }

    public String getFlowId() {
        return flowId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public CustomerData getCustomerData() {
        return customerData;
    }

    @Override
    public Object getCustomerData(String key) {
        return customerData.getAttribute(key)
                .map(CustomerAttribute::getValue)
                .orElse(null);
    }

    public String getExecutionId() {
        return executionId;
    }

    public ExecutionMetadata getMetadata() {
        return metadata;
    }

    public void addVariable(String key, Object value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Variable key must not be null or empty");
        }
        variables.put(key, value);
    }

    public Object getVariable(String key) {
        return variables.get(key);
    }

    public Map<String, Object> getVariables() {
        return new HashMap<>(variables);
    }

    private String generateExecutionId() {
        return UUID.randomUUID().toString();
    }
}