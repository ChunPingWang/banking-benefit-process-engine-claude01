package com.example.banking.benefit.domain.port.input;

import com.example.banking.benefit.domain.model.config.FlowConfig;
import com.example.banking.benefit.domain.model.flow.FlowId;

import java.util.Optional;
import java.util.Map;

/**
 * 配置用例介面
 * Primary Port - 輸入埠
 */
public interface ConfigurationUseCase {
    
    /**
     * 設定流程配置
     *
     * @param flowId 流程ID
     * @param config 配置資訊
     */
    void setFlowConfig(FlowId flowId, FlowConfig config);
    
    /**
     * 取得流程配置
     *
     * @param flowId 流程ID
     * @return 配置資訊
     */
    Optional<FlowConfig> getFlowConfig(FlowId flowId);
    
    /**
     * 更新流程配置
     *
     * @param flowId 流程ID
     * @param config 配置資訊
     */
    void updateFlowConfig(FlowId flowId, FlowConfig config);
    
    /**
     * 刪除流程配置
     *
     * @param flowId 流程ID
     */
    void deleteFlowConfig(FlowId flowId);
    
    /**
     * 檢查流程配置是否存在
     *
     * @param flowId 流程ID
     * @return 是否存在
     */
    boolean hasFlowConfig(FlowId flowId);
    
    /**
     * 取得所有流程配置
     *
     * @return 配置資訊映射表
     */
    Map<FlowId, FlowConfig> getAllFlowConfigs();
}