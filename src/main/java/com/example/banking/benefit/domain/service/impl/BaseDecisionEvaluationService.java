package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.decision.DecisionCommand;
import com.example.banking.benefit.domain.model.decision.DecisionType;
import com.example.banking.benefit.domain.service.DecisionEvaluationService;
import com.example.banking.benefit.domain.exception.DecisionEvaluationException;
import com.example.banking.benefit.domain.service.common.SpelExpressionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 決策節點評估服務的基礎實作
 */
@Service
public class BaseDecisionEvaluationService implements DecisionEvaluationService {
    
    // 錯誤碼定義
    private static final String ERROR_CODE_INVALID_EXPRESSION = "DECISION_002";
    private static final String ERROR_CODE_EVALUATION_ERROR = "DECISION_003";
    
    private static final Logger logger = LoggerFactory.getLogger(BaseDecisionEvaluationService.class);
    
    // 用於儲存評估結果的快取
    private final Map<String, Boolean> resultCache = new ConcurrentHashMap<>();

    @Override
    public boolean evaluate(DecisionNode node, BaseExecutionContext context) {
        if (!canEvaluate(node, context)) {
            throw DecisionEvaluationException.evaluationError("無法評估決策節點: " + node.getNodeId(), null);
        }

        try {
            logger.info("開始評估決策節點: {}", node.getNodeId());
            
            boolean result;
            
            // 根據決策類型執行不同評估邏輯
            switch (node.getDecisionType()) {
                case JAVA_CLASS:
                    result = evaluateJavaImplementation(node, context);
                    break;
                case SPEL:
                    result = evaluateSpelExpression(node, context);
                    break;
                default:
                    throw DecisionEvaluationException.evaluationError("不支援的決策類型: " + node.getDecisionType(), null);
            }
            
            // 儲存評估結果
            String cacheKey = generateCacheKey(node, context);
            resultCache.put(cacheKey, result);
            
            logger.info("決策節點評估完成: {}, 結果: {}", node.getNodeId(), result);
            return result;
            
        } catch (Exception e) {
            logger.error("決策節點評估失敗: {}", node.getNodeId(), e);
            throw DecisionEvaluationException.evaluationError("決策節點評估失敗", e);
        }
    }

    @Override
    public boolean canEvaluate(DecisionNode node, BaseExecutionContext context) {
        // 檢查節點是否為空
        if (node == null) {
            return false;
        }

        // 檢查執行內容是否為空
        if (context == null) {
            return false;
        }

        // 檢查決策類型是否支援
        if (node.getDecisionType() == null) {
            return false;
        }

        // 檢查實作類別或表達式是否存在
        switch (node.getDecisionType()) {
            case JAVA_CLASS:
                if (node.getImplementationClass() == null) {
                    return false;
                }
                try {
                    // 載入決策類別並檢查是否實作正確介面
                    Class<?> decisionClass = Class.forName(node.getImplementationClass());
                    return DecisionCommand.class.isAssignableFrom(decisionClass);
                } catch (Exception e) {
                    logger.warn("檢查決策類別載入失敗: {}", node.getNodeId(), e);
                    return false;
                }
            case SPEL:
                String expression = node.getSpelExpression();
                return expression != null && !expression.trim().isEmpty();
            default:
                return false;
        }
    }

    @Override
    public boolean isEligible(DecisionNode node, BaseExecutionContext context) {
        // 檢查節點是否可評估
        if (!canEvaluate(node, context)) {
            return false;
        }

        // 檢查實作類別或表達式是否存在
        switch (node.getDecisionType()) {
            case JAVA_CLASS:
                try {
                    // 載入決策類別並檢查是否可執行
                    Class<?> decisionClass = Class.forName(node.getImplementationClass());
                    DecisionCommand command = (DecisionCommand) decisionClass.getDeclaredConstructor().newInstance();
                    return command.canExecute(context);
                } catch (Exception e) {
                    logger.warn("檢查決策節點執行條件失敗: {}", node.getNodeId(), e);
                    return false;
                }
            case SPEL:
                String expression = node.getSpelExpression();
                return spelExecutor.validateExpression(expression);
            default:
                return false;
        }
    }

    /**
     * 評估 Java 類別實作
     */
    private boolean evaluateJavaImplementation(DecisionNode node, BaseExecutionContext context) {
        String className = node.getImplementationClass();
        if (className == null || className.trim().isEmpty()) {
            throw DecisionEvaluationException.classNotFound("實作類別名稱不可為空");
        }

        try {
            // 載入類別
            Class<?> decisionClass = Class.forName(className);
            
            // 檢查類別是否實作決策介面
            if (!DecisionCommand.class.isAssignableFrom(decisionClass)) {
                throw DecisionEvaluationException.classNotFound(
                    String.format("類別 %s 必須實作 DecisionCommand 介面", className)
                );
            }
            
            // 建立實例
            DecisionCommand command = (DecisionCommand) decisionClass.getDeclaredConstructor().newInstance();
            
            // 執行決策邏輯
            return command.evaluate(context);
            
        } catch (ClassNotFoundException e) {
            throw DecisionEvaluationException.classNotFound(className);
        } catch (Exception e) {
            throw DecisionEvaluationException.evaluationError(
                String.format("執行 Java 類別 %s 失敗", className),
                e
            );
        }
    }

    /**
     * 評估 SpEL 表達式
     */
    @Autowired
    private SpelExpressionExecutor spelExecutor;

    private boolean evaluateSpelExpression(DecisionNode node, BaseExecutionContext context) {
        String expression = node.getSpelExpression();
        if (expression == null || expression.trim().isEmpty()) {
            throw new DecisionEvaluationException(
                ERROR_CODE_INVALID_EXPRESSION,
                "SpEL表達式不可為空"
            );
        }

        try {
            // 使用 SpEL 表達式執行器評估表達式
            return spelExecutor.evaluateAsBoolean(expression, context);
        } catch (Exception e) {
            throw new DecisionEvaluationException(
                ERROR_CODE_EVALUATION_ERROR,
                String.format("SpEL表達式評估失敗: %s", e.getMessage()),
                e
            );
        }
    }

    /**
     * 生成快取鍵值
     */
    private String generateCacheKey(DecisionNode node, BaseExecutionContext context) {
        return String.format("%s_%s_%s", 
            node.getNodeId(), 
            context.getCustomerId(),
            context.getExecutionId()
        );
    }
}