package com.example.banking.benefit.domain.repository;

import com.example.banking.benefit.domain.model.log.ExecutionLog;
import com.example.banking.benefit.domain.model.flow.FlowId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 執行日誌儲存庫介面
 */
public interface ExecutionLogRepository extends BaseRepository<ExecutionLog, String> {
    
    /**
     * 根據流程ID查詢執行日誌
     *
     * @param flowId 流程ID
     * @return 執行日誌列表
     */
    List<ExecutionLog> findByFlowId(FlowId flowId);
    
    /**
     * 根據客戶ID查詢執行日誌
     *
     * @param customerId 客戶ID
     * @return 執行日誌列表
     */
    List<ExecutionLog> findByCustomerId(String customerId);
    
    /**
     * 查詢時間範圍內的執行日誌
     *
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 執行日誌列表
     */
    List<ExecutionLog> findByExecutionTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根據執行結果查詢執行日誌
     *
     * @param result 執行結果
     * @return 執行日誌列表
     */
    List<ExecutionLog> findByExecutionResult(String result);
    
    /**
     * 查詢執行時間超過指定毫秒數的執行日誌
     *
     * @param durationMs 執行時間(毫秒)
     * @return 執行日誌列表
     */
    List<ExecutionLog> findByExecutionDurationGreaterThan(int durationMs);
    
    /**
     * 根據節點ID查詢執行日誌
     *
     * @param nodeId 節點ID
     * @return 執行日誌列表
     */
    List<ExecutionLog> findByNodeId(String nodeId);
    
    /**
     * 刪除指定時間之前的執行日誌
     *
     * @param dateTime 時間點
     */
    void deleteByExecutionTimeBefore(LocalDateTime dateTime);
    
    /**
     * 根據流程ID和時間範圍查詢執行日誌
     *
     * @param flowId 流程ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 執行日誌列表
     */
    List<ExecutionLog> findByFlowIdAndExecutionTimeBetween(String flowId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根據流程ID、執行狀態和時間範圍分頁查詢執行日誌
     *
     * @param flowId 流程ID
     * @param status 執行狀態
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @param pageable 分頁參數
     * @return 執行日誌分頁
     */
    Page<ExecutionLog> findByFlowIdAndExecutionResultAndExecutionTimeBetween(
            String flowId, String status, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}