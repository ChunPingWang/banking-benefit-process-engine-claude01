package com.example.banking.benefit.domain.service.executor;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.result.ExecutionResult;

import java.util.Map;

/**
 * 節點執行器介面
 */
public interface NodeExecutor {
    
    /**
     * 執行節點
     *
     * @param node 要執行的節點
     * @param context 執行上下文
     * @param nodeContext 節點執行上下文
     * @return 執行結果
     */
    ExecutionResult execute(Node node, BaseExecutionContext context, Map<String, Object> nodeContext);
    
    /**
     * 檢查是否支援此節點類型
     *
     * @param node 要檢查的節點
     * @return 是否支援
     */
    boolean supports(Node node);
}