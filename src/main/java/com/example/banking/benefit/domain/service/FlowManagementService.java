package com.example.banking.benefit.domain.service;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.flow.Version;
import java.util.List;
import java.util.Optional;

public interface FlowManagementService {
    
    /**
     * 創建新的流程
     *
     * @param flowId 流程ID
     * @param name 流程名稱
     * @param description 流程描述
     * @param version 流程版本
     * @return 創建的流程
     */
    Flow createFlow(FlowId flowId, String name, String description, Version version);
    
    /**
     * 根據ID取得流程
     *
     * @param flowId 流程ID
     * @return 流程物件
     */
    Optional<Flow> getFlow(FlowId flowId);
    
    /**
     * 取得所有流程
     *
     * @return 流程列表
     */
    List<Flow> getAllFlows();
    
    /**
     * 更新流程
     *
     * @param flow 更新後的流程
     * @return 更新後的流程
     */
    Flow updateFlow(Flow flow);
    
    /**
     * 刪除流程
     *
     * @param flowId 流程ID
     */
    void deleteFlow(FlowId flowId);
    
    /**
     * 根據ID和版本取得流程
     *
     * @param flowId 流程ID
     * @param version 流程版本
     * @return 流程物件
     */
    Optional<Flow> getFlow(FlowId flowId, String version);
}