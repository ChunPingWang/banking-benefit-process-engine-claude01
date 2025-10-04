package com.example.banking.benefit.domain.repository;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;

import java.util.List;
import java.util.Optional;

/**
 * 流程定義儲存庫介面
 */
public interface FlowRepository extends BaseRepository<Flow, FlowId> {
    
    /**
     * 根據狀態查詢流程定義
     *
     * @param status 狀態
     * @return 流程定義列表
     */
    List<Flow> findByStatus(String status);
    
    /**
     * 查詢最新版本的流程定義
     *
     * @param flowId 流程ID
     * @return 流程定義
     */
    Optional<Flow> findLatestVersion(FlowId flowId);
    
    /**
     * 查詢特定版本的流程定義
     *
     * @param flowId 流程ID
     * @param version 版本
     * @return 流程定義
     */
    Optional<Flow> findByVersion(FlowId flowId, String version);
    
    /**
     * 查詢流程的所有版本
     *
     * @param flowId 流程ID
     * @return 流程定義列表
     */
    List<Flow> findAllVersions(FlowId flowId);
    
    /**
     * 檢查流程ID是否已存在
     *
     * @param flowId 流程ID
     * @return 是否存在
     */
    boolean existsByFlowId(FlowId flowId);
}