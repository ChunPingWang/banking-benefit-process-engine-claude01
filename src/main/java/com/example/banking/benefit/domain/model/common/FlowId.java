package com.example.banking.benefit.domain.model.common;

/**
 * 流程ID值物件
 */
public class FlowId {
    private final String id;
    
    public FlowId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("流程ID不可為空");
        }
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        FlowId flowId = (FlowId) o;
        
        return id.equals(flowId.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}