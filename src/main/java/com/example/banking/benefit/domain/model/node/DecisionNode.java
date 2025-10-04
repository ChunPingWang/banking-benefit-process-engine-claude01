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
    
    private DecisionNode(String nodeId, String nodeName, String nodeDescription,
                         DecisionType decisionType, String implementationClass, String spelExpression) {
        if (nodeId == null || nodeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Node ID cannot be null or empty.");
        }
        if (nodeName == null || nodeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Node name cannot be null or empty.");
        }
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.nodeDescription = nodeDescription;
        this.decisionType = decisionType;
        this.implementationClass = implementationClass;
        this.spelExpression = spelExpression;
    }

    public static DecisionNode createJavaClassDecision(String nodeId, String nodeName, String description, String implementationClass) {
        if (implementationClass == null || implementationClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Implementation class cannot be null or empty for JAVA_CLASS decision type.");
        }
        return new DecisionNode(nodeId, nodeName, description, DecisionType.JAVA_CLASS, implementationClass, null);
    }

    public static DecisionNode createSpELDecision(String nodeId, String nodeName, String description, String spelExpression) {
        if (spelExpression == null || spelExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("SpEL expression cannot be null or empty for SPEL decision type.");
        }
        return new DecisionNode(nodeId, nodeName, description, DecisionType.SPEL, null, spelExpression);
    }
    
    @Override
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
    public String getDescription() {
        return nodeDescription;
    }
}