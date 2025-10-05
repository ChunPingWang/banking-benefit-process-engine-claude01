package com.example.banking.benefit.application.dto.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "流程執行結果")
public class ExecutionResult {
    
    @Schema(description = "執行ID", example = "e123-456-789")
    private String executionId;
    
    @Schema(description = "流程ID", example = "f123-456-789")
    private String flowId;
    
    @Schema(description = "流程版本", example = "1.0.0")
    private String version;
    
    @Schema(description = "執行狀態", example = "COMPLETED")
    private String status;
    
    @Schema(description = "執行結果數據")
    private Map<String, Object> resultData;
    
    @Schema(description = "開始時間", example = "2025-10-04T10:00:00")
    private String startTime;
    
    @Schema(description = "結束時間", example = "2025-10-04T10:00:05")
    private String endTime;
    
    @Schema(description = "執行時間（毫秒）", example = "5000")
    private Long executionTime;
}