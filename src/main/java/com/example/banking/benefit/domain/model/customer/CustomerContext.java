package com.example.banking.benefit.domain.model.customer;

import java.util.HashMap;
import java.util.Map;

/**
 * 客戶執行上下文
 */
public class CustomerContext {
    private String customerId;
    private String customerType;
    private Map<String, Object> attributes;
    private Map<String, Integer> benefitUsage;
    
    private CustomerContext(String customerId, String customerType) {
        this.customerId = customerId;
        this.customerType = customerType;
        this.attributes = new HashMap<>();
        this.benefitUsage = new HashMap<>();
    }
    
    public static CustomerContext create(String customerId, String customerType) {
        return new CustomerContext(customerId, customerType);
    }
    
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }
    
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }
    
    public void updateBenefitUsage(String benefitCode, int usageCount) {
        this.benefitUsage.merge(benefitCode, usageCount, Integer::sum);
    }
    
    public int getBenefitUsage(String benefitCode) {
        return this.benefitUsage.getOrDefault(benefitCode, 0);
    }
    
    // Getters
    public String getCustomerId() {
        return customerId;
    }
    
    public String getCustomerType() {
        return customerType;
    }
    
    public Map<String, Object> getAttributes() {
        return new HashMap<>(attributes);
    }
    
    public Map<String, Integer> getBenefitUsage() {
        return new HashMap<>(benefitUsage);
    }
}