package com.example.banking.benefit.domain.service;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.result.ExecutionStatus;
import com.example.banking.benefit.domain.port.output.CachePort;
import com.example.banking.benefit.domain.port.output.LoggingPort;
import com.example.banking.benefit.domain.port.output.NotificationPort;
import com.example.banking.benefit.domain.repository.FlowRepository;
import com.example.banking.benefit.domain.exception.FlowExecutionException;
import com.example.banking.benefit.domain.model.log.LogLevel;
import com.example.banking.benefit.domain.model.log.AuditLog;
import com.example.banking.benefit.domain.model.notification.NotificationMessage;
import com.example.banking.benefit.domain.model.notification.NotificationType;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * 流程執行服務的基礎類別
 * 提供基本的 Flow 執行和日誌記錄功能
 */
public abstract class BaseFlowExecutionService implements FlowExecutionService {
    
    protected final FlowRepository flowRepository;
    protected final CachePort cachePort;
    protected final LoggingPort loggingPort;
    protected final NotificationPort notificationPort;

    protected BaseFlowExecutionService(
            FlowRepository flowRepository,
            CachePort cachePort,
            LoggingPort loggingPort,
            NotificationPort notificationPort) {
        this.flowRepository = flowRepository;
        this.cachePort = cachePort;
        this.loggingPort = loggingPort;
        this.notificationPort = notificationPort;
    }

    @Override
    public ExecutionResult execute(Flow flow, BaseExecutionContext context) {
        Map<String, Object> logContext = new HashMap<>();
        logContext.put("flowId", flow.getFlowId());
        logContext.put("customerId", context.getCustomerId());
        
        loggingPort.log(LogLevel.INFO, "開始執行流程: " + flow.getFlowId() + ", 客戶: " + context.getCustomerId(), logContext);
        
        try {
            validateFlow(flow);
            String executionId = generateExecutionId();
            
            ExecutionResult result = doExecute(flow, context, executionId);
            
            logExecution(flow, context, result);
            handleNotification(flow, context, result);
            
            return result;
            
        } catch (Exception e) {
            Map<String, Object> errorContext = new HashMap<>(logContext);
            errorContext.put("error", e.getMessage());
            loggingPort.error("流程執行異常: " + e.getMessage(), e, errorContext);
            throw new FlowExecutionException("流程執行失敗", e);
        }
    }
    
    @Override
    public void pause(Flow flow, BaseExecutionContext context) {
        Map<String, Object> logContext = new HashMap<>();
        logContext.put("flowId", flow.getFlowId());
        logContext.put("customerId", context.getCustomerId());
        
        loggingPort.log(LogLevel.INFO, "暫停流程: " + flow.getFlowId() + ", 客戶: " + context.getCustomerId(), logContext);
        doPause(flow, context);
    }
    
    @Override
    public ExecutionResult resume(Flow flow, BaseExecutionContext context) {
        Map<String, Object> logContext = new HashMap<>();
        logContext.put("flowId", flow.getFlowId());
        logContext.put("customerId", context.getCustomerId());
        
        loggingPort.log(LogLevel.INFO, "繼續執行流程: " + flow.getFlowId() + ", 客戶: " + context.getCustomerId(), logContext);
        return doResume(flow, context);
    }
    
    @Override
    public void terminate(Flow flow, BaseExecutionContext context) {
        Map<String, Object> logContext = new HashMap<>();
        logContext.put("flowId", flow.getFlowId());
        logContext.put("customerId", context.getCustomerId());
        
        loggingPort.log(LogLevel.INFO, "中止流程: " + flow.getFlowId() + ", 客戶: " + context.getCustomerId(), logContext);
        doTerminate(flow, context);
    }
    
    @Override
    public String getExecutionStatus(Flow flow, BaseExecutionContext context) {
        return doGetExecutionStatus(flow, context);
    }
    
    /**
     * 產生執行 ID
     */
    protected String generateExecutionId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 驗證流程定義
     */
    protected void validateFlow(Flow flow) {
        if (!flow.isValid()) {
            throw new FlowExecutionException("流程定義無效: " + flow.getFlowId());
        }
    }
    
    /**
     * 記錄執行日誌
     */
    protected void logExecution(Flow flow, BaseExecutionContext context, ExecutionResult result) {
        String status = result.getStatus() == ExecutionStatus.SUCCESS ? "SUCCESS" : "FAILURE";
        AuditLog auditLog = AuditLog.create(
            context.getCustomerId(),
            "Flow Execution",
            "Flow",
            flow.getFlowId().toString(),
            "EXECUTE"
        );
        auditLog.setResult(status);
        loggingPort.audit(auditLog);
    }
    
    /**
     * 處理結果通知
     */
    protected void handleNotification(Flow flow, BaseExecutionContext context, ExecutionResult result) {
        NotificationType type = NotificationType.SYSTEM;
        String subject = result.getStatus() == ExecutionStatus.SUCCESS ? "Flow Execution Success" : "Flow Execution Failed";
        String content = result.getStatus() == ExecutionStatus.SUCCESS ?
            "Flow " + flow.getFlowId() + " executed successfully" :
            "Flow " + flow.getFlowId() + " execution failed: " + result.getMessage();
            
        NotificationMessage message = NotificationMessage.create(
            type,
            context.getCustomerId(),
            subject,
            content,
            1
        );
            
        notificationPort.send(message);
    }
    
    /**
     * 實際執行流程的抽象方法，由子類實作具體邏輯
     */
    protected abstract ExecutionResult doExecute(Flow flow, BaseExecutionContext context, String executionId);
    
    /**
     * 實作暫停流程的邏輯
     */
    protected abstract void doPause(Flow flow, BaseExecutionContext context);
    
    /**
     * 實作繼續執行流程的邏輯
     */
    protected abstract ExecutionResult doResume(Flow flow, BaseExecutionContext context);
    
    /**
     * 實作中止流程的邏輯
     */
    protected abstract void doTerminate(Flow flow, BaseExecutionContext context);
    
    /**
     * 實作取得流程執行狀態的邏輯
     */
    protected abstract String doGetExecutionStatus(Flow flow, BaseExecutionContext context);
}