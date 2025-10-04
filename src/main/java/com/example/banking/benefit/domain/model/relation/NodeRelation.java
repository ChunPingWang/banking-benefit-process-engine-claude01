package com.example.banking.benefit.domain.model.relation;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 節點關聯實體
 * 定義節點之間的連接關係和轉換條件
 */
public class NodeRelation {
    private String relationId;
    private String flowId;
    private String sourceNodeId;
    private NodeType sourceNodeType;
    private String targetNodeId;
    private NodeType targetNodeType;
    private RelationType relationType;
    private LogicOperator logicOperator;
    private String conditionExpression;
    private LocalDateTime createdTime;

    private NodeRelation(
            String flowId,
            String sourceNodeId,
            NodeType sourceNodeType,
            String targetNodeId,
            NodeType targetNodeType,
            RelationType relationType
    ) {
        this.relationId = UUID.randomUUID().toString();
        this.flowId = flowId;
        this.sourceNodeId = sourceNodeId;
        this.sourceNodeType = sourceNodeType;
        this.targetNodeId = targetNodeId;
        this.targetNodeType = targetNodeType;
        this.relationType = relationType;
        this.createdTime = LocalDateTime.now();
    }

    public static NodeRelation create(
            String flowId,
            String sourceNodeId,
            NodeType sourceNodeType,
            String targetNodeId,
            NodeType targetNodeType,
            RelationType relationType
    ) {
        if (flowId == null || flowId.trim().isEmpty()) {
            throw new IllegalArgumentException("flowId must not be null or empty");
        }
        if (sourceNodeId == null || sourceNodeId.trim().isEmpty()) {
            throw new IllegalArgumentException("sourceNodeId must not be null or empty");
        }
        if (sourceNodeType == null) {
            throw new IllegalArgumentException("sourceNodeType must not be null");
        }
        if (targetNodeId == null || targetNodeId.trim().isEmpty()) {
            throw new IllegalArgumentException("targetNodeId must not be null or empty");
        }
        if (targetNodeType == null) {
            throw new IllegalArgumentException("targetNodeType must not be null");
        }
        if (relationType == null) {
            throw new IllegalArgumentException("relationType must not be null");
        }
        return new NodeRelation(flowId, sourceNodeId, sourceNodeType, targetNodeId, targetNodeType, relationType);
    }

    public void setLogicOperator(LogicOperator logicOperator) {
        this.logicOperator = logicOperator;
    }

    public void setConditionExpression(String conditionExpression) {
        if (conditionExpression == null || conditionExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("conditionExpression must not be null or empty");
        }
        this.conditionExpression = conditionExpression;
    }

    // Getters
    public String getRelationId() { return relationId; }
    public String getFlowId() { return flowId; }
    public String getSourceNodeId() { return sourceNodeId; }
    public NodeType getSourceNodeType() { return sourceNodeType; }
    public String getTargetNodeId() { return targetNodeId; }
    public NodeType getTargetNodeType() { return targetNodeType; }
    public RelationType getRelationType() { return relationType; }
    public LogicOperator getLogicOperator() { return logicOperator; }
    public String getConditionExpression() { return conditionExpression; }
    public LocalDateTime getCreatedTime() { return createdTime; }
}