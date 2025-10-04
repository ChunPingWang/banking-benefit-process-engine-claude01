package com.example.banking.benefit.domain.port.output;

import com.example.banking.benefit.domain.model.notification.NotificationMessage;
import com.example.banking.benefit.domain.model.notification.NotificationType;
import java.util.List;

/**
 * 通知服務介面
 * Secondary Port - 輸出埠
 */
public interface NotificationPort {
    
    /**
     * 發送通知
     *
     * @param message 通知訊息
     * @return 是否發送成功
     */
    boolean send(NotificationMessage message);
    
    /**
     * 批次發送通知
     *
     * @param messages 通知訊息列表
     * @return 成功發送的數量
     */
    int sendBatch(List<NotificationMessage> messages);
    
    /**
     * 檢查通知服務是否可用
     *
     * @param type 通知類型
     * @return 是否可用
     */
    boolean isAvailable(NotificationType type);
    
    /**
     * 取得支援的通知類型
     *
     * @return 通知類型列表
     */
    List<NotificationType> getSupportedTypes();
    
    /**
     * 取得通知發送狀態
     *
     * @param messageId 訊息ID
     * @return 發送狀態
     */
    String getMessageStatus(String messageId);
}