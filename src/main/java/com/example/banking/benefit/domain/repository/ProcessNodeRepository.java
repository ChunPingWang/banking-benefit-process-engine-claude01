package com.example.banking.benefit.domain.repository;

import com.example.banking.benefit.domain.model.process.ProcessNode;
import com.example.banking.benefit.domain.model.flow.FlowId;

import java.util.List;

/**
 * 處理節點儲存庫介面
 */
public interface ProcessNodeRepository extends BaseRepository<ProcessNode, String> {
    
    /**
     * 根據流程ID查詢處理節點
     *
     * @param flowId 流程ID
     * @return 處理節點列表
     */
    List<ProcessNode> findByFlowId(FlowId flowId);
    
    /**
     * 根據狀態名稱查詢處理節點
     *
     * @param stateName 狀態名稱
     * @return 處理節點列表
     */
    List<ProcessNode> findByStateName(String stateName);
    
    /**
     * 根據流程ID刪除所有處理節點
     *
     * @param flowId 流程ID
     */
    void deleteByFlowId(FlowId flowId);
    
    /**
     * 取得特定流程的處理節點數量
     *
     * @param flowId 流程ID
     * @return 節點數量
     */
    long countByFlowId(FlowId flowId);
    
    /**
     * 根據實作類別查詢處理節點
     *
     * @param implementationClass 實作類別名稱
     * @return 處理節點列表
     */
    List<ProcessNode> findByImplementationClass(String implementationClass);
    
    /**
     * 查詢特定流程的起始處理節點
     *
     * @param flowId 流程ID
     * @return 處理節點列表
     */
    List<ProcessNode> findStartNodesByFlowId(FlowId flowId);
}