package com.example.banking.benefit.domain.service;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.statistics.ExecutionDetails;
import com.example.banking.benefit.domain.model.statistics.FlowStatistics;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程執行服務介面
 */
public interface FlowExecutionService {
    
    /**
     * 執行流程
     *
     * @param flow 流程定義
     * @param context 執行內容
     * @return 執行結果
     * @throws FlowExecutionException 流程執行異常
     */
    ExecutionResult execute(Flow flow, BaseExecutionContext context);
    
    /**
     * 暫停流程執行
     *
     * @param flow 流程定義
     * @param context 執行內容
     */
    void pause(Flow flow, BaseExecutionContext context);
    
    /**
     * 繼續執行流程
     *
     * @param flow 流程定義
     * @param context 執行內容
     * @return 執行結果
     */
    ExecutionResult resume(Flow flow, BaseExecutionContext context);
    
    /**
     * 中止流程執行
     *
     * @param flow 流程定義
     * @param context 執行內容
     */
    void terminate(Flow flow, BaseExecutionContext context);
    
    /**
     * 取得流程執行狀態
     *
     * @param flow 流程定義
     * @param context 執行內容
     * @return 執行狀態
     */
    String getExecutionStatus(Flow flow, BaseExecutionContext context);

    /**
     * 取得流程統計資訊
     *
     * @param flowId 流程ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 流程統計資訊
     */
    FlowStatistics getFlowStatistics(String flowId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 取得流程執行列表
     *
     * @param flowId 流程ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @param status 執行狀態
     * @param pageSize 分頁大小
     * @param pageNumber 頁碼
     * @return 執行列表
     */
    List<ExecutionDetails> getFlowExecutions(String flowId, LocalDateTime startTime, LocalDateTime endTime, String status, int pageSize, int pageNumber);

    /**
     * 取得執行詳細資訊
     *
     * @param executionId 執行ID
     * @return 執行詳細資訊
     */
    ExecutionDetails getExecutionDetails(String executionId);

    /**
     * 取得所有流程統計資訊
     *
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 所有流程統計資訊列表
     */
    List<FlowStatistics> getAllFlowStatistics(LocalDateTime startTime, LocalDateTime endTime);
}