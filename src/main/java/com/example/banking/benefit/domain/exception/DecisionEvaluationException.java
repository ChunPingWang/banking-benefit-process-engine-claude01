package com.example.banking.benefit.domain.exception;

/**
 * 決策節點評估異常
 */
public class DecisionEvaluationException extends BaseException {

    public static final String ERROR_CODE_DECISION_NOT_FOUND = "DECISION_001";
    public static final String ERROR_CODE_INVALID_EXPRESSION = "DECISION_002";
    public static final String ERROR_CODE_EVALUATION_ERROR = "DECISION_003";
    public static final String ERROR_CODE_CLASS_NOT_FOUND = "DECISION_004";

    public DecisionEvaluationException(String errorCode, String message) {
        super(errorCode, message);
    }

    public DecisionEvaluationException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public static DecisionEvaluationException decisionNotFound(String decisionId) {
        return new DecisionEvaluationException(
            ERROR_CODE_DECISION_NOT_FOUND,
            String.format("Decision not found with id: %s", decisionId)
        );
    }

    public static DecisionEvaluationException invalidExpression(String expression, String reason) {
        return new DecisionEvaluationException(
            ERROR_CODE_INVALID_EXPRESSION,
            String.format("Invalid expression: %s, reason: %s", expression, reason)
        );
    }

    public static DecisionEvaluationException evaluationError(String message, Throwable cause) {
        return new DecisionEvaluationException(
            ERROR_CODE_EVALUATION_ERROR,
            String.format("Decision evaluation error: %s", message),
            cause
        );
    }

    public static DecisionEvaluationException classNotFound(String className) {
        return new DecisionEvaluationException(
            ERROR_CODE_CLASS_NOT_FOUND,
            String.format("Decision implementation class not found: %s", className)
        );
    }
}