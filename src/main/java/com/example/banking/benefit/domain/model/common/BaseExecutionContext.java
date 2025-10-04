package com.example.banking.benefit.domain.model.common;

import java.util.Map;

/**
 * 基礎執行上下文介面
 */
public interface BaseExecutionContext {
    
    /**
     * 取得流程 ID
     */
    String getFlowId();
    
    /**
     * 取得執行 ID
     */
    String getExecutionId();
    
    /**
     * 取得客戶 ID
     */
    String getCustomerId();
    
    /**
     * 取得變數集合
     */
    Map<String, Object> getVariables();
    
    /**
     * 設定變數值
     */
    void addVariable(String key, Object value);
    
    /**
     * 取得變數值
     */
    Object getVariable(String key);
    
    /**
     * 取得客戶資料
     */
    Map<String, Object> getCustomerData();
    
    /**
     * 取得指定的客戶資料
     */
    Object getCustomerData(String key);
}