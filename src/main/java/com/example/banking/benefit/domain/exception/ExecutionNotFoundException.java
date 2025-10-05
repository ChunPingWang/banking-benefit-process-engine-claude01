package com.example.banking.benefit.domain.exception;

/**
 * 找不到執行記錄時拋出的例外
 */
public class ExecutionNotFoundException extends BaseException {
    
    private static final String ERROR_CODE = "E404";
    
    public ExecutionNotFoundException(String message) {
        super(ERROR_CODE, message);
    }
    
    public ExecutionNotFoundException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}