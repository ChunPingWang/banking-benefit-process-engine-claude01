package com.example.banking.benefit.domain.exception;

/**
 * 流程狀態處理相關例外
 */
public class ProcessStateException extends BaseException {
    
    public static final String ERROR_CODE_STATE_NOT_FOUND = "STATE_001";
    public static final String ERROR_CODE_INVALID_STATE = "STATE_002";
    public static final String ERROR_CODE_TRANSITION_ERROR = "STATE_003";
    public static final String ERROR_CODE_STATE_EXECUTION_ERROR = "STATE_004";

    public ProcessStateException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public ProcessStateException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public static ProcessStateException stateNotFound(String stateId) {
        return new ProcessStateException(
            ERROR_CODE_STATE_NOT_FOUND,
            String.format("Process state not found with id: %s", stateId)
        );
    }

    public static ProcessStateException invalidState(String reason) {
        return new ProcessStateException(
            ERROR_CODE_INVALID_STATE,
            String.format("Invalid process state: %s", reason)
        );
    }

    public static ProcessStateException transitionError(String fromState, String toState, String reason) {
        return new ProcessStateException(
            ERROR_CODE_TRANSITION_ERROR,
            String.format("State transition error from %s to %s: %s", fromState, toState, reason)
        );
    }

    public static ProcessStateException stateExecutionError(String state, String message, Throwable cause) {
        return new ProcessStateException(
            ERROR_CODE_STATE_EXECUTION_ERROR,
            String.format("State execution error in %s: %s", state, message),
            cause
        );
    }
}