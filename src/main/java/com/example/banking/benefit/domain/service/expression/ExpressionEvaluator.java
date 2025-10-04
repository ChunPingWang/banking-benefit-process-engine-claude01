package com.example.banking.benefit.domain.service.expression;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import java.util.Map;

/**
 * 表達式執行服務介面
 */
public interface ExpressionEvaluator {
    
    /**
     * 執行 SpEL 表達式並返回布林值
     *
     * @param expression SpEL 表達式
     * @param context 執行上下文
     * @param variables 變數集合
     * @return 表達式執行結果
     */
    boolean evaluateCondition(String expression, BaseExecutionContext context, Map<String, Object> variables);
    
    /**
     * 執行 SpEL 表達式並返回物件
     *
     * @param expression SpEL 表達式
     * @param context 執行上下文
     * @param variables 變數集合
     * @param expectedType 期望的回傳類型
     * @return 表達式執行結果
     */
    <T> T evaluateExpression(String expression, BaseExecutionContext context, Map<String, Object> variables, Class<T> expectedType);
    
    /**
     * 驗證表達式語法
     *
     * @param expression SpEL 表達式
     * @return 是否有效
     */
    boolean validateExpression(String expression);
}