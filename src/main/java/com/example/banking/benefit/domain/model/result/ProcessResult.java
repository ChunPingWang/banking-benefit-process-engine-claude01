package com.example.banking.benefit.domain.model.result;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 處理結果
 * 封裝處理節點的執行結果
 */
public class ProcessResult {
    
    private final boolean success;
    private final String message;
    private final Map<String, Object> data;
    private final LocalDateTime timestamp;
    
    private ProcessResult(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = new HashMap<>(data);
        this.timestamp = LocalDateTime.now();
    }
    
    public static ProcessResult success() {
        return new ProcessResult(true, "處理成功", new HashMap<>());
    }
    
    public static ProcessResult success(String message) {
        return new ProcessResult(true, message, new HashMap<>());
    }
    
    public static ProcessResult failure(String message) {
        return new ProcessResult(false, message, new HashMap<>());
    }
    
    public ProcessResult withData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }
    
    public Object getData(String key) {
        return data.get(key);
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("ProcessResult{success=%s, message='%s', data=%s, timestamp=%s}",
            success, message, data, timestamp);
    }
}