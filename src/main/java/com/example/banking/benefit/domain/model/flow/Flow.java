package com.example.banking.benefit.domain.model.flow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.node.FlowStructure;
import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.process.ProcessNode;
import com.example.banking.benefit.domain.model.relation.NodeRelation;

/**
 * Flow 代表一個完整的流程定義，是整個系統的聚合根
 * 包含流程的基本資訊、版本控制和狀態管理
 */
public class Flow {
    private List<DecisionNode> decisionNodes = new ArrayList<>();
    private List<ProcessNode> processNodes = new ArrayList<>();
    private List<NodeRelation> relations = new ArrayList<>();
    private FlowStructure flowStructure;
    private FlowId flowId;
    private String flowName;
    private String description;
    private Version version;
    private FlowStatus status;
    private String startNodeId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String createdBy;

    private Flow(FlowId flowId, String flowName, String description, Version version) {
        this.flowId = flowId;
        this.flowName = flowName;
        this.description = description;
        this.version = version;
        this.status = FlowStatus.DRAFT;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    public static Flow create(FlowId flowId, String flowName, String description, Version version) {
        if (flowId == null) {
            throw new IllegalArgumentException("flowId must not be null");
        }
        if (flowName == null || flowName.trim().isEmpty()) {
            throw new IllegalArgumentException("flowName must not be null or empty");
        }
        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }
        return new Flow(flowId, flowName, description, version);
    }

    public void activate() {
        if (this.startNodeId == null) {
            throw new IllegalStateException("Cannot activate flow without start node");
        }
        this.status = FlowStatus.ACTIVE;
        this.updatedTime = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = FlowStatus.INACTIVE;
        this.updatedTime = LocalDateTime.now();
    }

    public void setStartNode(String nodeId) {
        if (nodeId == null || nodeId.trim().isEmpty()) {
            throw new IllegalArgumentException("nodeId must not be null or empty");
        }
        this.startNodeId = nodeId;
        this.updatedTime = LocalDateTime.now();
    }

    // Getters
    public FlowId getFlowId() { return flowId; }
    public String getFlowName() { return flowName; }
    public String getDescription() { return description; }
    public Version getVersion() { return version; }
    public FlowStatus getStatus() { return status; }
    public String getStartNodeId() { return startNodeId; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public String getCreatedBy() { return createdBy; }
    
    // Decision nodes management
    public List<DecisionNode> getDecisionNodes() {
        return new ArrayList<>(decisionNodes);
    }
    
    public void addDecisionNode(DecisionNode node) {
        if (node == null) {
            throw new IllegalArgumentException("node must not be null");
        }
        this.decisionNodes.add(node);
        this.updatedTime = LocalDateTime.now();
    }
    
    public void removeDecisionNode(DecisionNode node) {
        this.decisionNodes.remove(node);
        this.updatedTime = LocalDateTime.now();
    }
    
    // Process nodes management
    public List<ProcessNode> getProcessNodes() {
        return new ArrayList<>(processNodes);
    }
    
    public void addProcessNode(ProcessNode node) {
        if (node == null) {
            throw new IllegalArgumentException("node must not be null");
        }
        this.processNodes.add(node);
        this.updatedTime = LocalDateTime.now();
    }
    
    public void removeProcessNode(ProcessNode node) {
        this.processNodes.remove(node);
        this.updatedTime = LocalDateTime.now();
    }

    public void addRelation(NodeRelation relation) {
        if (relation == null) {
            throw new IllegalArgumentException("relation must not be null");
        }
        this.relations.add(relation);
        this.updatedTime = LocalDateTime.now();
    }

    public void buildStructure() {
        this.flowStructure = new FlowStructure(decisionNodes, processNodes, relations);
    }
    
    // Get start node
    public Optional<Node> getStartNode() {
        if (startNodeId == null) {
            return Optional.empty();
        }
        return flowStructure.findNodeById(startNodeId);
    }
    
    public boolean isValid() {
        return startNodeId != null && 
               (decisionNodes.size() > 0 || processNodes.size() > 0);
    }
    
    public Optional<Node> getNextNode(String currentNodeId, boolean condition) {
        return flowStructure.findNextNode(currentNodeId, condition);
    }
    
    /**
     * 根據節點ID查找節點
     *
     * @param nodeId 節點ID
     * @return 節點物件，如果找不到則返回null
     */
    public Optional<Node> findNodeById(String nodeId) {
        return flowStructure.findNodeById(nodeId);
    }
}