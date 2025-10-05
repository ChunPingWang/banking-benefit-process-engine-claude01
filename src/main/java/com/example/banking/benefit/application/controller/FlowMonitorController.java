package com.example.banking.benefit.application.controller;

import com.example.banking.benefit.application.converter.MonitoringConverter;
import com.example.banking.benefit.application.dto.common.ApiResponse;
import com.example.banking.benefit.application.dto.monitor.ExecutionDetails;
import com.example.banking.benefit.application.dto.monitor.FlowStatistics;
import com.example.banking.benefit.domain.service.FlowExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flow-monitor")
@Tag(name = "Flow Monitor", description = "流程監控相關 API")
public class FlowMonitorController {

    private final FlowExecutionService flowExecutionService;
    private final MonitoringConverter monitoringConverter;

    public FlowMonitorController(FlowExecutionService flowExecutionService, MonitoringConverter monitoringConverter) {
        this.flowExecutionService = flowExecutionService;
        this.monitoringConverter = monitoringConverter;
    }

    @GetMapping("/statistics/{flowId}")
    @Operation(summary = "取得流程統計資訊", description = "取得指定流程的執行統計資訊")
    public ResponseEntity<ApiResponse<FlowStatistics>> getFlowStatistics(
            @Parameter(description = "流程ID", example = "f123-456-789")
            @PathVariable String flowId,
            @Parameter(description = "開始時間", example = "2025-10-04T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "結束時間", example = "2025-10-04T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            var domainStats = flowExecutionService.getFlowStatistics(flowId, startTime, endTime);
            var statistics = monitoringConverter.toDto(domainStats);
            var response = ApiResponse.<FlowStatistics>success(statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            var response = ApiResponse.<FlowStatistics>error("404", "找不到流程統計資訊：" + e.getMessage());
            return ResponseEntity.status(404)
                .headers(headers -> headers.add("X-Error-Code", "404"))
                .body(response);
        }
    }

    @GetMapping("/executions/{flowId}")
    @Operation(summary = "取得流程執行列表", description = "取得指定流程的執行記錄列表")
    public ResponseEntity<ApiResponse<List<ExecutionDetails>>> getFlowExecutions(
            @Parameter(description = "流程ID", example = "f123-456-789")
            @PathVariable String flowId,
            @Parameter(description = "開始時間", example = "2025-10-04T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "結束時間", example = "2025-10-04T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "執行狀態", example = "COMPLETED")
            @RequestParam(required = false) String status,
            @Parameter(description = "分頁大小", example = "10")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "頁碼", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber) {
        try {
            var domainExecutions = flowExecutionService.getFlowExecutions(
                flowId, startTime, endTime, status, pageSize, pageNumber);
            var executions = monitoringConverter.toDtoList(domainExecutions);
            var response = ApiResponse.<List<ExecutionDetails>>success(executions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            var response = ApiResponse.<List<ExecutionDetails>>error("400", "獲取執行列表失敗：" + e.getMessage());
            return ResponseEntity.status(400)
                .headers(headers -> headers.add("X-Error-Code", "400"))
                .body(response);
        }
    }

    @GetMapping("/execution/{executionId}")
    @Operation(summary = "取得執行詳細資訊", description = "根據執行ID取得詳細的執行資訊")
    public ResponseEntity<ApiResponse<ExecutionDetails>> getExecutionDetails(
            @Parameter(description = "執行ID", example = "e123-456-789")
            @PathVariable String executionId) {
        try {
            var domainDetails = flowExecutionService.getExecutionDetails(executionId);
            var details = monitoringConverter.toDto(domainDetails);
            var response = ApiResponse.<ExecutionDetails>success(details);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            var response = ApiResponse.<ExecutionDetails>error("404", "找不到執行詳細資訊：" + e.getMessage());
            return ResponseEntity.status(404)
                .headers(headers -> headers.add("X-Error-Code", "404"))
                .body(response);
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "取得所有流程統計資訊", description = "取得系統中所有流程的統計資訊")
    public ResponseEntity<ApiResponse<List<FlowStatistics>>> getAllFlowStatistics(
            @Parameter(description = "開始時間", example = "2025-10-04T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "結束時間", example = "2025-10-04T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            var domainStatsList = flowExecutionService.getAllFlowStatistics(startTime, endTime);
            var statistics = monitoringConverter.toDto(domainStatsList);
            var response = ApiResponse.<List<FlowStatistics>>success(statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            var response = ApiResponse.<List<FlowStatistics>>error("400", "獲取統計資訊失敗：" + e.getMessage());
            return ResponseEntity.status(400)
                .headers(headers -> headers.add("X-Error-Code", "400"))
                .body(response);
        }
    }
}