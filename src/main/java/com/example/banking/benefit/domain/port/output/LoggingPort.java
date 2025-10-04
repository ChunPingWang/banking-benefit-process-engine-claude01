package com.example.banking.benefit.domain.port.output;

import com.example.banking.benefit.domain.model.log.AuditLog;
import com.example.banking.benefit.domain.model.log.LogLevel;
import com.example.banking.benefit.domain.model.log.LogEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 日誌記錄服務介面
 * Secondary Port - 輸出埠
 */
public interface LoggingPort {
    
    /**
     * 記錄一般日誌
     *
     * @param level 日誌等級
     * @param message 日誌訊息
     * @param context 日誌上下文
     */
    void log(LogLevel level, String message, Map<String, Object> context);
    
    /**
     * 記錄審計日誌
     *
     * @param auditLog 審計日誌
     */
    void audit(AuditLog auditLog);
    
    /**
     * 記錄錯誤日誌
     *
     * @param message 錯誤訊息
     * @param error 錯誤物件
     * @param context 日誌上下文
     */
    void error(String message, Throwable error, Map<String, Object> context);
    
    /**
     * 查詢日誌
     *
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @param level 日誌等級
     * @return 日誌列表
     */
    List<LogEntry> query(LocalDateTime startTime, LocalDateTime endTime, LogLevel level);
    
    /**
     * 查詢審計日誌
     *
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @param operationType 操作類型
     * @return 審計日誌列表
     */
    List<AuditLog> queryAudit(LocalDateTime startTime, LocalDateTime endTime, String operationType);
    
    /**
     * 清除過期日誌
     *
     * @param retentionDays 保留天數
     * @return 清除的日誌數量
     */
    int purgeExpiredLogs(int retentionDays);
}