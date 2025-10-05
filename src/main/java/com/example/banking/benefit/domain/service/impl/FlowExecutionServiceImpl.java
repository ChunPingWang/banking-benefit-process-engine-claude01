package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.statistics.ExecutionDetails;
import com.example.banking.benefit.domain.model.statistics.FlowStatistics;
import com.example.banking.benefit.domain.model.log.ExecutionLog;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.service.FlowExecutionService;
import com.example.banking.benefit.domain.repository.ExecutionLogRepository;
import com.example.banking.benefit.domain.repository.FlowRepository;
import com.example.banking.benefit.domain.exception.ExecutionNotFoundException;
import com.example.banking.benefit.domain.exception.FlowNotFoundException;
import jakarta.inject.Inject;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@Service
public class FlowExecutionServiceImpl implements FlowExecutionService {

    private final FlowRepository flowRepository;
    private final ExecutionLogRepository executionLogRepository;

    @Inject
    public FlowExecutionServiceImpl(FlowRepository flowRepository, ExecutionLogRepository executionLogRepository) {
        this.flowRepository = flowRepository;
        this.executionLogRepository = executionLogRepository;
    }
    
    @Override
    public ExecutionResult execute(Flow flow, BaseExecutionContext context) {
        // 原有的執行邏輯
        return null;  // TODO: 實作執行邏輯
    }
    
    @Override
    public void pause(Flow flow, BaseExecutionContext context) {
        // 原有的暫停邏輯
    }
    
    @Override
    public ExecutionResult resume(Flow flow, BaseExecutionContext context) {
        // 原有的繼續執行邏輯
        return null;  // TODO: 實作繼續執行邏輯
    }
    
    @Override
    public void terminate(Flow flow, BaseExecutionContext context) {
        // 原有的中止邏輯
    }
    
    @Override
    public String getExecutionStatus(Flow flow, BaseExecutionContext context) {
        // 原有的取得狀態邏輯
        return null;  // TODO: 實作取得狀態邏輯
    }

    @Override
    public FlowStatistics getFlowStatistics(String flowId, LocalDateTime startTime, LocalDateTime endTime) {
        FlowId id = FlowId.of(flowId);
        // 檢查流程是否存在
        if (!flowRepository.existsById(id)) {
            throw new FlowNotFoundException("找不到流程：" + flowId);
        }
        
        // 從 ExecutionLogRepository 取得執行記錄
        List<ExecutionLog> logs = executionLogRepository.findByFlowIdAndExecutionTimeBetween(flowId, startTime, endTime);
        
        // 計算成功和失敗的執行數量
        long totalExecutions = logs.size();
        long successfulExecutions = logs.stream()
            .filter(log -> "SUCCESS".equals(log.getExecutionResult().toString()))
            .count();
        long failedExecutions = totalExecutions - successfulExecutions;
        
        // 計算平均、最大和最小執行時間
        Duration averageExecutionTime = calculateAverageExecutionTime(logs);
        Duration maxExecutionTime = calculateMaxExecutionTime(logs);
        Duration minExecutionTime = calculateMinExecutionTime(logs);
        
        // 建立並返回統計資訊
        return new FlowStatistics(
            totalExecutions,
            successfulExecutions,
            failedExecutions,
            averageExecutionTime,
            maxExecutionTime,
            minExecutionTime
        );
    }

    @Override
    public List<ExecutionDetails> getFlowExecutions(String flowId, LocalDateTime startTime, LocalDateTime endTime, 
                                                  String status, int pageSize, int pageNumber) {
        FlowId id = FlowId.of(flowId);
        // 檢查流程是否存在
        if (!flowRepository.existsById(id)) {
            throw new FlowNotFoundException("找不到流程：" + flowId);
        }
        
        // 轉換為分頁參數
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        
        // 查詢執行記錄
        var executionLogs = executionLogRepository.findByFlowIdAndExecutionResultAndExecutionTimeBetween(
            flowId, status, startTime, endTime, pageRequest);
            
        // 將執行記錄轉換為 ExecutionDetails
        return executionLogs.getContent().stream()
            .map(this::convertToExecutionDetails)
            .collect(Collectors.toList());
    }

    @Override
    public ExecutionDetails getExecutionDetails(String executionId) {
        ExecutionLog log = executionLogRepository.findById(executionId)
            .orElseThrow(() -> new ExecutionNotFoundException("執行記錄不存在: " + executionId));
            
        return convertToExecutionDetails(log);
    }

    @Override
    public List<FlowStatistics> getAllFlowStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        List<Flow> flows = flowRepository.findAll();
        
        return flows.stream()
            .map(flow -> getFlowStatistics(flow.getFlowId().getValue(), startTime, endTime))
            .collect(Collectors.toList());
    }

    private ExecutionDetails convertToExecutionDetails(ExecutionLog log) {
        return ExecutionDetails.builder()
            .executionId(log.getLogId())
            .flowId(log.getFlowId())
            .customerId(log.getCustomerId())
            .startTime(log.getExecutionTime())
            .executionTime(Duration.ofMillis(log.getExecutionDurationMs()))
            .status(log.getExecutionResult().toString())
            .errorMessage(log.getErrorMessage())
            .build();
    }

    private Duration calculateAverageExecutionTime(List<ExecutionLog> logs) {
        if (logs.isEmpty()) {
            return Duration.ZERO;
        }
        
        double avgMillis = logs.stream()
            .mapToInt(ExecutionLog::getExecutionDurationMs)
            .average()
            .orElse(0.0);
            
        return Duration.ofMillis(Math.round(avgMillis));
    }

    private Duration calculateMaxExecutionTime(List<ExecutionLog> logs) {
        if (logs.isEmpty()) {
            return Duration.ZERO;
        }
        
        OptionalInt maxMillis = logs.stream()
            .mapToInt(ExecutionLog::getExecutionDurationMs)
            .max();
            
        return maxMillis.isPresent() ? Duration.ofMillis(maxMillis.getAsInt()) : Duration.ZERO;
    }

    private Duration calculateMinExecutionTime(List<ExecutionLog> logs) {
        if (logs.isEmpty()) {
            return Duration.ZERO;
        }
        
        OptionalInt minMillis = logs.stream()
            .mapToInt(ExecutionLog::getExecutionDurationMs)
            .min();
            
        return minMillis.isPresent() ? Duration.ofMillis(minMillis.getAsInt()) : Duration.ZERO;
    }
}
}
}
}