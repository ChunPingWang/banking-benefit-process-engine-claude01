package com.example.banking.benefit.domain.service;

import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.statistics.ExecutionDetails;
import com.example.banking.benefit.domain.model.statistics.FlowStatistics;

import java.util.List;
import java.util.Optional;

/**
 * 流程監控服務介面
 */
public interface FlowMonitorService {

    /**
     * 獲取指定流程的統計資訊
     *
     * @param flowId 流程ID
     * @return 流程統計資訊
     */
    Optional<FlowStatistics> getFlowStatistics(FlowId flowId);

    /**
     * 獲取指定流程的執行列表
     *
     * @param flowId 流程ID
     * @param limit 最大返回數量
     * @param offset 起始位置
     * @return 執行列表
     */
    List<ExecutionDetails> getFlowExecutions(FlowId flowId, int limit, int offset);

    /**
     * 獲取執行詳細資訊
     *
     * @param executionId 執行ID
     * @return 執行詳細資訊
     */
    Optional<ExecutionDetails> getExecutionDetails(String executionId);

    /**
     * 獲取所有流程的統計資訊
     *
     * @return 所有流程的統計資訊列表
     */
    List<FlowStatistics> getAllFlowStatistics();
}