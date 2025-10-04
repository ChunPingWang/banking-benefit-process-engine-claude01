package com.example.banking.benefit.domain.model.log;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 日誌實體
 */
public class LogEntry {
    private String id;
    private LogLevel level;
    private String message;
    private Map<String, Object> context;
    private LocalDateTime timestamp;
    private String threadName;
    private String logger;
    private String source;
    
    private LogEntry(
            LogLevel level,
            String message,
            Map<String, Object> context,
            String source
    ) {
        this.id = java.util.UUID.randomUUID().toString();
        this.level = level;
        this.message = message;
        this.context = new HashMap<>(context);
        this.source = source;
        this.timestamp = LocalDateTime.now();
        this.threadName = Thread.currentThread().getName();
    }
    
    public static LogEntry create(
            LogLevel level,
            String message,
            Map<String, Object> context,
            String source
    ) {
        return new LogEntry(level, message, context, source);
    }
    
    public String getId() {
        return id;
    }
    
    public LogLevel getLevel() {
        return level;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Map<String, Object> getContext() {
        return new HashMap<>(context);
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getThreadName() {
        return threadName;
    }
    
    public String getLogger() {
        return logger;
    }
    
    public void setLogger(String logger) {
        this.logger = logger;
    }
    
    public String getSource() {
        return source;
    }
    
    public void addContext(String key, Object value) {
        this.context.put(key, value);
    }
}