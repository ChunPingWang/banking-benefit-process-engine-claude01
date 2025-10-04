package com.example.banking.benefit.domain.model.config;

import java.util.Map;
import java.util.HashMap;

/**
 * 流程配置類別
 */
public class FlowConfig {
    private String executionMode;
    private int maxRetries;
    private long timeoutMillis;
    private boolean logEnabled;
    private Map<String, String> properties;
    
    public FlowConfig(
            String executionMode,
            int maxRetries,
            long timeoutMillis,
            boolean logEnabled
    ) {
        this.executionMode = executionMode;
        this.maxRetries = maxRetries;
        this.timeoutMillis = timeoutMillis;
        this.logEnabled = logEnabled;
        this.properties = new HashMap<>();
    }
    
    public String getExecutionMode() {
        return executionMode;
    }
    
    public void setExecutionMode(String executionMode) {
        this.executionMode = executionMode;
    }
    
    public int getMaxRetries() {
        return maxRetries;
    }
    
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    public long getTimeoutMillis() {
        return timeoutMillis;
    }
    
    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }
    
    public boolean isLogEnabled() {
        return logEnabled;
    }
    
    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }
    
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }
    
    public void setProperty(String key, String value) {
        this.properties.put(key, value);
    }
    
    public String getProperty(String key) {
        return this.properties.get(key);
    }
    
    public void removeProperty(String key) {
        this.properties.remove(key);
    }
    
    public void clearProperties() {
        this.properties.clear();
    }
}