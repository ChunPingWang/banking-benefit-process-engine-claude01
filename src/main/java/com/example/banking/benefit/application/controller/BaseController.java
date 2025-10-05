package com.example.banking.benefit.application.controller;

import com.example.banking.benefit.application.dto.common.ApiResponse;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    
    protected <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    protected ResponseEntity<ApiResponse<Void>> success() {
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
    protected <T> ResponseEntity<ApiResponse<T>> error(String code, String message) {
        return ResponseEntity.badRequest().body(ApiResponse.error(code, message));
    }
}