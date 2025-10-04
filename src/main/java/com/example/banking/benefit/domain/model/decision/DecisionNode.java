package com.example.banking.benefit.domain.model.decision;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 決策節點實體
 * 負責執行決策邏輯並返回結果
 */
public class DecisionNode {
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
    public String getNodeId() { return nodeId; }
    public String getFlowId() { return flowId; }
    public String getNodeName() { return nodeName; }
    public String getDescription() { return description; }
    public DecisionType getDecisionType() { return decisionType; }
    public String getImplementationClass() { return implementationClass; }
    public String getSpelExpression() { return spelExpression; }
    public Integer getNodeOrder() { return nodeOrder; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
}