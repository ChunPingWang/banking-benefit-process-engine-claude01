package com.example.banking.benefit.infrastructure.drools.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 銀行業務上下文
 * 作為 Drools 規則的事實物件，包含銀行業務相關的上下文資訊
 */
public class BankingContext {
    
    private final String flowId;
    private final String executionId;
    private final String customerId;
    private final String productType;
    private final String channelType;
    private final LocalDateTime requestTime;
    private final Map<String, Object> businessData;
    
    private BankingContext(Builder builder) {
        this.flowId = builder.flowId;
        this.executionId = builder.executionId;
        this.customerId = builder.customerId;
        this.productType = builder.productType;
        this.channelType = builder.channelType;
        this.requestTime = builder.requestTime != null ? builder.requestTime : LocalDateTime.now();
        this.businessData = new HashMap<>(builder.businessData);
    }
    
    /**
     * 創建建造者
     */
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public String getFlowId() {
        return flowId;
    }
    
    public String getExecutionId() {
        return executionId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public String getProductType() {
        return productType;
    }
    
    public String getChannelType() {
        return channelType;
    }
    
    public LocalDateTime getRequestTime() {
        return requestTime;
    }
    
    public Map<String, Object> getBusinessData() {
        return new HashMap<>(businessData);
    }
    
    /**
     * 獲取業務資料
     */
    public Object getBusinessData(String key) {
        return businessData.get(key);
    }
    
    /**
     * 獲取業務資料（帶默認值）
     */
    @SuppressWarnings("unchecked")
    public <T> T getBusinessData(String key, T defaultValue) {
        Object value = businessData.get(key);
        return value != null ? (T) value : defaultValue;
    }
    
    /**
     * 檢查是否為信用卡產品
     */
    public boolean isCreditCardProduct() {
        return "CREDIT_CARD".equals(productType);
    }
    
    /**
     * 檢查是否為貸款產品
     */
    public boolean isLoanProduct() {
        return "LOAN".equals(productType);
    }
    
    /**
     * 檢查是否為存款產品
     */
    public boolean isDepositProduct() {
        return "DEPOSIT".equals(productType);
    }
    
    /**
     * 檢查是否為網路銀行渠道
     */
    public boolean isOnlineBankingChannel() {
        return "ONLINE_BANKING".equals(channelType);
    }
    
    /**
     * 檢查是否為手機銀行渠道
     */
    public boolean isMobileBankingChannel() {
        return "MOBILE_BANKING".equals(channelType);
    }
    
    /**
     * 建造者類別
     */
    public static class Builder {
        private String flowId;
        private String executionId;
        private String customerId;
        private String productType;
        private String channelType;
        private LocalDateTime requestTime;
        private final Map<String, Object> businessData = new HashMap<>();
        
        public Builder flowId(String flowId) {
            this.flowId = flowId;
            return this;
        }
        
        public Builder executionId(String executionId) {
            this.executionId = executionId;
            return this;
        }
        
        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }
        
        public Builder productType(String productType) {
            this.productType = productType;
            return this;
        }
        
        public Builder channelType(String channelType) {
            this.channelType = channelType;
            return this;
        }
        
        public Builder requestTime(LocalDateTime requestTime) {
            this.requestTime = requestTime;
            return this;
        }
        
        public Builder businessData(String key, Object value) {
            this.businessData.put(key, value);
            return this;
        }
        
        public Builder businessData(Map<String, Object> businessData) {
            if (businessData != null) {
                this.businessData.putAll(businessData);
            }
            return this;
        }
        
        public BankingContext build() {
            return new BankingContext(this);
        }
    }
    
    @Override
    public String toString() {
        return String.format("BankingContext{flowId='%s', customerId='%s', productType='%s', channelType='%s'}", 
                flowId, customerId, productType, channelType);
    }
}