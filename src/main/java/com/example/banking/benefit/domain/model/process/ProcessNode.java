package com.example.banking.benefit.domain.model.process;

import com.example.banking.benefit.domain.model.node.ProcessType;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 處理節點實體
 * 負責執行具體的業務邏輯
 */
public class ProcessNode extends BaseProcessNode {
    private String flowId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    private ProcessNode(String flowId, String nodeName, String nodeDescription, ProcessType processType) {
        this.id = UUID.randomUUID().toString();
        this.flowId = flowId;
        this.name = nodeName;
        this.description = nodeDescription;
        this.processType = processType;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    public static ProcessNode createJavaClassProcess(
            String flowId,
            String nodeName,
            String nodeDescription,
            String implementationClassName
    ) {
        ProcessNode node = new ProcessNode(flowId, nodeName, nodeDescription, ProcessType.JAVA_CLASS);
        node.setImplementationClass(implementationClassName);
        return node;
    }

    public static ProcessNode createSpELProcess(
            String flowId,
            String nodeName,
            String nodeDescription,
            String spelExpression
    ) {
        ProcessNode node = new ProcessNode(flowId, nodeName, nodeDescription, ProcessType.SPEL);
        node.setSpelExpression(spelExpression);
        return node;
    }

    public void setImplementationClass(String implementationClassName) {
        if (this.processType != ProcessType.JAVA_CLASS) {
            throw new IllegalStateException("Cannot set implementation class for non-Java class process");
        }
        if (implementationClassName == null || implementationClassName.trim().isEmpty()) {
            throw new IllegalArgumentException("Implementation class must not be null or empty");
        }
        try {
            this.implementationClass = Class.forName(implementationClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot find implementation class: " + implementationClassName, e);
        }
        this.updatedTime = LocalDateTime.now();
    }

    public void setSpelExpression(String spelExpression) {
        if (this.processType != ProcessType.SPEL) {
            throw new IllegalStateException("Cannot set SpEL expression for non-SpEL process");
        }
        if (spelExpression == null || spelExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("SpEL expression must not be null or empty");
        }
        this.spelExpression = spelExpression;
        this.updatedTime = LocalDateTime.now();
    }

    public void setStateName(String stateName) {
        this.state = stateName;
        this.updatedTime = LocalDateTime.now();
    }

    public void setNodeOrder(Integer nodeOrder) {
        this.nodeOrder = nodeOrder;
        this.updatedTime = LocalDateTime.now();
    }

    // Getters
    @Override
    public String getNodeId() { return id; }
    public String getFlowId() { return flowId; }
    @Override
    public String getNodeName() { return name; }
    @Override
    public String getNodeDescription() { return description; }
    @Override
    public ProcessType getProcessType() { return processType; }
    public String getImplementationClassName() { 
        return implementationClass != null ? implementationClass.getName() : null; 
    }
    public String getSpelExpression() { return spelExpression; }
    public String getStateName() { return state; }
    @Override
    public Integer getNodeOrder() { return nodeOrder; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
}