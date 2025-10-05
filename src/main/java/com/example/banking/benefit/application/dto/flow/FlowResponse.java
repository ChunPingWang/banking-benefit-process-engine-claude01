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
@Schema(description = "流程資訊響應")
public class FlowResponse {
    
    @Schema(description = "流程ID", example = "f123-456-789")
    private String id;
    
    @Schema(description = "流程名稱", example = "信用卡回饋金計算流程")
    private String name;
    
    @Schema(description = "流程描述", example = "根據客戶的消費行為計算信用卡回饋金")
    private String description;
    
    @Schema(description = "流程版本", example = "1.0.0")
    private String version;
    
    @Schema(description = "建立時間", example = "2025-10-04T10:00:00")
    private String createdAt;
    
    @Schema(description = "更新時間", example = "2025-10-04T10:00:00")
    private String updatedAt;
}