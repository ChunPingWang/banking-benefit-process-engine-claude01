package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.controller.FlowMonitorController;
import com.example.banking.benefit.domain.model.statistics.FlowStatistics;
import com.example.banking.benefit.domain.model.statistics.ExecutionDetails;

import java.time.Duration;
import static org.mockito.ArgumentMatchers.eq;
import com.example.banking.benefit.domain.service.FlowExecutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlowMonitorControllerTest {

    @Mock
    private FlowExecutionService flowExecutionService;

    @InjectMocks
    private FlowMonitorController flowMonitorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFlowStatistics_ShouldReturnStatistics() {
        // Arrange
        String flowId = "test-flow";
        FlowStatistics mockStatistics = new FlowStatistics(
            100L, // totalExecutions
            90L,  // successfulExecutions
            10L,  // failedExecutions
            Duration.ofMillis(150), // averageExecutionTime
            Duration.ofMillis(200), // maxExecutionTime
            Duration.ofMillis(100)  // minExecutionTime
        );
        
        when(flowExecutionService.getFlowStatistics(eq(flowId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockStatistics);

        // Act
        ResponseEntity<FlowStatistics> response = flowMonitorController.getFlowStatistics(flowId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getTotalExecutions());
        assertEquals(90L, response.getBody().getSuccessfulExecutions());
        assertEquals(10L, response.getBody().getFailedExecutions());
        assertEquals(100L, response.getBody().getTotalExecutions());
        verify(flowExecutionService).getFlowStatistics(eq(flowId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getFlowExecutions_ShouldReturnExecutions() {
        // Arrange
        String flowId = "test-flow";
        int page = 0;
        int size = 10;
        List<ExecutionDetails> executions = new ArrayList<>();
        ExecutionDetails details = ExecutionDetails.builder()
            .executionId("exec-1")
            .flowId(flowId)
            .version("1.0")
            .customerId("customer-1")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusSeconds(2))
            .executionTime(Duration.ofSeconds(2))
            .status("SUCCESS")
            .build();
        executions.add(details);
        
        when(flowExecutionService.getFlowExecutions(eq(flowId), any(), any(), any(), eq(size), eq(page)))
                .thenReturn(executions);

        // Act
        ResponseEntity<List<ExecutionDetails>> response = 
                flowMonitorController.getFlowExecutions(flowId, page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        verify(flowExecutionService).getFlowExecutions(eq(flowId), any(), any(), any(), eq(size), eq(page));
    }

    @Test
    void getExecutionDetails_ShouldReturnDetails() {
        // Arrange
        String executionId = "exec-1";
        ExecutionDetails mockDetails = ExecutionDetails.builder()
            .executionId(executionId)
            .flowId("test-flow")
            .version("1.0")
            .customerId("customer-1")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusSeconds(2))
            .executionTime(Duration.ofSeconds(2))
            .status("SUCCESS")
            .build();
        
        when(flowExecutionService.getExecutionDetails(executionId)).thenReturn(mockDetails);

        // Act
        ResponseEntity<ExecutionDetails> response = flowMonitorController.getExecutionDetails(executionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(executionId, response.getBody().getExecutionId());
        verify(flowExecutionService).getExecutionDetails(executionId);
    }

    @Test
    void getAllFlowStatistics_ShouldReturnAllStatistics() {
        // Arrange
        List<FlowStatistics> mockStatistics = new ArrayList<>();
        mockStatistics.add(new FlowStatistics(
            100L, // totalExecutions
            90L,  // successfulExecutions
            10L,  // failedExecutions
            Duration.ofMillis(150), // averageExecutionTime
            Duration.ofMillis(200), // maxExecutionTime
            Duration.ofMillis(100)  // minExecutionTime
        ));
        
        mockStatistics.add(new FlowStatistics(
            200L, // totalExecutions
            180L, // successfulExecutions
            20L,  // failedExecutions
            Duration.ofMillis(160), // averageExecutionTime
            Duration.ofMillis(250), // maxExecutionTime
            Duration.ofMillis(120)  // minExecutionTime
        ));
        
        when(flowExecutionService.getAllFlowStatistics(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockStatistics);

        // Act
        ResponseEntity<List<FlowStatistics>> response = flowMonitorController.getAllFlowStatistics();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(flowExecutionService).getAllFlowStatistics(any(LocalDateTime.class), any(LocalDateTime.class));
    }
}