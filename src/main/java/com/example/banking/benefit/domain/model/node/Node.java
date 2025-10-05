package com.example.banking.benefit.domain.model.node;

import com.example.banking.benefit.domain.model.common.ExecutionContext;

/**
 * 節點介面
 * 定義流程中所有節點的共同行為
 */
public interface Node {
    /**
     * 獲取節點 ID
     *
     * @return 節點 ID
     */
    String getNodeId();
    
    /**
     * 獲取節點名稱
     *
     * @return 節點名稱
     */
    String getNodeName();
    
    /**
     * 獲取節點描述
     *
     * @return 節點描述
     */
    String getDescription();
    
    /**
     * 獲取節點順序
     *
     * @return 節點順序
     */
    Integer getNodeOrder();
    
    /**
     * 獲取節點類型
     *
     * @return 節點類型
     */
    NodeType getNodeType();
    
    /**
     * 檢查是否可以執行此節點
     *
     * @param context 執行上下文
     * @return 如果可以執行返回 true，否則返回 false
     */
    boolean canExecute(ExecutionContext context);
    
    /**
     * 驗證節點配置是否正確
     *
     * @throws IllegalStateException 如果節點配置無效
     */
    void validate();
}