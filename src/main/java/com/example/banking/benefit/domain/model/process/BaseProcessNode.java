package com.example.banking.benefit.domain.model.process;

import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.node.ProcessType;

/**
 * 處理節點基礎類別
 * 實作 Node 介面，提供處理節點的基本功能
 */
public abstract class BaseProcessNode implements Node {
    
    protected String id;
    protected String name;
    protected String description;
    protected ProcessType processType;
    protected String implementationClass;
    protected String spelExpression;
    protected String state;
    protected Integer nodeOrder;

    // 預設建構子
    public BaseProcessNode() {}

    // 建構子
    public BaseProcessNode(String id, String name, String description, ProcessType processType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.processType = processType;
    }

    @Override
    public String getNodeId() {
        return id;
    }

    @Override
    public String getNodeName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getNodeOrder() {
        return nodeOrder;
    }

    // Getter 方法
    public ProcessType getProcessType() {
        return processType;
    }

    public String getImplementationClass() {
        return implementationClass;
    }

    public String getSpelExpression() {
        return spelExpression;
    }

    public String getState() {
        return state;
    }

    // Setter 方法
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }

    public void setSpelExpression(String spelExpression) {
        this.spelExpression = spelExpression;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setNodeOrder(Integer nodeOrder) {
        this.nodeOrder = nodeOrder;
    }
}