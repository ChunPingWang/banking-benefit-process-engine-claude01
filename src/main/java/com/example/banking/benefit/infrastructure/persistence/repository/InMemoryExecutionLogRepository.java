package com.example.banking.benefit.infrastructure.persistence.repository;

import com.example.banking.benefit.domain.model.log.ExecutionLog;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.repository.ExecutionLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 執行日誌儲存庫的記憶體實作
 * 用於測試目的的簡單實作
 */
@Repository
@Slf4j
public class InMemoryExecutionLogRepository implements ExecutionLogRepository {

    private final Map<String, ExecutionLog> logs = new ConcurrentHashMap<>();

    @Override
    public ExecutionLog save(ExecutionLog executionLog) {
        log.debug("Saving execution log with ID: {}", executionLog.getLogId());
        logs.put(executionLog.getLogId(), executionLog);
        return executionLog;
    }

    @Override
    public Optional<ExecutionLog> findById(String id) {
        log.debug("Finding execution log by ID: {}", id);
        return Optional.ofNullable(logs.get(id));
    }

    @Override
    public List<ExecutionLog> findAll() {
        log.debug("Finding all execution logs, total count: {}", logs.size());
        return new ArrayList<>(logs.values());
    }

    @Override
    public void delete(ExecutionLog entity) {
        log.debug("Deleting execution log: {}", entity.getLogId());
        logs.remove(entity.getLogId());
    }

    @Override
    public void deleteById(String id) {
        log.debug("Deleting execution log by ID: {}", id);
        logs.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        boolean exists = logs.containsKey(id);
        log.debug("Checking if execution log exists by ID: {}, result: {}", id, exists);
        return exists;
    }

    @Override
    public long count() {
        return logs.size();
    }

    @Override
    public List<ExecutionLog> findByFlowId(FlowId flowId) {
        log.debug("Finding execution logs by flow ID: {}", flowId.getValue());
        return logs.values().stream()
                .filter(executionLog -> executionLog.getFlowId().equals(flowId.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExecutionLog> findByCustomerId(String customerId) {
        log.debug("Finding execution logs by customer ID: {}", customerId);
        return logs.values().stream()
                .filter(executionLog -> customerId.equals(executionLog.getCustomerId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExecutionLog> findByExecutionTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding execution logs between {} and {}", startTime, endTime);
        return logs.values().stream()
                .filter(executionLog -> {
                    LocalDateTime executionTime = executionLog.getExecutionTime();
                    return executionTime.isAfter(startTime) && executionTime.isBefore(endTime);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ExecutionLog> findByExecutionResult(String result) {
        log.debug("Finding execution logs by result: {}", result);
        return logs.values().stream()
                .filter(executionLog -> executionLog.getExecutionResult() != null && 
                                       result.equals(executionLog.getExecutionResult().name()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExecutionLog> findByExecutionDurationGreaterThan(int durationMs) {
        log.debug("Finding execution logs with duration greater than: {} ms", durationMs);
        return logs.values().stream()
                .filter(executionLog -> executionLog.getExecutionDurationMs() != null && executionLog.getExecutionDurationMs() > durationMs)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExecutionLog> findByNodeId(String nodeId) {
        log.debug("Finding execution logs by node ID: {}", nodeId);
        return logs.values().stream()
                .filter(executionLog -> nodeId.equals(executionLog.getNodeId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByExecutionTimeBefore(LocalDateTime dateTime) {
        log.debug("Deleting execution logs before: {}", dateTime);
        logs.entrySet().removeIf(entry -> 
            entry.getValue().getExecutionTime().isBefore(dateTime));
    }

    @Override
    public List<ExecutionLog> findByFlowIdAndExecutionTimeBetween(String flowId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding execution logs by flow ID: {} between {} and {}", flowId, startTime, endTime);
        return logs.values().stream()
                .filter(executionLog -> flowId.equals(executionLog.getFlowId()))
                .filter(executionLog -> {
                    LocalDateTime executionTime = executionLog.getExecutionTime();
                    return executionTime.isAfter(startTime) && executionTime.isBefore(endTime);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<ExecutionLog> findByFlowIdAndExecutionResultAndExecutionTimeBetween(
            String flowId, String status, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding execution logs by flow ID: {}, status: {} between {} and {}", 
                 flowId, status, startTime, endTime);
        
        List<ExecutionLog> filtered = logs.values().stream()
                .filter(executionLog -> flowId.equals(executionLog.getFlowId()))
                .filter(executionLog -> executionLog.getExecutionResult() != null && 
                                       status.equals(executionLog.getExecutionResult().name()))
                .filter(executionLog -> {
                    LocalDateTime executionTime = executionLog.getExecutionTime();
                    return executionTime.isAfter(startTime) && executionTime.isBefore(endTime);
                })
                .collect(Collectors.toList());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<ExecutionLog> pageContent = filtered.subList(start, end);
        
        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    /**
     * 清除所有資料，用於測試
     */
    public void clear() {
        log.debug("Clearing all execution logs");
        logs.clear();
    }

    /**
     * 取得總數，用於測試
     */
    public int size() {
        return logs.size();
    }
}