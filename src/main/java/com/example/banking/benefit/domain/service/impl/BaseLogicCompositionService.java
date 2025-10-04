package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.service.DecisionEvaluationService;
import com.example.banking.benefit.domain.service.LogicCompositionService;
import com.example.banking.benefit.domain.exception.LogicCompositionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 邏輯組合服務的基礎實作
 */
@Service
public class BaseLogicCompositionService implements LogicCompositionService {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseLogicCompositionService.class);
    
    private final DecisionEvaluationService decisionEvaluationService;
    private final ExpressionParser expressionParser;
    
    public BaseLogicCompositionService(DecisionEvaluationService decisionEvaluationService) {
        this.decisionEvaluationService = decisionEvaluationService;
        this.expressionParser = new SpelExpressionParser();
    }

    @Override
    public boolean composeDecisions(List<DecisionNode> decisions, String operator, BaseExecutionContext context) {
        if (decisions == null || decisions.isEmpty()) {
            throw new LogicCompositionException("決策節點列表不可為空");
        }

        if (operator == null || operator.trim().isEmpty()) {
            throw new LogicCompositionException("邏輯運算子不可為空");
        }

        try {
            switch (operator.toUpperCase()) {
                case "AND":
                    return decisions.stream()
                            .allMatch(node -> decisionEvaluationService.evaluate(node, context));
                case "OR":
                    return decisions.stream()
                            .anyMatch(node -> decisionEvaluationService.evaluate(node, context));
                default:
                    throw new LogicCompositionException("不支援的邏輯運算子: " + operator);
            }
        } catch (Exception e) {
            logger.error("組合決策節點失敗", e);
            throw new LogicCompositionException("組合決策節點失敗", e);
        }
    }

    @Override
    public boolean checkTransitionCondition(Object sourceNode, Object targetNode, BaseExecutionContext context) {
        if (sourceNode == null || targetNode == null) {
            return false;
        }

        // TODO: 實作節點轉換條件檢查邏輯
        return true;
    }

    @Override
    public boolean evaluateExpression(String expression, BaseExecutionContext context) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new LogicCompositionException("表達式不可為空");
        }

        try {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
            
            // 設置表達式上下文
            evaluationContext.setVariable("context", context);
            evaluationContext.setVariable("customer", context.getCustomerData());
            evaluationContext.setVariable("variables", context.getVariables());
            
            // 評估表達式
            Boolean result = expressionParser.parseExpression(expression)
                    .getValue(evaluationContext, Boolean.class);
                    
            return result != null && result;
            
        } catch (Exception e) {
            logger.error("評估表達式失敗: {}", expression, e);
            throw new LogicCompositionException("評估表達式失敗", e);
        }
    }

    @Override
    public boolean validateExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }

        try {
            expressionParser.parseExpression(expression);
            return true;
        } catch (Exception e) {
            logger.warn("表達式語法無效: {}", expression);
            return false;
        }
    }
}