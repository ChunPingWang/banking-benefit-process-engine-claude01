package com.example.banking.benefit.domain.model.common;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class DefaultExecutionContext implements BaseExecutionContext {

    private String flowId;
    private String executionId;
    private String customerId;
    private Map<String, Object> variables;
    private CustomerData customerData;

    @Override
    public Map<String, Object> getVariables() {
        if(variables == null) {
            variables = new HashMap<>();
        }
        return variables;
    }

    @Override
    public void addVariable(String key, Object value) {
        getVariables().put(key, value);
    }

    @Override
    public Object getVariable(String key) {
        return getVariables().get(key);
    }

    @Override
    public Object getCustomerData(String key) {
        return customerData != null ? customerData.getAttribute(key) : null;
    }
}