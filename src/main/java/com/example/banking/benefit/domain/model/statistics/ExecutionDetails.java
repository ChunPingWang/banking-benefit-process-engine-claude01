package com.example.banking.benefit.domain.model.statistics;

import com.example.banking.benefit.domain.model.result.ExecutionResult;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class ExecutionDetails {
    private String executionId;
    private String flowId;
    private String version;
    private String customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration executionTime;
    private String status;
    private String errorMessage;
    private ExecutionResult result;
}