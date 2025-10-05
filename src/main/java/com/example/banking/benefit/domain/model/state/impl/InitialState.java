package com.example.banking.benefit.domain.model.state.impl;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.result.ProcessResult;
import com.example.banking.benefit.domain.model.state.BaseProcessState;
import com.example.banking.benefit.domain.model.state.ProcessState;

/**
 * 初始狀態
 * 處理節點的起始狀態
 */
public class InitialState extends BaseProcessState {
    
    public InitialState(String flowId, String nodeId) {
        super(flowId, nodeId, "INITIAL");
    }
    
    @Override
    protected ProcessResult doExecute(ExecutionContext context) {
        return ProcessResult.success("初始化完成")
            .withData("initTime", System.currentTimeMillis());
    }
    
    @Override
    protected ProcessState doDetermineNextState(ExecutionContext context, ProcessResult result) {
        return new ProcessingState(flowId, nodeId);
    }
}