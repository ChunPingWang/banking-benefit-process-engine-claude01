package com.example.banking.benefit.application.controller;

import com.example.banking.benefit.application.dto.common.ApiResponse;
import com.example.banking.benefit.application.dto.flow.ExecuteFlowRequest;
import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.DefaultExecutionContext;
import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.service.FlowExecutionServiceExtended;
import com.example.banking.benefit.domain.service.FlowManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flow-executions")
@RequiredArgsConstructor
@Tag(name = "Flow Execution", description = "流程執行相關 API")
public class FlowExecutionController {

    private final FlowExecutionServiceExtended flowExecutionService;
    private final FlowManagementService flowManagementService;

    @PostMapping
    @Operation(summary = "執行流程", description = "執行指定的流程並返回結果")
    public ResponseEntity<ApiResponse<ExecutionResult>> executeFlow(
            @Valid @RequestBody ExecuteFlowRequest request) {
        try {
            FlowId flowId = FlowId.of(request.getFlowId());
            Flow flow = flowManagementService.getFlow(flowId, request.getVersion())
                    .orElseThrow(() -> new RuntimeException("流程不存在"));

            BaseExecutionContext context = DefaultExecutionContext.builder()
                    .flowId(flowId.getValue())
                    .executionId(UUID.randomUUID().toString())
                    .customerId(request.getCustomerData().getId())
                    .customerData(request.getCustomerData())
                    .build();

            ExecutionResult result = flowExecutionService.execute(flow, context);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("500", "流程執行失敗：" + e.getMessage()));
        }
    }

    @GetMapping("/{executionId}")
    @Operation(summary = "取得執行結果", description = "根據執行ID取得流程執行結果")
    public ResponseEntity<ApiResponse<ExecutionResult>> getExecutionResult(
            @Parameter(description = "執行ID", example = "e123-456-789")
            @PathVariable String executionId) {
        try {
            ExecutionResult result = flowExecutionService.getExecutionResult(executionId);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                .body(ApiResponse.error("404", "取得執行結果失敗：" + e.getMessage()));
        }
    }

    @DeleteMapping("/{executionId}")
    @Operation(summary = "取消流程執行", description = "取消正在執行的流程")
    public ResponseEntity<ApiResponse<Void>> cancelExecution(
            @Parameter(description = "執行ID", example = "e123-456-789")
            @PathVariable String executionId) {
        try {
            flowExecutionService.cancelExecution(executionId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                .body(ApiResponse.error("400", "取消執行失敗：" + e.getMessage()));
        }
    }
}