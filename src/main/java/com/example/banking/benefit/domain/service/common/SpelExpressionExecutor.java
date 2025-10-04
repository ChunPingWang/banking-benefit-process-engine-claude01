package com.example.banking.benefit.domain.service.common;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.exception.LogicCompositionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Spring Expression Language (SpEL) 表達式執行器
 * 提供表達式解析、執行和快取功能
 */
@Component
public class SpelExpressionExecutor {
    
    private static final Logger logger = LoggerFactory.getLogger(SpelExpressionExecutor.class);
    
    private final ExpressionParser parser;
    private final Map<String, Expression> expressionCache;
    
    public SpelExpressionExecutor() {
        // 配置 SpEL 解析器，允許 null 引用和自動類型轉換
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
        this.parser = new SpelExpressionParser(config);
        this.expressionCache = new ConcurrentHashMap<>();
    }
    
    /**
     * 執行 SpEL 表達式並返回布林值結果
     */
    public boolean evaluateAsBoolean(String expression, BaseExecutionContext context) {
        try {
            EvaluationContext evalContext = createEvaluationContext(context);
            Expression exp = getExpression(expression);
            
            Boolean result = exp.getValue(evalContext, Boolean.class);
            return result != null && result;
            
        } catch (Exception e) {
            logger.error("SpEL表達式執行失敗: {}", expression, e);
            throw new LogicCompositionException(
                String.format("SpEL表達式執行失敗: %s, 原因: %s", expression, e.getMessage()),
                e
            );
        }
    }
    
    /**
     * 執行 SpEL 表達式並返回指定類型的結果
     */
    public <T> T evaluate(String expression, BaseExecutionContext context, Class<T> resultType) {
        try {
            EvaluationContext evalContext = createEvaluationContext(context);
            Expression exp = getExpression(expression);
            
            return exp.getValue(evalContext, resultType);
            
        } catch (Exception e) {
            logger.error("SpEL表達式執行失敗: {}", expression, e);
            throw new LogicCompositionException(
                String.format("SpEL表達式執行失敗: %s, 原因: %s", expression, e.getMessage()),
                e
            );
        }
    }
    
    /**
     * 驗證 SpEL 表達式的語法
     */
    public boolean validateExpression(String expression) {
        try {
            parser.parseExpression(expression);
            return true;
        } catch (Exception e) {
            logger.warn("SpEL表達式語法無效: {}", expression);
            return false;
        }
    }
    
    /**
     * 從快取中獲取或解析表達式
     */
    private Expression getExpression(String expression) {
        return expressionCache.computeIfAbsent(expression, parser::parseExpression);
    }
    
    /**
     * 建立評估上下文
     */
    private EvaluationContext createEvaluationContext(BaseExecutionContext context) {
        StandardEvaluationContext evalContext = new StandardEvaluationContext();
        
        // 設置基本變數
        evalContext.setVariable("context", context);
        evalContext.setVariable("customer", context.getCustomerData());
        
        // 設置所有上下文變數
        context.getVariables().forEach(evalContext::setVariable);
        
        // 設置一些常用的函數
        // TODO: 加入更多自定義函數
        
        return evalContext;
    }
    
    /**
     * 清除表達式快取
     */
    public void clearExpressionCache() {
        expressionCache.clear();
    }
}