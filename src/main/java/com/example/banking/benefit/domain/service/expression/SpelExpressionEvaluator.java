package com.example.banking.benefit.domain.service.expression;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.common.TemplateParserContext;

import java.util.Map;

/**
 * Spring Expression Language (SpEL) 表達式執行器
 */
public class SpelExpressionEvaluator implements ExpressionEvaluator {
    
    private final ExpressionParser parser;
    private final ParserContext parserContext;
    
    public SpelExpressionEvaluator() {
        this.parser = new SpelExpressionParser();
        this.parserContext = new TemplateParserContext();
    }
    
    @Override
    public boolean evaluateCondition(String expression, BaseExecutionContext context, Map<String, Object> variables) {
        Expression exp = parser.parseExpression(expression, parserContext);
        StandardEvaluationContext evalContext = createEvaluationContext(context, variables);
        Boolean result = exp.getValue(evalContext, Boolean.class);
        return Boolean.TRUE.equals(result);
    }
    
    @Override
    public <T> T evaluateExpression(String expression, BaseExecutionContext context, Map<String, Object> variables, Class<T> expectedType) {
        Expression exp = parser.parseExpression(expression, parserContext);
        StandardEvaluationContext evalContext = createEvaluationContext(context, variables);
        return exp.getValue(evalContext, expectedType);
    }
    
    @Override
    public boolean validateExpression(String expression) {
        try {
            parser.parseExpression(expression, parserContext);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private StandardEvaluationContext createEvaluationContext(BaseExecutionContext context, Map<String, Object> variables) {
        StandardEvaluationContext evalContext = new StandardEvaluationContext();
        
        // 添加上下文變數
        evalContext.setVariable("executionId", context.getExecutionId());
        evalContext.setVariable("flowId", context.getFlowId());
        evalContext.setVariable("customerId", context.getCustomerId());
        evalContext.setVariable("customerData", context.getCustomerData());
        evalContext.setVariable("variables", context.getVariables());
        
        // 添加額外變數
        if (variables != null) {
            variables.forEach(evalContext::setVariable);
        }
        
        return evalContext;
    }
}