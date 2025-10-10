package com.example.banking.benefit.domain.service.executor;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.exception.FlowExecutionException;
import com.example.banking.benefit.domain.model.flow.FlowId;

import java.util.Map;

import com.example.banking.benefit.domain.service.expression.ExpressionEvaluator;
import com.example.banking.benefit.domain.service.expression.JavaClassEvaluator;
import com.example.banking.benefit.domain.service.expression.SpelExpressionEvaluator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 決策節點執行器 (原有實作)
 * 當 Easy Rules 未啟用時使用此執行器
 */
@Component
@ConditionalOnProperty(name = "banking.benefit.decision.engine", havingValue = "legacy", matchIfMissing = true)
public class DecisionNodeExecutor implements NodeExecutor {

    private final ExpressionEvaluator javaClassEvaluator;
    private final ExpressionEvaluator spelExpressionEvaluator;
    
    public DecisionNodeExecutor() {
        this.javaClassEvaluator = new JavaClassEvaluator();
        this.spelExpressionEvaluator = new SpelExpressionEvaluator();
    }
    
    @Override
    public ExecutionResult execute(Node node, BaseExecutionContext context, Map<String, Object> nodeContext) {
        if (!(node instanceof DecisionNode)) {
            throw new FlowExecutionException("非決策節點類型");
        }
        
        DecisionNode decisionNode = (DecisionNode) node;
        nodeContext.put("currentNodeId", decisionNode.getNodeId());
        
        try {
            // 執行決策邏輯
            boolean result;
            if (decisionNode.getImplementationClass() != null) {
                result = executeJavaImplementation(decisionNode, context, nodeContext);
            } else if (decisionNode.getSpelExpression() != null) {
                result = executeSpelExpression(decisionNode, context, nodeContext);
            } else {
                throw new FlowExecutionException("節點未設定決策邏輯");
            }
            
            // 記錄決策結果
            nodeContext.put("decisionResult", result);
            
            return ExecutionResult.success(
                FlowId.of(context.getFlowId()), 
                (String) nodeContext.get("executionId"),
                nodeContext
            );
            
        } catch (Exception e) {
            return ExecutionResult.failure(
                FlowId.of(context.getFlowId()),
                (String) nodeContext.get("executionId"),
                "決策節點執行失敗: " + e.getMessage()
            );
        }
    }
    
    @Override
    public boolean supports(Node node) {
        return node instanceof DecisionNode;
    }
    
    /**
     * 執行 Java 實作類別
     */
    private boolean executeJavaImplementation(DecisionNode node, BaseExecutionContext context, Map<String, Object> nodeContext) {
        String className = node.getImplementationClass();
        return javaClassEvaluator.evaluateCondition(className, context, nodeContext);
    }
    
    /**
     * 執行 SpEL 表達式
     */
    private boolean executeSpelExpression(DecisionNode node, BaseExecutionContext context, Map<String, Object> nodeContext) {
        String expression = node.getSpelExpression();
        return spelExpressionEvaluator.evaluateCondition(expression, context, nodeContext);
    }
}