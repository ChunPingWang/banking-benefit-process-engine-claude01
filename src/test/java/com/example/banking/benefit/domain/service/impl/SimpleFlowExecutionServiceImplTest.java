package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.log.ExecutionLog;
import com.example.banking.benefit.domain.model.node.NodeType;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.repository.ExecutionLogRepository;
import com.example.banking.benefit.domain.repository.FlowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleFlowExecutionServiceImplTest {

    @Mock
    private FlowRepository flowRepository;

    @Mock
    private ExecutionLogRepository executionLogRepository;

    @InjectMocks
    private FlowExecutionServiceImpl flowExecutionService;

    private Flow testFlow;
    private BaseExecutionContext testContext;

    @BeforeEach
    void setUp() {
        // 準備測試用的流程
        testFlow = mock(Flow.class);
        when(testFlow.getFlowId()).thenReturn(FlowId.of("TEST_FLOW"));
        
        // 準備測試用的執行上下文
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("age", CustomerAttribute.forInteger(25));
        CustomerData customerData = CustomerData.create("CUST_001", attributes);
        testContext = ExecutionContext.create("TEST_FLOW", "CUST_001", customerData);
    }

    @Test
    void execute_ShouldReturnSuccessResult_WhenFlowExists() {
        // Arrange
        when(flowRepository.existsById(any(FlowId.class))).thenReturn(true);
        when(testFlow.getStartNode()).thenReturn(java.util.Optional.empty()); // 簡化測試，沒有節點

        // Act & Assert
        assertDoesNotThrow(() -> {
            ExecutionResult result = flowExecutionService.execute(testFlow, testContext);
            assertNotNull(result);
        });
    }

    @Test
    void execute_ShouldSaveExecutionLog() {
        // Arrange
        when(flowRepository.existsById(any(FlowId.class))).thenReturn(true);
        when(testFlow.getStartNode()).thenReturn(java.util.Optional.empty());

        // Act
        assertDoesNotThrow(() -> flowExecutionService.execute(testFlow, testContext));

        // Assert
        verify(executionLogRepository, atLeastOnce()).save(any(ExecutionLog.class));
    }

    @Test
    void execute_ShouldThrowException_WhenFlowNotFound() {
        // Arrange
        when(flowRepository.existsById(any(FlowId.class))).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            flowExecutionService.execute(testFlow, testContext);
        });
    }

    private ExecutionLog createTestExecutionLog() {
        return ExecutionLog.create(
            "TEST_FLOW",
            "CUST_001", 
            "NODE_001",
            NodeType.DECISION,
            com.example.banking.benefit.domain.model.log.ExecutionResult.PASS
        );
    }
}