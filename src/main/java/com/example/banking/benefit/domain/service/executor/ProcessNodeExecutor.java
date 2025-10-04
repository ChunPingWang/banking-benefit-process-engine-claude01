package com.example.banking.benefit.domain.service.executor;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.process.ProcessNode;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.exception.FlowExecutionException;
import com.example.banking.benefit.domain.model.flow.FlowId;

import java.util.Map;

import com.example.banking.benefit.domain.service.expression.ExpressionEvaluator;
import com.example.banking.benefit.domain.service.expression.JavaClassEvaluator;
import com.example.banking.benefit.domain.service.expression.SpelExpressionEvaluator;

/**
 * 處理節點執行器
 */
public class ProcessNodeExecutor implements NodeExecutor {
    
    private final ExpressionEvaluator javaClassEvaluator;
    private final ExpressionEvaluator spelExpressionEvaluator;
    
    public ProcessNodeExecutor() {
        this.javaClassEvaluator = new JavaClassEvaluator();
        this.spelExpressionEvaluator = new SpelExpressionEvaluator();
    }
    
    @Override
    public ExecutionResult execute(Node node, BaseExecutionContext context, Map<String, Object> nodeContext) {
        if (!(node instanceof ProcessNode)) {
            throw new FlowExecutionException("非處理節點類型");
        }
        
        ProcessNode processNode = (ProcessNode) node;
        nodeContext.put("currentNodeId", processNode.getNodeId());
        
        try {
            // 執行處理邏輯
            if (processNode.getImplementationClass() != null) {
                return executeJavaImplementation(processNode, context, nodeContext);
            } else if (processNode.getSpelExpression() != null) {
                return executeSpelExpression(processNode, context, nodeContext);
            } else {
                throw new FlowExecutionException("節點未設定執行邏輯");
            }
        } catch (Exception e) {
            return ExecutionResult.failure(
                FlowId.of(context.getFlowId()),
                (String) nodeContext.get("executionId"),
                "處理節點執行失敗: " + e.getMessage()
            );
        }
    }
    
    @Override
    public boolean supports(Node node) {
        return node instanceof ProcessNode;
    }
    
    /**
     * 執行 Java 實作類別
     */
    private ExecutionResult executeJavaImplementation(ProcessNode node, BaseExecutionContext context, Map<String, Object> nodeContext) {
        String className = node.getImplementationClassName();
        try {
            Object result = javaClassEvaluator.evaluateExpression(className, context, nodeContext, Object.class);
            nodeContext.put("processResult", result);
            return ExecutionResult.success(FlowId.of(context.getFlowId()), (String) nodeContext.get("executionId"), nodeContext);
        } catch (Exception e) {
            return ExecutionResult.failure(FlowId.of(context.getFlowId()), (String) nodeContext.get("executionId"), e.getMessage());
        }
    }
    
    /**
     * 執行 SpEL 表達式
     */
    private ExecutionResult executeSpelExpression(ProcessNode node, BaseExecutionContext context, Map<String, Object> nodeContext) {
        String expression = node.getSpelExpression();
        try {
            Object result = spelExpressionEvaluator.evaluateExpression(expression, context, nodeContext, Object.class);
            nodeContext.put("processResult", result);
            return ExecutionResult.success(FlowId.of(context.getFlowId()), (String) nodeContext.get("executionId"), nodeContext);
        } catch (Exception e) {
            return ExecutionResult.failure(FlowId.of(context.getFlowId()), (String) nodeContext.get("executionId"), e.getMessage());
        }
    }
}