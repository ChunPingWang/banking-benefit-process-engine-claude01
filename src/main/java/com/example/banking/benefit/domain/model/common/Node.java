package com.example.banking.benefit.domain.model.common;

/**
 * 節點基礎類別
 */
public abstract class Node {
    private String nodeId;
    private String nodeName;
    private String nodeDescription;
    private Integer nodeOrder;

    protected Node(String nodeId, String nodeName, String nodeDescription, Integer nodeOrder) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.nodeDescription = nodeDescription;
        this.nodeOrder = nodeOrder;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getNodeDescription() {
        return nodeDescription;
    }

    public Integer getNodeOrder() {
        return nodeOrder;
    }

    /**
     * 檢查是否為決策節點
     */
    public boolean isDecisionNode() {
        return false;
    }

    /**
     * 檢查是否為處理節點
     */
    public boolean isProcessNode() {
        return false;
    }

    /**
     * 驗證節點設定是否有效
     */
    public abstract boolean isValid();
}