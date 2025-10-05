package com.example.banking.benefit.application.dto.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "流程建立請求")
public class CreateFlowRequest {
    
    @Schema(description = "流程名稱", example = "信用卡回饋金計算流程")
    private String name;
    
    @Schema(description = "流程描述", example = "根據客戶的消費行為計算信用卡回饋金")
    private String description;
    
    @Schema(description = "流程版本", example = "1.0.0")
    private String version;
}