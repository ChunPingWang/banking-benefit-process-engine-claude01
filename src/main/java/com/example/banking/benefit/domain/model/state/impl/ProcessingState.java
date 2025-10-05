package com.example.banking.benefit.domain.model.state.impl;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.result.ProcessResult;
import com.example.banking.benefit.domain.model.state.BaseProcessState;
import com.example.banking.benefit.domain.model.state.ProcessState;

/**
 * 處理中狀態
 * 執行主要的處理邏輯
 */
public class ProcessingState extends BaseProcessState {
    
    public ProcessingState(String flowId, String nodeId) {
        super(flowId, nodeId, "PROCESSING");
    }
    
    @Override
    protected ProcessResult doExecute(ExecutionContext context) {
        // 這裡應該實作具體的業務邏輯
        return ProcessResult.success("處理完成")
            .withData("processTime", System.currentTimeMillis());
    }
    
    @Override
    protected ProcessState doDetermineNextState(ExecutionContext context, ProcessResult result) {
        return result.isSuccess()
            ? new CompletedState(flowId, nodeId)
            : null;  // 如果失敗則保持在當前狀態
    }
}