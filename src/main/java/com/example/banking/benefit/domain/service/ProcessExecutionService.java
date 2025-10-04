package com.example.banking.benefit.domain.service;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.process.ProcessNode;

/**
 * 處理節點執行服務介面
 */
public interface ProcessExecutionService {
    
    /**
     * 執行處理節點
     *
     * @param node 處理節點
     * @param context 執行內容
     * @throws ProcessExecutionException 處理執行異常
     */
    void execute(ProcessNode node, BaseExecutionContext context);
    
    /**
     * 檢查處理節點是否可以執行
     *
     * @param node 處理節點
     * @param context 執行內容
     * @return 是否可執行
     */
    boolean canExecute(ProcessNode node, BaseExecutionContext context);
    
    /**
     * 取消處理節點的執行
     *
     * @param node 處理節點
     * @param context 執行內容
     */
    void rollback(ProcessNode node, BaseExecutionContext context);
}