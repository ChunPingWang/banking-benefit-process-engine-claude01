package com.example.banking.benefit.domain.model.log;

/**
 * 日誌等級列舉
 */
public enum LogLevel {
    TRACE("TRACE", 1),
    DEBUG("DEBUG", 2),
    INFO("INFO", 3),
    WARN("WARN", 4),
    ERROR("ERROR", 5);
    
    private final String name;
    private final int severity;
    
    LogLevel(String name, int severity) {
        this.name = name;
        this.severity = severity;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSeverity() {
        return severity;
    }
    
    public static LogLevel fromName(String name) {
        for (LogLevel level : values()) {
            if (level.name.equals(name)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid log level name: " + name);
    }
    
    public boolean isMoreSevereThan(LogLevel other) {
        return this.severity > other.severity;
    }
}