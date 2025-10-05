package com.example.banking.benefit.domain.model.log;

import com.example.banking.benefit.domain.model.flow.FlowId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ExecutionDetails {
    private String executionId;
    private FlowId flowId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Map<String, Object> inputData;
    private Map<String, Object> outputData;
    private String errorMessage;
}