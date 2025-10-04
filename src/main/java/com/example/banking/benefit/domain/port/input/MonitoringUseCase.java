package com.example.banking.benefit.domain.port.input;

import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.log.ExecutionLog;
import com.example.banking.benefit.domain.model.statistics.FlowStatistics;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 監控用例介面
 * Primary Port - 輸入埠
 */
public interface MonitoringUseCase {
    
    /**
     * 查詢流程執行日誌
     *
     * @param flowId 流程ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 執行日誌列表
     */
    List<ExecutionLog> getExecutionLogs(FlowId flowId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查詢特定執行的日誌
     *
     * @param executionId 執行ID
     * @return 執行日誌列表
     */
    List<ExecutionLog> getExecutionLogsById(String executionId);
    
    /**
     * 查詢流程統計資訊
     *
     * @param flowId 流程ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 流程統計資訊
     */
    FlowStatistics getFlowStatistics(FlowId flowId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 取得當前正在執行的流程數量
     *
     * @return 執行中的流程數量
     */
    int getActiveExecutions();
    
    /**
     * 查詢執行失敗的流程
     *
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 執行日誌列表
     */
    List<ExecutionLog> getFailedExecutions(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 取得流程執行效能指標
     *
     * @param flowId 流程ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 流程統計資訊
     */
    FlowStatistics getPerformanceMetrics(FlowId flowId, LocalDateTime startTime, LocalDateTime endTime);
}