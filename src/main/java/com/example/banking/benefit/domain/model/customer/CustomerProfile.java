package com.example.banking.benefit.domain.model.customer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 客戶資料實體
 */
public class CustomerProfile {
    private String customerId;
    private String customerType;
    private String status;
    private Map<String, Object> attributes;
    private Map<String, Object> preferences;
    private LocalDateTime lastActivityTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    private CustomerProfile(String customerId, String customerType) {
        this.customerId = customerId;
        this.customerType = customerType;
        this.status = "ACTIVE";
        this.attributes = new HashMap<>();
        this.preferences = new HashMap<>();
        this.createdTime = LocalDateTime.now();
        this.updatedTime = this.createdTime;
    }
    
    public static CustomerProfile create(String customerId, String customerType) {
        return new CustomerProfile(customerId, customerType);
    }
    
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
        this.updatedTime = LocalDateTime.now();
    }
    
    public void setPreference(String key, Object value) {
        this.preferences.put(key, value);
        this.updatedTime = LocalDateTime.now();
    }
    
    public void updateLastActivityTime() {
        this.lastActivityTime = LocalDateTime.now();
        this.updatedTime = this.lastActivityTime;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedTime = LocalDateTime.now();
    }
    
    // Getters
    public String getCustomerId() {
        return customerId;
    }
    
    public String getCustomerType() {
        return customerType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public Map<String, Object> getAttributes() {
        return new HashMap<>(attributes);
    }
    
    public Map<String, Object> getPreferences() {
        return new HashMap<>(preferences);
    }
    
    public LocalDateTime getLastActivityTime() {
        return lastActivityTime;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }
    
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
}