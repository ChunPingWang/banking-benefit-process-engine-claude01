package com.example.banking.benefit.domain.repository;

import com.example.banking.benefit.domain.model.node.NodeRelation;
import com.example.banking.benefit.domain.model.flow.FlowId;

import java.util.List;

/**
 * 節點關係儲存庫介面
 */
public interface NodeRelationRepository extends BaseRepository<NodeRelation, String> {
    
    /**
     * 根據流程ID查詢節點關係
     *
     * @param flowId 流程ID
     * @return 節點關係列表
     */
    List<NodeRelation> findByFlowId(FlowId flowId);
    
    /**
     * 根據來源節點ID查詢節點關係
     *
     * @param sourceNodeId 來源節點ID
     * @return 節點關係列表
     */
    List<NodeRelation> findBySourceNodeId(String sourceNodeId);
    
    /**
     * 根據目標節點ID查詢節點關係
     *
     * @param targetNodeId 目標節點ID
     * @return 節點關係列表
     */
    List<NodeRelation> findByTargetNodeId(String targetNodeId);
    
    /**
     * 根據關係類型查詢節點關係
     *
     * @param relationType 關係類型
     * @return 節點關係列表
     */
    List<NodeRelation> findByRelationType(String relationType);
    
    /**
     * 根據流程ID刪除節點關係
     *
     * @param flowId 流程ID
     */
    void deleteByFlowId(FlowId flowId);
    
    /**
     * 檢查節點之間是否存在關係
     *
     * @param sourceNodeId 來源節點ID
     * @param targetNodeId 目標節點ID
     * @return 是否存在關係
     */
    boolean existsBySourceNodeIdAndTargetNodeId(String sourceNodeId, String targetNodeId);
    
    /**
     * 查詢孤立的節點（沒有任何關聯的節點）
     *
     * @param flowId 流程ID
     * @return 孤立節點ID列表
     */
    List<String> findOrphanNodeIds(FlowId flowId);
}