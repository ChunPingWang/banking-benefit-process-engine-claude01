package com.example.banking.benefit.domain.model.node;

/**
 * 節點介面
 * 定義流程中所有節點的共同行為
 */
public interface Node {
    String getNodeId();
    String getNodeName();
    String getNodeDescription();
    Integer getNodeOrder();
    boolean isDecisionNode();
    boolean isProcessNode();
}