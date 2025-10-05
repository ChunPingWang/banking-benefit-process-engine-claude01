package com.example.banking.benefit.application.converter;

import com.example.banking.benefit.application.dto.monitor.ExecutionDetails;
import com.example.banking.benefit.application.dto.monitor.FlowStatistics;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MonitoringConverter {
    
    public FlowStatistics toDto(com.example.banking.benefit.domain.model.statistics.FlowStatistics domain) {
        if (domain == null) {
            return null;
        }
        return FlowStatistics.builder()
            .totalExecutions(domain.getTotalExecutions())
            .successfulExecutions(domain.getSuccessfulExecutions())
            .failedExecutions(domain.getFailedExecutions())
            .averageExecutionTime(domain.getAverageExecutionTime().toMillis())
            .maxExecutionTime(domain.getMaxExecutionTime().toMillis())
            .minExecutionTime(domain.getMinExecutionTime().toMillis())
            .build();
    }
    
    public List<FlowStatistics> toDto(List<com.example.banking.benefit.domain.model.statistics.FlowStatistics> domains) {
        if (domains == null) {
            return null;
        }
        return domains.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public ExecutionDetails toDto(com.example.banking.benefit.domain.model.statistics.ExecutionDetails domain) {
        if (domain == null) {
            return null;
        }
        return ExecutionDetails.builder()
            .executionId(domain.getExecutionId())
            .flowId(domain.getFlowId())
            .startTime(domain.getStartTime().toString())
            .executionTime(domain.getExecutionTime().toMillis())
            .status(domain.getStatus())
            .errorMessage(domain.getErrorMessage())
            .build();
    }
    
    public List<ExecutionDetails> toDtoList(List<com.example.banking.benefit.domain.model.statistics.ExecutionDetails> domains) {
        if (domains == null) {
            return null;
        }
        return domains.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}