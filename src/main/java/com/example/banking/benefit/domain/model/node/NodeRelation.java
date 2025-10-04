package com.example.banking.benefit.domain.model.node;

import com.example.banking.benefit.domain.model.flow.FlowId;
import java.time.LocalDateTime;

/**
 * 節點關係實體類別
 */
public class NodeRelation {
    private String relationId;
    private FlowId flowId;
    private String sourceNodeId;
    private String sourceNodeType;
    private String targetNodeId;
    private String targetNodeType;
    private String relationType;
    private String logicOperator;
    private String conditionExpression;
    private LocalDateTime createdTime;

    public NodeRelation() {
        this.createdTime = LocalDateTime.now();
    }

    public NodeRelation(String relationId, FlowId flowId, String sourceNodeId, String sourceNodeType,
                       String targetNodeId, String targetNodeType, String relationType) {
        this();
        this.relationId = relationId;
        this.flowId = flowId;
        this.sourceNodeId = sourceNodeId;
        this.sourceNodeType = sourceNodeType;
        this.targetNodeId = targetNodeId;
        this.targetNodeType = targetNodeType;
        this.relationType = relationType;
    }

    // Getters
    public String getRelationId() {
        return relationId;
    }

    public FlowId getFlowId() {
        return flowId;
    }

    public String getSourceNodeId() {
        return sourceNodeId;
    }

    public String getSourceNodeType() {
        return sourceNodeType;
    }

    public String getTargetNodeId() {
        return targetNodeId;
    }

    public String getTargetNodeType() {
        return targetNodeType;
    }

    public String getRelationType() {
        return relationType;
    }

    public String getLogicOperator() {
        return logicOperator;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    // Setters
    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeRelation that = (NodeRelation) o;
        return relationId.equals(that.relationId);
    }

    @Override
    public int hashCode() {
        return relationId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("NodeRelation{relationId='%s', flowId='%s', sourceNodeId='%s', targetNodeId='%s'}",
                relationId, flowId, sourceNodeId, targetNodeId);
    }
}