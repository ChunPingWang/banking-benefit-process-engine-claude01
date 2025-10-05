package com.example.banking.benefit.application.dto.monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "流程執行詳細資訊")
public class ExecutionDetails {
    
    @Schema(description = "執行ID", example = "e123-456-789")
    private String executionId;
    
    @Schema(description = "流程ID", example = "f123-456-789")
    private String flowId;
    
    @Schema(description = "流程版本", example = "1.0.0")
    private String version;
    
    @Schema(description = "執行狀態", example = "COMPLETED")
    private String status;
    
    @Schema(description = "開始時間", example = "2025-10-04T10:00:00")
    private String startTime;
    
    @Schema(description = "結束時間", example = "2025-10-04T10:00:05")
    private String endTime;
    
    @Schema(description = "執行時間（毫秒）", example = "5000")
    private Long executionTime;
    
    @Schema(description = "執行路徑", example = "[\"Node1\", \"Node2\", \"Node3\"]")
    private List<String> executionPath;
    
    @Schema(description = "節點執行時間", example = "{\"Node1\": 1000, \"Node2\": 2000, \"Node3\": 2000}")
    private Map<String, Long> nodeExecutionTimes;
    
    @Schema(description = "錯誤訊息", example = "Decision evaluation failed at Node2")
    private String errorMessage;
}