package com.example.banking.benefit.application.exception;

import com.example.banking.benefit.application.dto.common.ApiResponse;
import com.example.banking.benefit.domain.exception.BaseException;
import com.example.banking.benefit.domain.exception.DecisionEvaluationException;
import com.example.banking.benefit.domain.exception.FlowExecutionException;
import com.example.banking.benefit.domain.exception.ProcessExecutionException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.error("Validation error: {}", errors);
        return ApiResponse.error("400", "輸入驗證錯誤: " + errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleConstraintViolation(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        log.error("Constraint violation: {}", errors);
        return ApiResponse.error("400", "參數驗證錯誤: " + errors);
    }

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleBaseException(BaseException ex) {
        log.error("Base error: {}", ex.getMessage(), ex);
        return ApiResponse.error("500", ex.getMessage());
    }

    @ExceptionHandler(FlowExecutionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleFlowExecutionException(FlowExecutionException ex) {
        return ApiResponse.error("400", ex.getMessage());
    }

    @ExceptionHandler(DecisionEvaluationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleDecisionEvaluationException(DecisionEvaluationException ex) {
        return ApiResponse.error("400", ex.getMessage());
    }

    @ExceptionHandler(ProcessExecutionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleProcessExecutionException(ProcessExecutionException ex) {
        return ApiResponse.error("400", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception ex) {
        return ApiResponse.error("500", "系統內部錯誤");
    }
}