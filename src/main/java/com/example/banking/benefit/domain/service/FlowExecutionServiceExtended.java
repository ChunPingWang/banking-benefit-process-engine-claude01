package com.example.banking.benefit.domain.service;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.result.ExecutionResult;

public interface FlowExecutionServiceExtended extends FlowExecutionService {
    
    /**
     * 根據執行ID取得執行結果
     *
     * @param executionId 執行ID
     * @return 執行結果
     */
    ExecutionResult getExecutionResult(String executionId);
    
    /**
     * 取消執行中的流程
     *
     * @param executionId 執行ID
     */
    void cancelExecution(String executionId);
}