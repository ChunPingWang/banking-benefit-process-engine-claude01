package com.example.banking.benefit.application.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API 通用響應物件")
public class ApiResponse<T> {
    
    @Schema(description = "響應代碼", example = "200")
    private String code;
    
    @Schema(description = "響應訊息", example = "操作成功")
    private String message;
    
    @Schema(description = "響應資料")
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code("200")
                .message("操作成功")
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}