package com.example.banking.benefit.domain.model.node;

/**
 * 決策節點
 * 代表一個流程中的決策點，可以使用 Java 類別或 SpEL 表達式實作決策邏輯
 */
public class DecisionNode implements Node {
    
    private String nodeId;
    private String nodeName;
    private String nodeDescription;
    private DecisionType decisionType;
    private String implementationClass;
    private String spelExpression;
    private Integer nodeOrder;
    
    public String getNodeId() {
        return nodeId;
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    public String getNodeName() {
        return nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    public String getNodeDescription() {
        return nodeDescription;
    }
    
    public void setNodeDescription(String nodeDescription) {
        this.nodeDescription = nodeDescription;
    }
    
    public DecisionType getDecisionType() {
        return decisionType;
    }
    
    public void setDecisionType(DecisionType decisionType) {
        this.decisionType = decisionType;
    }
    
    public String getImplementationClass() {
        return implementationClass;
    }
    
    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }
    
    public String getSpelExpression() {
        return spelExpression;
    }
    
    public void setSpelExpression(String spelExpression) {
        this.spelExpression = spelExpression;
    }
    
    public Integer getNodeOrder() {
        return nodeOrder;
    }
    
    public void setNodeOrder(Integer nodeOrder) {
        this.nodeOrder = nodeOrder;
    }
    
    @Override
    public boolean isDecisionNode() {
        return true;
    }
    
    @Override
    public boolean isProcessNode() {
        return false;
    }
}