package com.example.banking.benefit.domain.model.node;

import com.example.banking.benefit.domain.model.common.ExecutionContext;

public class ProcessNode implements Node {
    
    private String nodeId;
    private String nodeName;
    private String description;
    private Integer nodeOrder;
    private String spelExpression;
    private String implementationClass;
    private String stateName;
    
    private ProcessNode(String nodeId, String nodeName, String description,
                       String implementationClass, String spelExpression) {
        if (nodeId == null || nodeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Node ID cannot be null or empty.");
        }
        if (nodeName == null || nodeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Node name cannot be null or empty.");
        }
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.description = description;
        this.implementationClass = implementationClass;
        this.spelExpression = spelExpression;
    }
    
    public static ProcessNode createJavaClassProcess(String nodeId, String nodeName, 
                                                   String description, String implementationClass) {
        if (implementationClass == null || implementationClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Implementation class cannot be null or empty for Java class process.");
        }
        return new ProcessNode(nodeId, nodeName, description, implementationClass, null);
    }
    
    public static ProcessNode createSpELProcess(String nodeId, String nodeName, 
                                              String description, String spelExpression) {
        if (spelExpression == null || spelExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("SpEL expression cannot be null or empty for SpEL process.");
        }
        return new ProcessNode(nodeId, nodeName, description, null, spelExpression);
    }
    
    @Override
    public String getNodeId() {
        return nodeId;
    }
    
    @Override
    public String getNodeName() {
        return nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public Integer getNodeOrder() {
        return nodeOrder;
    }
    
    public void setNodeOrder(Integer nodeOrder) {
        this.nodeOrder = nodeOrder;
    }
    
    @Override
    public NodeType getNodeType() {
        return NodeType.PROCESS;
    }
    
    @Override
    public boolean canExecute(ExecutionContext context) {
        return context != null;
    }
    
    @Override
    public void validate() {
        if (spelExpression == null && implementationClass == null) {
            throw new IllegalStateException("Either SpEL expression or implementation class must be specified.");
        }
        if (spelExpression != null && implementationClass != null) {
            throw new IllegalStateException("Cannot specify both SpEL expression and implementation class.");
        }
    }
    
    public boolean isSpelExpression() {
        return spelExpression != null && !spelExpression.isEmpty();
    }
    
    public String getSpelExpression() {
        return spelExpression;
    }
    
    public void setSpelExpression(String spelExpression) {
        this.spelExpression = spelExpression;
    }
    
    public String getImplementationClass() {
        return implementationClass;
    }
    
    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }
    
    public String getStateName() {
        return stateName;
    }
    
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}