package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.result.ExecutionStatus;
import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.process.ProcessNode;
import com.example.banking.benefit.domain.service.FlowExecutionService;
import com.example.banking.benefit.domain.service.ProcessExecutionService;
import com.example.banking.benefit.domain.service.DecisionEvaluationService;
import com.example.banking.benefit.domain.exception.FlowExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程執行服務的基礎實作
 */
@Service
public class BaseFlowExecutionService implements FlowExecutionService {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseFlowExecutionService.class);
    
    private final ProcessExecutionService processExecutionService;
    private final DecisionEvaluationService decisionEvaluationService;
    
    // 用於儲存流程執行狀態的快取
    private final Map<String, String> statusCache = new ConcurrentHashMap<>();
    
    public BaseFlowExecutionService(
            ProcessExecutionService processExecutionService,
            DecisionEvaluationService decisionEvaluationService) {
        this.processExecutionService = processExecutionService;
        this.decisionEvaluationService = decisionEvaluationService;
    }

    @Override
    @Transactional
    public ExecutionResult execute(Flow flow, BaseExecutionContext context) {
        try {
            // 記錄開始執行
            logger.info("開始執行流程: {}, 執行ID: {}", flow.getFlowId(), context.getExecutionId());
            updateExecutionStatus(flow, context, ExecutionStatus.IN_PROGRESS);
            
            // 驗證流程定義
            validateFlow(flow);
            
            // 取得起始節點
            Optional<Node> currentNodeOpt = flow.getStartNode();
            if (currentNodeOpt.isEmpty()) {
                throw new FlowExecutionException("找不到流程起始節點");
            }
            
            // 開始執行流程邏輯
            while (currentNodeOpt.isPresent()) {
                Node currentNode = currentNodeOpt.get();
                // 檢查流程是否被暫停或中止
                var status = getExecutionStatus(flow, context);
                if (ExecutionStatus.PAUSED.name().equals(status)) {
                    return ExecutionResult.paused(flow.getFlowId(), context.getExecutionId());
                }
                if (ExecutionStatus.TERMINATED.name().equals(status)) {
                    return ExecutionResult.terminated(flow.getFlowId(), context.getExecutionId(), "流程被手動中止");
                }
                
                try {
                    // 根據節點類型執行不同邏輯
                    if (currentNode instanceof DecisionNode) {
                        var decision = (DecisionNode) currentNode;
                        boolean result = decisionEvaluationService.evaluate(decision, context);
                        // 根據決策結果選擇下一個節點
                        currentNodeOpt = flow.getNextNode(decision.getNodeId(), result);
                    } else if (currentNode instanceof ProcessNode) {
                        var process = (ProcessNode) currentNode;
                        processExecutionService.execute(process, context);
                        // 取得下一個節點
                        currentNodeOpt = flow.getNextNode(process.getNodeId(), true); // Assuming process node always goes to next
                    } else {
                        throw new FlowExecutionException("未知的節點類型: " + currentNode.getClass().getName());
                    }
                } catch (Exception e) {
                    logger.error("節點執行失敗: {}", currentNode.getNodeId(), e);
                    throw new FlowExecutionException("節點執行失敗", e);
                }
            }
            
            // 記錄完成執行
            logger.info("流程執行完成: {}", flow.getFlowId());
            updateExecutionStatus(flow, context, ExecutionStatus.SUCCESS);
            return ExecutionResult.success(flow.getFlowId(), context.getExecutionId(), context.getVariables());
            
        } catch (Exception e) {
            // 記錄執行失敗
            logger.error("流程執行失敗: {}", flow.getFlowId(), e);
            updateExecutionStatus(flow, context, ExecutionStatus.FAILURE);
            return ExecutionResult.failure(flow.getFlowId(), context.getExecutionId(), e.getMessage());
        }
    }

    @Override
    @Transactional
    public void pause(Flow flow, BaseExecutionContext context) {
        logger.info("暫停流程執行: {}", flow.getFlowId());
        updateExecutionStatus(flow, context, ExecutionStatus.PAUSED);
    }

    @Override
    @Transactional
    public ExecutionResult resume(Flow flow, BaseExecutionContext context) {
        logger.info("繼續執行流程: {}", flow.getFlowId());
        updateExecutionStatus(flow, context, ExecutionStatus.IN_PROGRESS);
        return execute(flow, context);
    }

    @Override
    @Transactional
    public void terminate(Flow flow, BaseExecutionContext context) {
        logger.info("中止流程執行: {}", flow.getFlowId());
        updateExecutionStatus(flow, context, ExecutionStatus.TERMINATED);
    }

    @Override
    public String getExecutionStatus(Flow flow, BaseExecutionContext context) {
        String cacheKey = generateStatusCacheKey(flow, context);
        return statusCache.getOrDefault(cacheKey, ExecutionStatus.IN_PROGRESS.name());
    }
    
    /**
     * 驗證流程定義
     */
    private void validateFlow(Flow flow) {
        if (flow == null) {
            throw new FlowExecutionException("流程定義不可為空");
        }
        if (!flow.isValid()) {
            throw new FlowExecutionException("流程定義無效: " + flow.getFlowId());
        }
    }
    
    /**
     * 更新流程執行狀態
     */
    private void updateExecutionStatus(Flow flow, BaseExecutionContext context, ExecutionStatus status) {
        String cacheKey = generateStatusCacheKey(flow, context);
        statusCache.put(cacheKey, status.name());
    }
    
    /**
     * 生成狀態快取鍵值
     */
    private String generateStatusCacheKey(Flow flow, BaseExecutionContext context) {
        return String.format("%s_%s", flow.getFlowId().toString(), context.getExecutionId());
    }
}