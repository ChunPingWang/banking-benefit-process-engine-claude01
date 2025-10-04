package com.example.banking.benefit.domain.model.log;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 審計日誌實體
 */
public class AuditLog {
    private String id;
    private String userId;
    private String operationType;
    private String resourceType;
    private String resourceId;
    private String action;
    private Map<String, Object> changes;
    private String result;
    private String ipAddress;
    private LocalDateTime timestamp;
    
    private AuditLog(
            String userId,
            String operationType,
            String resourceType,
            String resourceId,
            String action
    ) {
        this.id = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.operationType = operationType;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.action = action;
        this.changes = new HashMap<>();
        this.timestamp = LocalDateTime.now();
    }
    
    public static AuditLog create(
            String userId,
            String operationType,
            String resourceType,
            String resourceId,
            String action
    ) {
        return new AuditLog(userId, operationType, resourceType, resourceId, action);
    }
    
    public void addChange(String field, Object oldValue, Object newValue) {
        Map<String, Object> change = new HashMap<>();
        change.put("old", oldValue);
        change.put("new", newValue);
        this.changes.put(field, change);
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getOperationType() {
        return operationType;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public String getResourceId() {
        return resourceId;
    }
    
    public String getAction() {
        return action;
    }
    
    public Map<String, Object> getChanges() {
        return new HashMap<>(changes);
    }
    
    public String getResult() {
        return result;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}