package com.example.banking.benefit.domain.model.notification;

/**
 * 通知類型列舉
 */
public enum NotificationType {
    EMAIL("email", "電子郵件"),
    SMS("sms", "簡訊"),
    PUSH("push", "推播通知"),
    SYSTEM("system", "系統通知");
    
    private final String code;
    private final String description;
    
    NotificationType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static NotificationType fromCode(String code) {
        for (NotificationType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid notification type code: " + code);
    }
}