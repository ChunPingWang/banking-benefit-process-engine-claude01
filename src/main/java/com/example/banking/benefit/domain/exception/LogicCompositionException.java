package com.example.banking.benefit.domain.exception;

/**
 * 邏輯組合異常
 */
public class LogicCompositionException extends RuntimeException {
    
    public LogicCompositionException(String message) {
        super(message);
    }

    public LogicCompositionException(String message, Throwable cause) {
        super(message, cause);
    }
}