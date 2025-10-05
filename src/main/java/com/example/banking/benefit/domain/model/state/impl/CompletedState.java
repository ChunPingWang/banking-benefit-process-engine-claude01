package com.example.banking.benefit.domain.model.state.impl;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.result.ProcessResult;
import com.example.banking.benefit.domain.model.state.BaseProcessState;
import com.example.banking.benefit.domain.model.state.ProcessState;

/**
 * 完成狀態
 * 處理節點的終止狀態
 */
public class CompletedState extends BaseProcessState {
    
    public CompletedState(String flowId, String nodeId) {
        super(flowId, nodeId, "COMPLETED");
    }
    
    @Override
    protected boolean doCanEnter(ExecutionContext context) {
        // 只有在所有必要條件滿足時才允許進入完成狀態
        // 這裡可以加入額外的檢查邏輯
        return true;
    }
    
    @Override
    protected void doOnEnter(ExecutionContext context) {
        // 進入完成狀態時的清理工作
        // 例如：清除暫存資料、觸發完成事件等
    }
    
    @Override
    protected ProcessResult doExecute(ExecutionContext context) {
        return ProcessResult.success("流程完成")
            .withData("completionTime", System.currentTimeMillis());
    }
    
    @Override
    protected ProcessState doDetermineNextState(ExecutionContext context, ProcessResult result) {
        // 完成狀態是終止狀態，沒有下一個狀態
        return null;
    }
}