package com.example.banking.benefit.domain.service.expression;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * SpEL表達式求值器的單元測試類
 *
 * 本測試類主要驗證SpEL表達式求值器的以下功能：
 * 1. 簡單表達式求值
 * 2. 存取執行上下文變數
 * 3. 存取客戶資料屬性
 * 4. 支援額外變數注入
 * 5. 字串模板處理
 * 6. 數學運算處理
 * 7. 表達式語法驗證
 *
 * @see SpelExpressionEvaluator
 * @see BaseExecutionContext
 * @see CustomerData
 */
class SpelExpressionEvaluatorTest {

    private SpelExpressionEvaluator evaluator;

    @Mock
    private BaseExecutionContext context;

    /**
     * 測試前的初始化設定
     * 準備模擬物件與測試資料，包含：
     * 1. 初始化SpelExpressionEvaluator
     * 2. 設定模擬的ExecutionContext
     * 3. 準備客戶數據與屬性
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        evaluator = new SpelExpressionEvaluator();

        // 準備測試數據
        when(context.getExecutionId()).thenReturn("test-execution");
        when(context.getFlowId()).thenReturn("test-flow");
        when(context.getCustomerId()).thenReturn("test-customer");
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("age", CustomerAttribute.of(30, Integer.class));
        attributes.put("vip", CustomerAttribute.of(true, Boolean.class));
        CustomerData customerData = CustomerData.create(attributes);
        when(context.getCustomerData()).thenReturn(customerData);
    }

    /**
     * 測試簡單表達式求值
     * 驗證基本數學運算表達式能否正確求值
     */
    @Test
    void evaluateCondition_WithSimpleExpression_ShouldReturnTrue() {
        String expression = "#{1 + 1 == 2}";
        assertTrue(evaluator.evaluateCondition(expression, context, null));
    }

    /**
     * 測試執行上下文變數存取
     * 驗證能否正確存取CustomerData中的屬性並進行邏輯運算
     */
    @Test
    void evaluateCondition_WithContextVariables_ShouldReturnTrue() {
        String expression = "#{#customerData.get('age') > 25 and #customerData.get('vip')}";
        assertTrue(evaluator.evaluateCondition(expression, context, null));
    }

    /**
     * 測試額外變數注入
     * 驗證能否在表達式中使用外部注入的變數進行運算
     */
    @Test
    void evaluateCondition_WithAdditionalVariables_ShouldReturnTrue() {
        String expression = "#{#score > 80 and #customerData.get('vip')}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("score", 90);
        assertTrue(evaluator.evaluateCondition(expression, context, variables));
    }

    /**
     * 測試字串模板處理
     * 驗證能否正確組合上下文變數為字串
     */
    @Test
    void evaluateExpression_WithStringTemplate_ShouldReturnExpectedResult() {
        String expression = "#{#flowId + '-' + #executionId}";
        String result = evaluator.evaluateExpression(expression, context, null, String.class);
        assertEquals("test-flow-test-execution", result);
    }

    /**
     * 測試數學運算處理
     * 驗證能否正確執行數學計算並轉換為指定的返回類型
     */
    @Test
    void evaluateExpression_WithMathOperation_ShouldReturnExpectedResult() {
        String expression = "#{#customerData.get('age') * 2}";
        Integer result = evaluator.evaluateExpression(expression, context, null, Integer.class);
        assertEquals(60, result);
    }

    /**
     * 測試有效表達式驗證
     * 驗證語法正確的表達式能否通過驗證
     */
    @Test
    void validateExpression_WithValidExpression_ShouldReturnTrue() {
        String expression = "#{1 + 1}";
        assertTrue(evaluator.validateExpression(expression));
    }

    /**
     * 測試無效表達式驗證
     * 驗證語法錯誤的表達式能否被正確識別
     */
    @Test
    void validateExpression_WithInvalidExpression_ShouldReturnFalse() {
        String expression = "#{1 + }";
        assertFalse(evaluator.validateExpression(expression));
    }
}