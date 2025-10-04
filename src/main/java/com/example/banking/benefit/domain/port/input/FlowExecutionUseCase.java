package com.example.banking.benefit.domain.port.input;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.result.ExecutionResult;

/**
 * 流程執行用例介面
 * Primary Port - 輸入埠
 */
public interface FlowExecutionUseCase {
    
    /**
     * 執行流程
     *
     * @param flowId 流程ID
     * @param context 執行上下文
     * @return 執行結果
     */
    ExecutionResult executeFlow(FlowId flowId, BaseExecutionContext context);
    
    /**
     * 執行特定版本的流程
     *
     * @param flowId 流程ID
     * @param version 版本
     * @param context 執行上下文
     * @return 執行結果
     */
    ExecutionResult executeFlowVersion(FlowId flowId, String version, BaseExecutionContext context);
    
    /**
     * 恢復執行中斷的流程
     *
     * @param executionId 執行ID
     * @param context 執行上下文
     * @return 執行結果
     */
    ExecutionResult resumeFlow(String executionId, BaseExecutionContext context);
    
    /**
     * 中止正在執行的流程
     *
     * @param executionId 執行ID
     */
    void terminateFlow(String executionId);
    
    /**
     * 驗證流程是否可執行
     *
     * @param flowId 流程ID
     * @param context 執行上下文
     * @return 是否可執行
     */
    boolean validateFlow(FlowId flowId, BaseExecutionContext context);
}