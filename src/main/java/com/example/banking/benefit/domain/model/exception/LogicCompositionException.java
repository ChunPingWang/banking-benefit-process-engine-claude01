package com.example.banking.benefit.domain.model.exception;

/**
 * 邏輯組合異常
 * 用於處理邏輯組合過程中出現的錯誤
 */
public class LogicCompositionException extends RuntimeException {
    
    public LogicCompositionException(String message) {
        super(message);
    }
    
    public LogicCompositionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LogicCompositionException(Throwable cause) {
        super(cause);
    }
}