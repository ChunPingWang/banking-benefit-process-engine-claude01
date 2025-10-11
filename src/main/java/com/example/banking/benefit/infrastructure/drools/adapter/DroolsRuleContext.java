package com.example.banking.benefit.infrastructure.drools.adapter;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.infrastructure.drools.model.BankingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Drools 規則執行上下文
 * 封裝規則執行所需的所有資訊
 */
public class DroolsRuleContext {
    
    private final DroolsRuleType ruleType;
    private final BaseExecutionContext executionContext;
    private final CustomerData customerData;
    private final BankingContext bankingContext;
    private final List<Object> additionalFacts;
    private final Map<String, Object> parameters;
    
    private DroolsRuleContext(Builder builder) {
        this.ruleType = builder.ruleType;
        this.executionContext = builder.executionContext;
        this.customerData = builder.customerData;
        this.bankingContext = builder.bankingContext;
        this.additionalFacts = new ArrayList<>(builder.additionalFacts);
        this.parameters = new HashMap<>(builder.parameters);
    }
    
    /**
     * 創建建造者
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * 根據執行上下文創建規則上下文
     */
    public static DroolsRuleContext fromExecutionContext(BaseExecutionContext context, DroolsRuleType ruleType) {
        BankingContext bankingContext = BankingContext.builder()
                .flowId(context.getFlowId())
                .executionId(context.getExecutionId())
                .customerId(context.getCustomerId())
                .build();
        
        return builder()
                .ruleType(ruleType)
                .executionContext(context)
                .customerData(context.getCustomerData())
                .bankingContext(bankingContext)
                .build();
    }
    
    // Getters
    public DroolsRuleType getRuleType() {
        return ruleType;
    }
    
    public BaseExecutionContext getExecutionContext() {
        return executionContext;
    }
    
    public CustomerData getCustomerData() {
        return customerData;
    }
    
    public BankingContext getBankingContext() {
        return bankingContext;
    }
    
    public List<Object> getAdditionalFacts() {
        return new ArrayList<>(additionalFacts);
    }
    
    public Map<String, Object> getParameters() {
        return new HashMap<>(parameters);
    }
    
    /**
     * 獲取參數值
     */
    public Object getParameter(String key) {
        return parameters.get(key);
    }
    
    /**
     * 獲取參數值（帶默認值）
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key, T defaultValue) {
        Object value = parameters.get(key);
        return value != null ? (T) value : defaultValue;
    }
    
    /**
     * 建造者類別
     */
    public static class Builder {
        private DroolsRuleType ruleType;
        private BaseExecutionContext executionContext;
        private CustomerData customerData;
        private BankingContext bankingContext;
        private final List<Object> additionalFacts = new ArrayList<>();
        private final Map<String, Object> parameters = new HashMap<>();
        
        public Builder ruleType(DroolsRuleType ruleType) {
            this.ruleType = ruleType;
            return this;
        }
        
        public Builder executionContext(BaseExecutionContext executionContext) {
            this.executionContext = executionContext;
            return this;
        }
        
        public Builder customerData(CustomerData customerData) {
            this.customerData = customerData;
            return this;
        }
        
        public Builder bankingContext(BankingContext bankingContext) {
            this.bankingContext = bankingContext;
            return this;
        }
        
        public Builder addFact(Object fact) {
            if (fact != null) {
                this.additionalFacts.add(fact);
            }
            return this;
        }
        
        public Builder addFacts(List<Object> facts) {
            if (facts != null) {
                this.additionalFacts.addAll(facts);
            }
            return this;
        }
        
        public Builder parameter(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }
        
        public Builder parameters(Map<String, Object> parameters) {
            if (parameters != null) {
                this.parameters.putAll(parameters);
            }
            return this;
        }
        
        public DroolsRuleContext build() {
            if (ruleType == null) {
                throw new IllegalArgumentException("ruleType 不能為空");
            }
            if (executionContext == null) {
                throw new IllegalArgumentException("executionContext 不能為空");
            }
            
            return new DroolsRuleContext(this);
        }
    }
    
    @Override
    public String toString() {
        return String.format("DroolsRuleContext{ruleType=%s, customerId='%s', flowId='%s'}", 
                ruleType, 
                customerData != null ? customerData.getId() : "null",
                executionContext != null ? executionContext.getFlowId() : "null");
    }
}