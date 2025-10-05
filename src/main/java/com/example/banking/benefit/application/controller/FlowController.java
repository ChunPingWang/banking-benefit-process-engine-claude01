package com.example.banking.benefit.application.controller;

import com.example.banking.benefit.application.dto.common.ApiResponse;
import com.example.banking.benefit.application.dto.flow.CreateFlowRequest;
import com.example.banking.benefit.application.dto.flow.FlowResponse;
import com.example.banking.benefit.application.dto.flow.UpdateFlowRequest;
import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.flow.Version;
import com.example.banking.benefit.domain.service.FlowManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flows")
@RequiredArgsConstructor
@Tag(name = "Flow Management", description = "流程管理相關 API")
public class FlowController extends BaseController {

    private final FlowManagementService flowManagementService;

    @PostMapping
    @Operation(summary = "建立新流程", description = "建立一個新的流程定義")
    public ResponseEntity<ApiResponse<FlowResponse>> createFlow(
            @RequestBody CreateFlowRequest request) {
        try {
            FlowId flowId = FlowId.generate();
            Version version = Version.of(request.getVersion());
            
            Flow flow = flowManagementService.createFlow(
                flowId,
                request.getName(),
                request.getDescription(),
                version
            );
            
            FlowResponse response = mapToFlowResponse(flow);
            return success(response);
        } catch (Exception e) {
            return error("500", "建立流程失敗：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "取得流程資訊", description = "根據流程ID取得流程詳細資訊")
    public ResponseEntity<ApiResponse<FlowResponse>> getFlow(
            @Parameter(description = "流程ID", example = "f123-456-789")
            @PathVariable String id) {
        try {
            FlowId flowId = FlowId.of(id);
            Flow flow = flowManagementService.getFlow(flowId)
                    .orElseThrow(() -> new RuntimeException("流程不存在"));
            FlowResponse response = mapToFlowResponse(flow);
            return success(response);
        } catch (Exception e) {
            return error("404", "取得流程失敗：" + e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "取得所有流程", description = "取得系統中所有已定義的流程")
    public ResponseEntity<ApiResponse<List<FlowResponse>>> getAllFlows() {
        try {
            List<Flow> flows = flowManagementService.getAllFlows();
            List<FlowResponse> responses = flows.stream()
                    .map(this::mapToFlowResponse)
                    .collect(Collectors.toList());
            return success(responses);
        } catch (Exception e) {
            return error("500", "取得所有流程失敗：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新流程", description = "更新指定流程的資訊")
    public ResponseEntity<ApiResponse<FlowResponse>> updateFlow(
            @Parameter(description = "流程ID", example = "f123-456-789")
            @PathVariable String id,
            @RequestBody UpdateFlowRequest request) {
        try {
            FlowId flowId = FlowId.of(id);
            Flow existingFlow = flowManagementService.getFlow(flowId)
                    .orElseThrow(() -> new RuntimeException("流程不存在"));
            
            Version version = Version.of(request.getVersion());
            Flow updatedFlow = Flow.create(
                flowId,
                request.getName(),
                request.getDescription(),
                version
            );
            
            Flow savedFlow = flowManagementService.updateFlow(updatedFlow);
            FlowResponse response = mapToFlowResponse(savedFlow);
            return success(response);
        } catch (Exception e) {
            return error("404", "更新流程失敗：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除流程", description = "刪除指定的流程定義")
    public ResponseEntity<ApiResponse<Void>> deleteFlow(
            @Parameter(description = "流程ID", example = "f123-456-789")
            @PathVariable String id) {
        try {
            FlowId flowId = FlowId.of(id);
            flowManagementService.deleteFlow(flowId);
            return success();
        } catch (Exception e) {
            return error("404", "刪除流程失敗：" + e.getMessage());
        }
    }

    private FlowResponse mapToFlowResponse(Flow flow) {
        return FlowResponse.builder()
                .id(flow.getFlowId().getValue())
                .name(flow.getFlowName())
                .description(flow.getDescription())
                .version(flow.getVersion().toString())
                .createdAt(flow.getCreatedTime().toString())
                .updatedAt(flow.getUpdatedTime().toString())
                .build();
    }
}