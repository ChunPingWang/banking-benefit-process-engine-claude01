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

class SpelExpressionEvaluatorTest {

    private SpelExpressionEvaluator evaluator;

    @Mock
    private BaseExecutionContext context;

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

    @Test
    void evaluateCondition_WithSimpleExpression_ShouldReturnTrue() {
        String expression = "#{1 + 1 == 2}";
        assertTrue(evaluator.evaluateCondition(expression, context, null));
    }

    @Test
    void evaluateCondition_WithContextVariables_ShouldReturnTrue() {
        String expression = "#{#customerData.get('age') > 25 and #customerData.get('vip')}";
        assertTrue(evaluator.evaluateCondition(expression, context, null));
    }

    @Test
    void evaluateCondition_WithAdditionalVariables_ShouldReturnTrue() {
        String expression = "#{#score > 80 and #customerData.get('vip')}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("score", 90);
        assertTrue(evaluator.evaluateCondition(expression, context, variables));
    }

    @Test
    void evaluateExpression_WithStringTemplate_ShouldReturnExpectedResult() {
        String expression = "#{#flowId + '-' + #executionId}";
        String result = evaluator.evaluateExpression(expression, context, null, String.class);
        assertEquals("test-flow-test-execution", result);
    }

    @Test
    void evaluateExpression_WithMathOperation_ShouldReturnExpectedResult() {
        String expression = "#{#customerData.get('age') * 2}";
        Integer result = evaluator.evaluateExpression(expression, context, null, Integer.class);
        assertEquals(60, result);
    }

    @Test
    void validateExpression_WithValidExpression_ShouldReturnTrue() {
        String expression = "#{1 + 1}";
        assertTrue(evaluator.validateExpression(expression));
    }

    @Test
    void validateExpression_WithInvalidExpression_ShouldReturnFalse() {
        String expression = "#{1 + }";
        assertFalse(evaluator.validateExpression(expression));
    }
}