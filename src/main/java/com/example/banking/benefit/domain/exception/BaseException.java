package com.example.banking.benefit.domain.exception;

/**
 * 領域基礎例外類別
 */
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final String errorMessage;

    protected BaseException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    protected BaseException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}