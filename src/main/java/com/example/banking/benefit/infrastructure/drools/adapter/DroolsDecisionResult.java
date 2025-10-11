package com.example.banking.benefit.infrastructure.drools.adapter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Drools 決策結果
 * 封裝規則執行的結果資訊
 */
public class DroolsDecisionResult {
    
    private final boolean success;
    private final String ruleId;
    private final String message;
    private final Object result;
    private final LocalDateTime executionTime;
    private final Map<String, Object> metadata;
    
    private DroolsDecisionResult(boolean success, String ruleId, String message, Object result) {
        this.success = success;
        this.ruleId = ruleId;
        this.message = message;
        this.result = result;
        this.executionTime = LocalDateTime.now();
        this.metadata = new HashMap<>();
    }
    
    /**
     * 創建成功結果
     */
    public static DroolsDecisionResult success(String message) {
        return new DroolsDecisionResult(true, null, message, true);
    }
    
    /**
     * 創建成功結果（帶規則ID）
     */
    public static DroolsDecisionResult success(String ruleId, String message) {
        return new DroolsDecisionResult(true, ruleId, message, true);
    }
    
    /**
     * 創建成功結果（帶結果物件）
     */
    public static DroolsDecisionResult success(String ruleId, String message, Object result) {
        return new DroolsDecisionResult(true, ruleId, message, result);
    }
    
    /**
     * 創建失敗結果
     */
    public static DroolsDecisionResult failure(String message) {
        return new DroolsDecisionResult(false, null, message, false);
    }
    
    /**
     * 創建失敗結果（帶規則ID）
     */
    public static DroolsDecisionResult failure(String ruleId, String message) {
        return new DroolsDecisionResult(false, ruleId, message, false);
    }
    
    /**
     * 添加元數據
     */
    public DroolsDecisionResult withMetadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }
    
    /**
     * 添加執行時間元數據
     */
    public DroolsDecisionResult withExecutionDuration(long durationMs) {
        this.metadata.put("executionDuration", durationMs);
        return this;
    }
    
    /**
     * 添加觸發的規則數量
     */
    public DroolsDecisionResult withFiredRules(int count) {
        this.metadata.put("firedRulesCount", count);
        return this;
    }
    
    // Getters
    public boolean isSuccess() {
        return success;
    }
    
    public String getRuleId() {
        return ruleId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Object getResult() {
        return result;
    }
    
    public LocalDateTime getExecutionTime() {
        return executionTime;
    }
    
    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }
    
    /**
     * 獲取布林結果
     */
    public boolean getBooleanResult() {
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        return success;
    }
    
    /**
     * 獲取字串結果
     */
    public String getStringResult() {
        if (result != null) {
            return result.toString();
        }
        return message;
    }
    
    @Override
    public String toString() {
        return String.format("DroolsDecisionResult{success=%s, ruleId='%s', message='%s', result=%s, time=%s}", 
                success, ruleId, message, result, executionTime);
    }
}