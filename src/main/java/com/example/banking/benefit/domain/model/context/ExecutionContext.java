package com.example.banking.benefit.domain.model.context;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import java.util.HashMap;
import java.util.Map;

/**
 * 執行上下文類別
 * 用於存儲流程執行過程中的各種資料
 */
public class ExecutionContext implements BaseExecutionContext {
    
    private String flowId;
    private Map<String, Object> customerData;
    private Map<String, Object> variables;
    private String customerId;
    private String executionId;
    
    public ExecutionContext() {
        this.customerData = new HashMap<>();
        this.variables = new HashMap<>();
    }
    
    @Override
    public Map<String, Object> getCustomerData() {
        return customerData;
    }
    
    public void setCustomerData(Map<String, Object> customerData) {
        this.customerData = customerData;
    }
    
    @Override
    public Map<String, Object> getVariables() {
        return variables;
    }
    
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
    
    @Override
    public void addVariable(String key, Object value) {
        this.variables.put(key, value);
    }
    
    @Override
    public Object getVariable(String key) {
        return this.variables.get(key);
    }
    
    public void addCustomerData(String key, Object value) {
        this.customerData.put(key, value);
    }
    
    @Override
    public Object getCustomerData(String key) {
        return this.customerData.get(key);
    }
    
    @Override
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    @Override
    public String getExecutionId() {
        return executionId;
    }
    
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
    
    @Override
    public String getFlowId() {
        return flowId;
    }
    
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }
}