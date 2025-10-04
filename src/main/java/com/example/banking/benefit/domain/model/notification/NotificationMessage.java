package com.example.banking.benefit.domain.model.notification;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 通知訊息實體
 */
public class NotificationMessage {
    private String messageId;
    private NotificationType type;
    private String recipient;
    private String subject;
    private String content;
    private Map<String, String> parameters;
    private LocalDateTime scheduledTime;
    private int priority;
    private String status;
    private LocalDateTime createdTime;
    
    private NotificationMessage(
            NotificationType type,
            String recipient,
            String subject,
            String content,
            int priority
    ) {
        this.messageId = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        this.priority = priority;
        this.parameters = new HashMap<>();
        this.status = "PENDING";
        this.createdTime = LocalDateTime.now();
    }
    
    public static NotificationMessage create(
            NotificationType type,
            String recipient,
            String subject,
            String content,
            int priority
    ) {
        return new NotificationMessage(type, recipient, subject, content, priority);
    }
    
    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }
    
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // Getters
    public String getMessageId() {
        return messageId;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public String getContent() {
        return content;
    }
    
    public Map<String, String> getParameters() {
        return new HashMap<>(parameters);
    }
    
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public String getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
}