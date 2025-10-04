package com.example.banking.benefit.domain.model.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 客戶資料類別
 * 包含客戶的基本資料和其他屬性
 */
public class CustomerData {
    
    private String customerId;
    private String customerName;
    private Map<String, CustomerAttribute> attributes;
    
    public CustomerData(String customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.attributes = new HashMap<>();
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public Map<String, CustomerAttribute> getAttributes() {
        return attributes;
    }
    
    public void addAttribute(String key, CustomerAttribute value) {
        this.attributes.put(key, value);
    }
    
    public Optional<CustomerAttribute> getAttribute(String key) {
        return Optional.ofNullable(this.attributes.get(key));
    }
}