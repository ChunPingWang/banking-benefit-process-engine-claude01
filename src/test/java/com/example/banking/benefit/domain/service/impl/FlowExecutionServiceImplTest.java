package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.statistics.ExecutionDetails;
import com.example.banking.benefit.domain.model.statistics.FlowStatistics;
import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.log.ExecutionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.stream.Collectors;
import com.example.banking.benefit.domain.repository.ExecutionLogRepository;
import com.example.banking.benefit.domain.repository.FlowRepository;
import com.example.banking.benefit.domain.service.FlowExecutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FlowExecutionServiceImplTest {

    private ExecutionLog createExecutionLog(String executionId, String flowId, String status) {
        ExecutionLog log = new ExecutionLog();
        log.setExecutionId(executionId);
        log.setFlowId(FlowId.of(flowId));
        log.setStartTime(LocalDateTime.now().minusMinutes(30));
        log.setEndTime(LocalDateTime.now().minusMinutes(29));
        log.setExecutionDuration(Duration.ofMinutes(1));
        log.setExecutionResult(status);
        return log;
    }

    private ExecutionDetails convertToExecutionLog(ExecutionLog log) {
        return ExecutionDetails.builder()
            .executionId(log.getExecutionId())
            .flowId(log.getFlowId().getValue())
            .startTime(log.getStartTime())
            .endTime(log.getEndTime())
            .executionTime(log.getExecutionDuration())
            .status(log.getExecutionResult())
            .build();
    }

    @Mock
    private FlowRepository flowRepository;

    @Mock
    private ExecutionLogRepository executionLogRepository;

    @InjectMocks
    private FlowExecutionServiceImpl flowExecutionService;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startTime = LocalDateTime.now().minusDays(7);
        endTime = LocalDateTime.now();
    }

    @Test
    void getFlowStatistics_ShouldReturnStatistics() {
        // Arrange
        String flowId = "test-flow";
        List<ExecutionDetails> executions = Arrays.asList(
            ExecutionDetails.builder()
                .executionId("exec-1")
                .flowId(flowId)
                .startTime(LocalDateTime.now().minusMinutes(30))
                .endTime(LocalDateTime.now().minusMinutes(29))
                .executionTime(Duration.ofMinutes(1))
                .status("SUCCESS")
                .build(),
            ExecutionDetails.builder()
                .executionId("exec-2")
                .flowId(flowId)
                .startTime(LocalDateTime.now().minusMinutes(20))
                .endTime(LocalDateTime.now().minusMinutes(19))
                .executionTime(Duration.ofMinutes(1))
                .status("FAILED")
                .build()
        );

        when(executionLogRepository.findByFlowIdAndExecutionTimeBetween(eq(flowId), any(), any()))
            .thenReturn(executions);

        // Act
        FlowStatistics statistics = flowExecutionService.getFlowStatistics(flowId, startTime, endTime);

        // Assert
        assertNotNull(statistics);
        assertEquals(2, statistics.getTotalExecutions());
        assertEquals(1, statistics.getSuccessfulExecutions());
        assertEquals(1, statistics.getFailedExecutions());
        verify(executionLogRepository).findByFlowIdAndExecutionTimeBetween(eq(flowId), eq(startTime), eq(endTime));
    }

    @Test
    void getFlowExecutions_ShouldReturnExecutions() {
        // Arrange
        String flowId = "test-flow";
        String status = "SUCCESS";
        int pageSize = 10;
        int pageNumber = 0;

        List<ExecutionDetails> executions = Arrays.asList(
            ExecutionDetails.builder()
                .executionId("exec-1")
                .flowId(flowId)
                .startTime(LocalDateTime.now().minusMinutes(30))
                .endTime(LocalDateTime.now().minusMinutes(29))
                .executionTime(Duration.ofMinutes(1))
                .status(status)
                .build()
        );

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ExecutionLog> executionPage = new PageImpl<>(executions.stream()
            .map(this::convertToExecutionLog)
            .collect(java.util.stream.Collectors.toList()));

        when(executionLogRepository.findByFlowIdAndExecutionResultAndExecutionTimeBetween(
                eq(flowId), eq(status), eq(startTime), eq(endTime), any(Pageable.class)))
            .thenReturn(executionPage);

        // Act
        List<ExecutionDetails> result = flowExecutionService.getFlowExecutions(
            flowId, startTime, endTime, status, pageSize, pageNumber);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
        verify(executionLogRepository).findByFlowIdAndExecutionResultAndExecutionTimeBetween(
            eq(flowId), eq(status), eq(startTime), eq(endTime), eq(pageRequest));
    }

    @Test
    void getExecutionDetails_ShouldReturnDetails() {
        // Arrange
        String executionId = "exec-1";
        ExecutionDetails expectedDetails = ExecutionDetails.builder()
            .executionId(executionId)
            .flowId("test-flow")
            .startTime(LocalDateTime.now().minusMinutes(30))
            .endTime(LocalDateTime.now().minusMinutes(29))
            .executionTime(Duration.ofMinutes(1))
            .status("SUCCESS")
            .build();

        when(executionLogRepository.findById(executionId))
            .thenReturn(java.util.Optional.of(expectedDetails));

        // Act
        ExecutionDetails details = flowExecutionService.getExecutionDetails(executionId);

        // Assert
        assertNotNull(details);
        assertEquals(executionId, details.getExecutionId());
        assertEquals("SUCCESS", details.getStatus());
        verify(executionLogRepository).findById(executionId);
    }

    @Test
    void getAllFlowStatistics_ShouldReturnAllStatistics() {
        // Arrange
        List<String> flowIds = Arrays.asList("flow-1", "flow-2");
        List<Flow> flows = Arrays.asList(
            Flow.builder().flowId(FlowId.of("flow-1")).build(),
            Flow.builder().flowId(FlowId.of("flow-2")).build()
        );
        when(flowRepository.findByStatus("ACTIVE")).thenReturn(flows);

        List<ExecutionLog> flow1Executions = Arrays.asList(
            createExecutionLog("exec-1", "flow-1", "SUCCESS"),
            createExecutionLog("exec-2", "flow-1", "SUCCESS")
        );

        List<ExecutionLog> flow2Executions = Arrays.asList(
            createExecutionLog("exec-3", "flow-2", "FAILED"),
            createExecutionLog("exec-4", "flow-2", "SUCCESS")
        );

        when(executionLogRepository.findByFlowIdAndExecutionTimeBetween(eq("flow-1"), any(), any()))
            .thenReturn(flow1Executions);
        when(executionLogRepository.findByFlowIdAndExecutionTimeBetween(eq("flow-2"), any(), any()))
            .thenReturn(flow2Executions);

        // Act
        List<FlowStatistics> statistics = flowExecutionService.getAllFlowStatistics(startTime, endTime);

        // Assert
        assertNotNull(statistics);
        assertEquals(2, statistics.size());
        verify(flowRepository).findByStatus("ACTIVE");
        verify(executionLogRepository, times(2))
            .findByFlowIdAndExecutionTimeBetween(anyString(), eq(startTime), eq(endTime));
    }
}