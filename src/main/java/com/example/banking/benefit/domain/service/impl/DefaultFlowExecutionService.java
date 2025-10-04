package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.process.ProcessNode;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.result.ExecutionStatus;
import com.example.banking.benefit.domain.port.output.CachePort;
import com.example.banking.benefit.domain.port.output.LoggingPort;
import com.example.banking.benefit.domain.port.output.NotificationPort;
import com.example.banking.benefit.domain.repository.FlowRepository;
import com.example.banking.benefit.domain.service.BaseFlowExecutionService;
import com.example.banking.benefit.domain.exception.FlowExecutionException;
import com.example.banking.benefit.domain.service.executor.NodeExecutor;
import com.example.banking.benefit.domain.service.executor.NodeExecutorFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 預設的流程執行服務實作
 */
public class DefaultFlowExecutionService extends BaseFlowExecutionService {
    
    private final NodeExecutorFactory nodeExecutorFactory;
    private final Map<String, ExecutionStatus> executionStates;
    private final Map<String, Map<String, Object>> executionContexts;
    
    public DefaultFlowExecutionService(
            FlowRepository flowRepository,
            CachePort cachePort,
            LoggingPort loggingPort,
            NotificationPort notificationPort,
            NodeExecutorFactory nodeExecutorFactory) {
        super(flowRepository, cachePort, loggingPort, notificationPort);
        this.nodeExecutorFactory = nodeExecutorFactory;
        this.executionStates = new ConcurrentHashMap<>();
        this.executionContexts = new ConcurrentHashMap<>();
    }
    
    @Override
    protected ExecutionResult doExecute(Flow flow, BaseExecutionContext context, String executionId) {
        try {
            // 初始化執行狀態
            executionStates.put(executionId, ExecutionStatus.IN_PROGRESS);
            executionContexts.put(executionId, new HashMap<>());
            
            // 獲取起始節點
            Node startNode = (Node) flow.getStartNode();
            if (startNode == null) {
                throw new FlowExecutionException("找不到起始節點");
            }
            
            // 開始執行節點
            return executeNode(flow, startNode, context, executionId);
            
        } catch (Exception e) {
            executionStates.put(executionId, ExecutionStatus.FAILURE);
            throw e;
        }
    }
    
    @Override
    protected void doPause(Flow flow, BaseExecutionContext context) {
        String executionId = context.getExecutionId();
        if (executionId == null) {
            throw new FlowExecutionException("找不到執行ID");
        }
        executionStates.put(executionId, ExecutionStatus.PAUSED);
    }
    
    @Override
    protected ExecutionResult doResume(Flow flow, BaseExecutionContext context) {
        String executionId = context.getExecutionId();
        if (executionId == null) {
            throw new FlowExecutionException("找不到執行ID");
        }
        
        ExecutionStatus currentStatus = executionStates.get(executionId);
        if (currentStatus != ExecutionStatus.PAUSED) {
            throw new FlowExecutionException("流程不在暫停狀態");
        }
        
        // 從上次執行的節點繼續
        Node currentNode = getCurrentNode(flow, executionId);
        if (currentNode == null) {
            throw new FlowExecutionException("找不到當前節點");
        }
        
        return executeNode(flow, currentNode, context, executionId);
    }
    
    @Override
    protected void doTerminate(Flow flow, BaseExecutionContext context) {
        String executionId = context.getExecutionId();
        if (executionId == null) {
            throw new FlowExecutionException("找不到執行ID");
        }
        executionStates.put(executionId, ExecutionStatus.TERMINATED);
        executionContexts.remove(executionId);
    }
    
    @Override
    protected String doGetExecutionStatus(Flow flow, BaseExecutionContext context) {
        String executionId = context.getExecutionId();
        if (executionId == null) {
            throw new FlowExecutionException("找不到執行ID");
        }
        ExecutionStatus status = executionStates.get(executionId);
        return status != null ? status.name() : "UNKNOWN";
    }
    
    /**
     * 執行節點
     */
    private ExecutionResult executeNode(Flow flow, Node node, BaseExecutionContext context, String executionId) {
        // 檢查是否已終止或暫停
        ExecutionStatus currentStatus = executionStates.get(executionId);
        if (currentStatus == ExecutionStatus.TERMINATED || currentStatus == ExecutionStatus.PAUSED) {
            return ExecutionResult.failure(flow.getFlowId(), executionId, "Flow execution " + currentStatus.name());
        }

        // 取得節點執行器並執行
        NodeExecutor executor = nodeExecutorFactory.getExecutor(node);
        Map<String, Object> nodeContext = executionContexts.get(executionId);
        ExecutionResult nodeResult = executor.execute(node, context, nodeContext);

        // 根據執行結果決定後續節點
        if (nodeResult.getStatus() == ExecutionStatus.SUCCESS) {
            Node nextNode = null;
            if (node instanceof ProcessNode) {
                nextNode = flow.getNextNode((ProcessNode) node);
            } else if (node instanceof DecisionNode) {
                Boolean decision = null;
                if (nodeContext != null) {
                    Object v = nodeContext.get("decisionResult");
                    if (v instanceof Boolean) {
                        decision = (Boolean) v;
                    }
                }
                nextNode = flow.getNextNode((DecisionNode) node, decision != null ? decision : false);
            }

            if (nextNode != null) {
                return executeNode(flow, nextNode, context, executionId);
            } else {
                executionStates.put(executionId, ExecutionStatus.SUCCESS);
                return nodeResult;
            }
        } else {
            executionStates.put(executionId, ExecutionStatus.FAILURE);
            return nodeResult;
        }
    }

    /**
     * 取得當前執行節點（根據 executionContexts 中儲存的 currentNodeId）
     */
    private Node getCurrentNode(Flow flow, String executionId) {
        Map<String, Object> context = executionContexts.get(executionId);
        if (context == null) {
            return null;
        }
        String currentNodeId = (String) context.get("currentNodeId");
        if (currentNodeId == null) {
            return null;
        }
        return flow.findNodeById(currentNodeId);
    }
}