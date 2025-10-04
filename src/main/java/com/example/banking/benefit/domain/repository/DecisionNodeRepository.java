package com.example.banking.benefit.domain.repository;

import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.flow.FlowId;

import java.util.List;

/**
 * 決策節點儲存庫介面
 */
public interface DecisionNodeRepository extends BaseRepository<DecisionNode, String> {
    
    /**
     * 根據流程ID查詢決策節點
     *
     * @param flowId 流程ID
     * @return 決策節點列表
     */
    List<DecisionNode> findByFlowId(FlowId flowId);
    
    /**
     * 根據群組ID查詢決策節點
     *
     * @param groupId 群組ID
     * @return 決策節點列表
     */
    List<DecisionNode> findByGroupId(String groupId);
    
    /**
     * 根據流程ID刪除所有決策節點
     *
     * @param flowId 流程ID
     */
    void deleteByFlowId(FlowId flowId);
    
    /**
     * 取得特定流程的決策節點數量
     *
     * @param flowId 流程ID
     * @return 節點數量
     */
    long countByFlowId(FlowId flowId);
    
    /**
     * 根據實作類別查詢決策節點
     *
     * @param implementationClass 實作類別名稱
     * @return 決策節點列表
     */
    List<DecisionNode> findByImplementationClass(String implementationClass);
}