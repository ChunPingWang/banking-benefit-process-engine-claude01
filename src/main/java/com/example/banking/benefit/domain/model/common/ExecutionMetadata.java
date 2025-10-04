package com.example.banking.benefit.domain.model.common;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 執行元數據值物件
 * 記錄執行過程中的相關資訊
 */
public class ExecutionMetadata {
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private final Map<String, Object> attributes;

    public ExecutionMetadata() {
        this.startTime = LocalDateTime.now();
        this.attributes = new HashMap<>();
    }

    public void complete() {
        this.endTime = LocalDateTime.now();
    }

    public void addAttribute(String key, Object value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("key must not be null or empty");
        }
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public long getExecutionTimeMillis() {
        if (endTime == null) {
            return -1;
        }
        return java.time.Duration.between(startTime, endTime).toMillis();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Map<String, Object> getAttributes() {
        return new HashMap<>(attributes);
    }
}