package com.example.banking.benefit.application.dto.monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "流程執行統計資訊")
public class FlowStatistics {
    
    @Schema(description = "流程ID", example = "f123-456-789")
    private String flowId;
    
    @Schema(description = "流程名稱", example = "信用卡回饋金計算流程")
    private String flowName;
    
    @Schema(description = "總執行次數", example = "1000")
    private Long totalExecutions;
    
    @Schema(description = "成功執行次數", example = "950")
    private Long successfulExecutions;
    
    @Schema(description = "失敗執行次數", example = "50")
    private Long failedExecutions;
    
    @Schema(description = "平均執行時間（毫秒）", example = "150")
    private Long averageExecutionTime;
    
    @Schema(description = "最長執行時間（毫秒）", example = "500")
    private Long maxExecutionTime;
    
    @Schema(description = "最短執行時間（毫秒）", example = "50")
    private Long minExecutionTime;
}