package com.example.banking.benefit.domain.service.executor;

import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.exception.FlowExecutionException;
import java.util.List;

/**
 * 節點執行器工廠
 */
public class NodeExecutorFactory {
    
    private final List<NodeExecutor> executors;
    
    public NodeExecutorFactory(List<NodeExecutor> executors) {
        this.executors = executors;
    }
    
    /**
     * 根據節點類型獲取對應的執行器
     *
     * @param node 要執行的節點
     * @return 節點執行器
     * @throws FlowExecutionException 當找不到支援的執行器時拋出異常
     */
    public NodeExecutor getExecutor(Node node) {
        return executors.stream()
                .filter(executor -> executor.supports(node))
                .findFirst()
                .orElseThrow(() -> new FlowExecutionException("找不到支援的節點執行器: " + node.getClass().getSimpleName()));
    }
}