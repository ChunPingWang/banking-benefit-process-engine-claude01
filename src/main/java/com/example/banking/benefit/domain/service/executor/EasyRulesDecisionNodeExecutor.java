package com.example.banking.benefit.domain.service.executor;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.service.easyrules.engine.EasyRulesDecisionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Easy Rules 決策節點執行器
 * 使用 Easy Rules 框架來執行決策節點，替代原有的 DecisionNodeExecutor
 */
@Component
@ConditionalOnProperty(name = "banking.benefit.decision.engine", havingValue = "easy-rules", matchIfMissing = false)
public class EasyRulesDecisionNodeExecutor implements NodeExecutor {
    
    private static final Logger logger = LoggerFactory.getLogger(EasyRulesDecisionNodeExecutor.class);
    
    private final EasyRulesDecisionEngine easyRulesEngine;
    
    @Autowired
    public EasyRulesDecisionNodeExecutor(EasyRulesDecisionEngine easyRulesEngine) {
        this.easyRulesEngine = easyRulesEngine;
        logger.info("EasyRulesDecisionNodeExecutor initialized successfully");
    }
    
    @Override
    public ExecutionResult execute(Node node, BaseExecutionContext context, Map<String, Object> nodeContext) {
        if (!(node instanceof DecisionNode)) {
            throw new IllegalArgumentException("Node must be a DecisionNode instance");
        }
        
        DecisionNode decisionNode = (DecisionNode) node;
        
        logger.debug("Executing decision node {} using Easy Rules engine", decisionNode.getNodeId());
        
        try {
            // 將 nodeContext 添加到執行上下文中
            if (nodeContext != null && !nodeContext.isEmpty()) {
                nodeContext.forEach(context::addVariable);
            }
            
            // 使用 Easy Rules 引擎執行決策
            ExecutionResult result = easyRulesEngine.executeDecision(decisionNode, context);
            
            // 取得決策結果並放入 nodeContext
            if (nodeContext != null) {
                String decisionKey = "decision_" + decisionNode.getNodeId();
                Object decisionResult = context.getVariable(decisionKey);
                nodeContext.put("decisionResult", decisionResult);
                nodeContext.put("executionEngine", "easy-rules");
            }
            
            logger.info("Decision node {} executed successfully using Easy Rules", decisionNode.getNodeId());
            return result;
            
        } catch (Exception e) {
            logger.error("Error executing decision node {} with Easy Rules: {}", 
                        decisionNode.getNodeId(), e.getMessage(), e);
            
            return ExecutionResult.failure(
                context.getFlowId() != null ? FlowId.of(context.getFlowId()) : FlowId.of("unknown"),
                "ER-ERROR-" + System.currentTimeMillis(),
                "Easy Rules execution failed: " + e.getMessage()
            );
        }
    }
    
    @Override
    public boolean supports(Node node) {
        if (!(node instanceof DecisionNode)) {
            return false;
        }
        
        DecisionNode decisionNode = (DecisionNode) node;
        return easyRulesEngine.canHandle(decisionNode);
    }
    
    /**
     * 取得引擎統計資訊
     */
    public String getEngineInfo() {
        return easyRulesEngine.getEngineStats();
    }
}