package com.example.banking.benefit.infrastructure.persistence.repository;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.repository.FlowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程儲存庫的記憶體實作
 * 用於測試目的的簡單實作
 */
@Repository
@Slf4j
public class InMemoryFlowRepository implements FlowRepository {

    private final Map<FlowId, Flow> flows = new ConcurrentHashMap<>();

    @Override
    public Flow save(Flow flow) {
        log.debug("Saving flow with ID: {}", flow.getFlowId().getValue());
        flows.put(flow.getFlowId(), flow);
        return flow;
    }

    @Override
    public Optional<Flow> findById(FlowId id) {
        log.debug("Finding flow by ID: {}", id.getValue());
        return Optional.ofNullable(flows.get(id));
    }

    @Override
    public List<Flow> findAll() {
        log.debug("Finding all flows, total count: {}", flows.size());
        return new ArrayList<>(flows.values());
    }

    @Override
    public void delete(Flow entity) {
        log.debug("Deleting flow: {}", entity.getFlowId().getValue());
        flows.remove(entity.getFlowId());
    }

    @Override
    public void deleteById(FlowId id) {
        log.debug("Deleting flow by ID: {}", id.getValue());
        flows.remove(id);
    }

    @Override
    public boolean existsById(FlowId id) {
        boolean exists = flows.containsKey(id);
        log.debug("Checking if flow exists by ID: {}, result: {}", id.getValue(), exists);
        return exists;
    }

    @Override
    public List<Flow> findByStatus(String status) {
        log.debug("Finding flows by status: {}", status);
        return flows.values().stream()
                .filter(flow -> flow.getStatus().name().equals(status))
                .toList();
    }

    @Override
    public Optional<Flow> findLatestVersion(FlowId flowId) {
        log.debug("Finding latest version for flow ID: {}", flowId.getValue());
        return flows.values().stream()
                .filter(flow -> flow.getFlowId().equals(flowId))
                .max(Comparator.comparing(flow -> flow.getVersion().getValue()));
    }

    @Override
    public Optional<Flow> findByVersion(FlowId flowId, String version) {
        log.debug("Finding flow by ID: {} and version: {}", flowId.getValue(), version);
        return flows.values().stream()
                .filter(flow -> flow.getFlowId().equals(flowId) && 
                               flow.getVersion().getValue().equals(version))
                .findFirst();
    }

    @Override
    public List<Flow> findAllVersions(FlowId flowId) {
        log.debug("Finding all versions for flow ID: {}", flowId.getValue());
        return flows.values().stream()
                .filter(flow -> flow.getFlowId().equals(flowId))
                .sorted(Comparator.comparing(flow -> flow.getVersion().getValue()))
                .toList();
    }

    @Override
    public boolean existsByFlowId(FlowId flowId) {
        boolean exists = flows.values().stream()
                .anyMatch(flow -> flow.getFlowId().equals(flowId));
        log.debug("Checking if flow exists by flow ID: {}, result: {}", flowId.getValue(), exists);
        return exists;
    }

    /**
     * 清除所有資料，用於測試
     */
    public void clear() {
        log.debug("Clearing all flows");
        flows.clear();
    }

    @Override
    public long count() {
        return flows.size();
    }

    /**
     * 取得總數，用於測試
     */
    public int size() {
        return flows.size();
    }
}