package com.example.banking.benefit.domain.service;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.result.ExecutionResult;

/**
 * 流程執行服務介面
 */
public interface FlowExecutionService {
    
    /**
     * 執行流程
     *
     * @param flow 流程定義
     * @param context 執行內容
     * @return 執行結果
     * @throws FlowExecutionException 流程執行異常
     */
    ExecutionResult execute(Flow flow, BaseExecutionContext context);
    
    /**
     * 暫停流程執行
     *
     * @param flow 流程定義
     * @param context 執行內容
     */
    void pause(Flow flow, BaseExecutionContext context);
    
    /**
     * 繼續執行流程
     *
     * @param flow 流程定義
     * @param context 執行內容
     * @return 執行結果
     */
    ExecutionResult resume(Flow flow, BaseExecutionContext context);
    
    /**
     * 中止流程執行
     *
     * @param flow 流程定義
     * @param context 執行內容
     */
    void terminate(Flow flow, BaseExecutionContext context);
    
    /**
     * 取得流程執行狀態
     *
     * @param flow 流程定義
     * @param context 執行內容
     * @return 執行狀態
     */
    String getExecutionStatus(Flow flow, BaseExecutionContext context);
}