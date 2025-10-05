package com.example.banking.benefit.application.controller;

import com.example.banking.benefit.application.dto.common.ApiResponse;
import com.example.banking.benefit.application.dto.flow.ExecuteFlowRequest;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.service.FlowExecutionServiceExtended;
import com.example.banking.benefit.domain.service.FlowManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlowExecutionController.class)
class FlowExecutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FlowExecutionServiceExtended flowExecutionService;

    @MockBean
    private FlowManagementService flowManagementService;

    private ExecuteFlowRequest request;
    private Flow flow;
    private ExecutionResult executionResult;

    @BeforeEach
    void setUp() {
        // 準備測試資料
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("age", CustomerAttribute.forInteger(25));
        CustomerData customerData = CustomerData.create("C001", attributes);

        request = ExecuteFlowRequest.builder()
                .flowId("F001")
                .version("1.0.0")
                .customerData(customerData)
                .build();

        flow = mock(Flow.class);
        executionResult = mock(ExecutionResult.class);
    }

    @Test
    void executeFlow_Success() throws Exception {
        when(flowManagementService.getFlow(any(FlowId.class), eq("1.0.0")))
                .thenReturn(Optional.of(flow));
        when(flowExecutionService.execute(any(Flow.class), any()))
                .thenReturn(executionResult);

        mockMvc.perform(post("/api/v1/flow-executions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("200"));

        verify(flowExecutionService).execute(any(Flow.class), any());
    }

    @Test
    void executeFlow_ValidationError() throws Exception {
        request.setFlowId("");  // 設置無效的流程ID

        mockMvc.perform(post("/api/v1/flow-executions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value(containsString("流程ID不能為空")));

        verify(flowExecutionService, never()).execute(any(), any());
    }

    @Test
    void executeFlow_FlowNotFound() throws Exception {
        when(flowManagementService.getFlow(any(FlowId.class), any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/flow-executions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("流程不存在"));

        verify(flowExecutionService, never()).execute(any(), any());
    }

    @Test
    void getExecutionResult_Success() throws Exception {
        String executionId = UUID.randomUUID().toString();
        when(flowExecutionService.getExecutionResult(executionId))
                .thenReturn(executionResult);

        mockMvc.perform(get("/api/v1/flow-executions/{executionId}", executionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("200"));

        verify(flowExecutionService).getExecutionResult(executionId);
    }

    @Test
    void getExecutionResult_NotFound() throws Exception {
        String executionId = UUID.randomUUID().toString();
        when(flowExecutionService.getExecutionResult(executionId))
                .thenThrow(new RuntimeException("執行結果不存在"));

        mockMvc.perform(get("/api/v1/flow-executions/{executionId}", executionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("404"));

        verify(flowExecutionService).getExecutionResult(executionId);
    }

    @Test
    void cancelExecution_Success() throws Exception {
        String executionId = UUID.randomUUID().toString();
        doNothing().when(flowExecutionService).cancelExecution(executionId);

        mockMvc.perform(delete("/api/v1/flow-executions/{executionId}", executionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("200"));

        verify(flowExecutionService).cancelExecution(executionId);
    }

    @Test
    void cancelExecution_Error() throws Exception {
        String executionId = UUID.randomUUID().toString();
        doThrow(new RuntimeException("無法取消執行"))
                .when(flowExecutionService).cancelExecution(executionId);

        mockMvc.perform(delete("/api/v1/flow-executions/{executionId}", executionId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value(containsString("無法取消執行")));

        verify(flowExecutionService).cancelExecution(executionId);
    }
}