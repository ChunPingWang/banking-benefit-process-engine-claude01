package com.example.banking.benefit.domain.service.expression;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JavaClassEvaluatorTest {

    private JavaClassEvaluator evaluator;

    @Mock
    private BaseExecutionContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        evaluator = new JavaClassEvaluator();

        // 準備測試數據
        when(context.getExecutionId()).thenReturn("test-execution");
        when(context.getFlowId()).thenReturn("test-flow");
        when(context.getCustomerId()).thenReturn("test-customer");
        Map<String, Object> customerData = new HashMap<>();
        customerData.put("age", 30);
        customerData.put("vip", true);
        when(context.getCustomerData()).thenReturn(customerData);
    }

    @Test
    void evaluateCondition_WithValidClass_ShouldReturnTrue() {
        String className = TestConditionEvaluator.class.getName();
        assertTrue(evaluator.evaluateCondition(className, context, null));
    }

    @Test
    void evaluateExpression_WithValidClass_ShouldReturnExpectedResult() {
        String className = TestExpressionProcessor.class.getName();
        String result = evaluator.evaluateExpression(className, context, null, String.class);
        assertEquals("test-customer:vip", result);
    }

    @Test
    void validateExpression_WithValidClass_ShouldReturnTrue() {
        String className = TestConditionEvaluator.class.getName();
        assertTrue(evaluator.validateExpression(className));
    }

    @Test
    void validateExpression_WithInvalidClass_ShouldReturnFalse() {
        String className = "com.example.NonExistentClass";
        assertFalse(evaluator.validateExpression(className));
    }

    @Test
    void evaluateCondition_WithInvalidClass_ShouldThrowException() {
        String className = "com.example.NonExistentClass";
        assertThrows(ExpressionEvaluationException.class, () ->
            evaluator.evaluateCondition(className, context, null));
    }

    // 測試用的條件評估類別
    public static class TestConditionEvaluator {
        public boolean evaluate(BaseExecutionContext context, Map<String, Object> variables) {
            Map<String, Object> customerData = context.getCustomerData();
            return (boolean) customerData.get("vip");
        }
    }

    // 測試用的表達式處理類別
    public static class TestExpressionProcessor {
        public String process(BaseExecutionContext context, Map<String, Object> variables) {
            Map<String, Object> customerData = context.getCustomerData();
            boolean isVip = (boolean) customerData.get("vip");
            return context.getCustomerId() + ":" + (isVip ? "vip" : "normal");
        }
    }
}