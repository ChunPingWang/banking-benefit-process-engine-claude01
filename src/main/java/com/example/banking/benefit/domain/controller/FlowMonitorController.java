package com.example.banking.benefit.domain.controller;

import com.example.banking.benefit.domain.model.statistics.FlowStatistics;
import com.example.banking.benefit.domain.model.statistics.ExecutionDetails;
import com.example.banking.benefit.domain.service.FlowExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monitor")
@RequiredArgsConstructor
public class FlowMonitorController {
    
    private final FlowExecutionService flowExecutionService;

    @GetMapping("/flows/{flowId}/statistics")
    public ResponseEntity<FlowStatistics> getFlowStatistics(@PathVariable String flowId) {
        return ResponseEntity.ok(flowExecutionService.getFlowStatistics(flowId, null, null));
    }

    @GetMapping("/flows/{flowId}/executions")
    public ResponseEntity<List<ExecutionDetails>> getFlowExecutions(
            @PathVariable String flowId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(flowExecutionService.getFlowExecutions(flowId, null, null, null, size, page));
    }

    @GetMapping("/executions/{executionId}")
    public ResponseEntity<ExecutionDetails> getExecutionDetails(@PathVariable String executionId) {
        return ResponseEntity.ok(flowExecutionService.getExecutionDetails(executionId));
    }

    @GetMapping("/flows/statistics")
    public ResponseEntity<List<FlowStatistics>> getAllFlowStatistics() {
        return ResponseEntity.ok(flowExecutionService.getAllFlowStatistics(null, null));
    }
}