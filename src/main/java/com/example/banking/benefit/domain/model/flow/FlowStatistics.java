package com.example.banking.benefit.domain.model.flow;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FlowStatistics {
    private FlowId flowId;
    private Long totalExecutions;
    private Long successfulExecutions;
    private Long failedExecutions;
    private Double averageExecutionTime;
    private LocalDateTime lastExecutionTime;
}