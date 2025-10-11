package com.example.banking.benefit.application.service;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.flow.Version;
import com.example.banking.benefit.domain.repository.FlowRepository;
import com.example.banking.benefit.domain.service.FlowManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 流程管理服務實作
 * 應用層服務，實作流程管理相關的業務邏輯
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowManagementServiceImpl implements FlowManagementService {

    private final FlowRepository flowRepository;

    @Override
    public Flow createFlow(FlowId flowId, String name, String description, Version version) {
        log.info("Creating flow with ID: {}, name: {}", flowId.getValue(), name);
        
        // 檢查流程是否已存在
        if (flowRepository.existsById(flowId)) {
            throw new IllegalArgumentException("Flow with ID " + flowId.getValue() + " already exists");
        }
        
        // 建立新流程
        Flow flow = Flow.create(flowId, name, description, version);
        
        // 儲存流程
        Flow savedFlow = flowRepository.save(flow);
        
        log.info("Successfully created flow with ID: {}", savedFlow.getFlowId().getValue());
        return savedFlow;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flow> getFlow(FlowId flowId) {
        log.debug("Getting flow with ID: {}", flowId.getValue());
        return flowRepository.findById(flowId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flow> getAllFlows() {
        log.debug("Getting all flows");
        return flowRepository.findAll();
    }

    @Override
    public Flow updateFlow(Flow flow) {
        log.info("Updating flow with ID: {}", flow.getFlowId().getValue());
        
        // 檢查流程是否存在
        if (!flowRepository.existsById(flow.getFlowId())) {
            throw new IllegalArgumentException("Flow with ID " + flow.getFlowId().getValue() + " not found");
        }
        
        // 更新流程
        Flow updatedFlow = flowRepository.save(flow);
        
        log.info("Successfully updated flow with ID: {}", updatedFlow.getFlowId().getValue());
        return updatedFlow;
    }

    @Override
    public void deleteFlow(FlowId flowId) {
        log.info("Deleting flow with ID: {}", flowId.getValue());
        
        // 檢查流程是否存在
        if (!flowRepository.existsById(flowId)) {
            throw new IllegalArgumentException("Flow with ID " + flowId.getValue() + " not found");
        }
        
        // 刪除流程
        flowRepository.deleteById(flowId);
        
        log.info("Successfully deleted flow with ID: {}", flowId.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flow> getFlow(FlowId flowId, String version) {
        log.debug("Getting flow with ID: {} and version: {}", flowId.getValue(), version);
        return flowRepository.findByVersion(flowId, version);
    }
}