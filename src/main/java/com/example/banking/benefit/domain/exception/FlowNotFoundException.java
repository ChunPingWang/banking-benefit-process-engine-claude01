package com.example.banking.benefit.domain.exception;

/**
 * 找不到流程時拋出的例外
 */
public class FlowNotFoundException extends BaseException {
    
    private static final String ERROR_CODE = "E404";
    
    public FlowNotFoundException(String message) {
        super(ERROR_CODE, message);
    }
    
    public FlowNotFoundException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}