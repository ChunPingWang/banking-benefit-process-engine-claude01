package com.example.banking.benefit.domain.model.decision;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.banking.benefit.domain.model.node.Node;

/**
 * 決策節點實體
 * 負責執行決策邏輯並返回結果
 */
public class DecisionNode implements Node {
    private String nodeId;
    private String flowId;
    private String nodeName;
    private String description;
    private DecisionType decisionType;
    private String implementationClass;
    private String spelExpression;
    private Integer nodeOrder;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    private DecisionNode(String flowId, String nodeName, String description, DecisionType decisionType) {
        this.nodeId = UUID.randomUUID().toString();
        this.flowId = flowId;
        this.nodeName = nodeName;
        this.description = description;
        this.decisionType = decisionType;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    public static DecisionNode createJavaClassDecision(
            String flowId,
            String nodeName,
            String description,
            String implementationClass
    ) {
        DecisionNode node = new DecisionNode(flowId, nodeName, description, DecisionType.JAVA_CLASS);
        node.setImplementationClass(implementationClass);
        return node;
    }

    public static DecisionNode createSpELDecision(
            String flowId,
            String nodeName,
            String description,
            String spelExpression
    ) {
        DecisionNode node = new DecisionNode(flowId, nodeName, description, DecisionType.SPEL);
        node.setSpelExpression(spelExpression);
        return node;
    }

    public void setImplementationClass(String implementationClass) {
        if (this.decisionType != DecisionType.JAVA_CLASS) {
            throw new IllegalStateException("Cannot set implementation class for non-Java class decision");
        }
        if (implementationClass == null || implementationClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Implementation class must not be null or empty");
        }
        this.implementationClass = implementationClass;
        this.updatedTime = LocalDateTime.now();
    }

    public void setSpelExpression(String spelExpression) {
        if (this.decisionType != DecisionType.SPEL) {
            throw new IllegalStateException("Cannot set SpEL expression for non-SpEL decision");
        }
        if (spelExpression == null || spelExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("SpEL expression must not be null or empty");
        }
        this.spelExpression = spelExpression;
        this.updatedTime = LocalDateTime.now();
    }

    public void setNodeOrder(Integer nodeOrder) {
        this.nodeOrder = nodeOrder;
        this.updatedTime = LocalDateTime.now();
    }

    // Getters
    @Override
    public String getNodeId() { return nodeId; }
    public String getFlowId() { return flowId; }
    @Override
    public String getNodeName() { return nodeName; }
    @Override
    public String getDescription() { return description; }
    public DecisionType getDecisionType() { return decisionType; }
    public String getImplementationClass() { return implementationClass; }
    public String getSpelExpression() { return spelExpression; }
    @Override
    public Integer getNodeOrder() { return nodeOrder; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    
    @Override
    public com.example.banking.benefit.domain.model.node.NodeType getNodeType() {
        return com.example.banking.benefit.domain.model.node.NodeType.DECISION;
    }
    
    @Override
    public boolean canExecute(com.example.banking.benefit.domain.model.common.ExecutionContext context) {
        return context != null;
    }
    
    @Override
    public void validate() {
        if (decisionType == DecisionType.JAVA_CLASS && 
            (implementationClass == null || implementationClass.trim().isEmpty())) {
            throw new IllegalStateException("Implementation class must be specified for JAVA_CLASS decision type.");
        }
        if (decisionType == DecisionType.SPEL && 
            (spelExpression == null || spelExpression.trim().isEmpty())) {
            throw new IllegalStateException("SpEL expression must be specified for SPEL decision type.");
        }
    }
}