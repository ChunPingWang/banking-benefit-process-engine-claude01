package com.example.banking.benefit.domain.service.expression;

/**
 * 表達式執行異常
 */
public class ExpressionEvaluationException extends RuntimeException {
    
    public ExpressionEvaluationException(String message) {
        super(message);
    }
    
    public ExpressionEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}