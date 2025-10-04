package com.example.banking.benefit.domain.port.input;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;

import java.util.List;
import java.util.Optional;

/**
 * 流程管理用例介面
 * Primary Port - 輸入埠
 */
public interface FlowManagementUseCase {
    
    /**
     * 創建新流程
     *
     * @param flow 流程定義
     * @return 已創建的流程
     */
    Flow createFlow(Flow flow);
    
    /**
     * 更新流程
     *
     * @param flow 流程定義
     * @return 已更新的流程
     */
    Flow updateFlow(Flow flow);
    
    /**
     * 刪除流程
     *
     * @param flowId 流程ID
     */
    void deleteFlow(FlowId flowId);
    
    /**
     * 啟用流程
     *
     * @param flowId 流程ID
     * @param version 版本
     * @return 已啟用的流程
     */
    Flow activateFlow(FlowId flowId, String version);
    
    /**
     * 停用流程
     *
     * @param flowId 流程ID
     * @param version 版本
     * @return 已停用的流程
     */
    Flow deactivateFlow(FlowId flowId, String version);
    
    /**
     * 查詢流程
     *
     * @param flowId 流程ID
     * @return 流程定義
     */
    Optional<Flow> getFlow(FlowId flowId);
    
    /**
     * 查詢所有流程
     *
     * @return 流程定義列表
     */
    List<Flow> getAllFlows();
    
    /**
     * 查詢特定版本的流程
     *
     * @param flowId 流程ID
     * @param version 版本
     * @return 流程定義
     */
    Optional<Flow> getFlowVersion(FlowId flowId, String version);
    
    /**
     * 查詢流程的所有版本
     *
     * @param flowId 流程ID
     * @return 流程定義列表
     */
    List<Flow> getFlowVersions(FlowId flowId);
}