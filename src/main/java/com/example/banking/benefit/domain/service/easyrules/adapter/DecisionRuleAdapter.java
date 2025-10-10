package com.example.banking.benefit.domain.service.easyrules.adapter;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.node.DecisionType;
import com.example.banking.benefit.domain.service.expression.ExpressionEvaluator;
import com.example.banking.benefit.domain.service.expression.JavaClassEvaluator;
import com.example.banking.benefit.domain.service.expression.SpelExpressionEvaluator;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * DecisionNode 適配為 Easy Rules Rule 的適配器
 * 將現有的決策節點轉換為 Easy Rules 可以執行的規則
 */
@Rule
public class DecisionRuleAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(DecisionRuleAdapter.class);
    
    private final DecisionNode decisionNode;
    private final ExpressionEvaluator javaClassEvaluator;
    private final ExpressionEvaluator spelExpressionEvaluator;
    private boolean conditionResult = false;
    
    public DecisionRuleAdapter(DecisionNode decisionNode) {
        this.decisionNode = decisionNode;
        this.javaClassEvaluator = new JavaClassEvaluator();
        this.spelExpressionEvaluator = new SpelExpressionEvaluator();
    }
    
    @Condition
    public boolean when(@Fact("context") BaseExecutionContext context) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("nodeId", decisionNode.getNodeId());
            variables.put("nodeName", decisionNode.getNodeName());
            
            if (decisionNode.getDecisionType() == DecisionType.SPEL) {
                String expression = decisionNode.getSpelExpression();
                conditionResult = spelExpressionEvaluator.evaluateCondition(expression, context, variables);
            } else if (decisionNode.getDecisionType() == DecisionType.JAVA_CLASS) {
                String className = decisionNode.getImplementationClass();
                conditionResult = javaClassEvaluator.evaluateCondition(className, context, variables);
            }
            
            logger.debug("Decision rule {} evaluated to: {}", decisionNode.getNodeId(), conditionResult);
            return conditionResult;
            
        } catch (Exception e) {
            logger.error("Error evaluating decision rule {}: {}", decisionNode.getNodeId(), e.getMessage(), e);
            conditionResult = false;
            return false;
        }
    }
    
    @Action
    public void then(@Fact("context") BaseExecutionContext context) {
        logger.info("Executing action for decision rule: {} with result: {}", 
                   decisionNode.getNodeId(), conditionResult);
        
        // 將結果存入上下文中
        context.addVariable("decision_" + decisionNode.getNodeId(), conditionResult);
        context.addVariable("lastExecutedNode", decisionNode.getNodeId());
    }
    
    @Priority
    public int getPriority() {
        // 使用節點順序作為優先級，如果沒有設置則使用默認值
        return decisionNode.getNodeOrder() != null ? decisionNode.getNodeOrder() : 100;
    }
    
    // Getters
    public DecisionNode getDecisionNode() {
        return decisionNode;
    }
    
    public boolean getConditionResult() {
        return conditionResult;
    }
    
    public String getName() {
        return decisionNode.getNodeName();
    }
    
    public String getDescription() {
        return decisionNode.getDescription();
    }
}