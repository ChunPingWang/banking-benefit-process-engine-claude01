package com.example.banking.benefit.domain.model.group;

import com.example.banking.benefit.domain.model.relation.LogicOperator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 決策群組實體
 * 將多個決策節點組織在一起，並定義其邏輯關係
 */
public class DecisionGroup {
    private String groupId;
    private String flowId;
    private String groupName;
    private LogicOperator logicOperator;
    private String targetProcessId;
    private String parentGroupId;
    private LocalDateTime createdTime;
    private List<GroupMember> members;

    private DecisionGroup(String flowId, String groupName, LogicOperator logicOperator) {
        this.groupId = UUID.randomUUID().toString();
        this.flowId = flowId;
        this.groupName = groupName;
        this.logicOperator = logicOperator;
        this.createdTime = LocalDateTime.now();
        this.members = new ArrayList<>();
    }

    public static DecisionGroup create(String flowId, String groupName, LogicOperator logicOperator) {
        if (flowId == null || flowId.trim().isEmpty()) {
            throw new IllegalArgumentException("flowId must not be null or empty");
        }
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("groupName must not be null or empty");
        }
        if (logicOperator == null) {
            throw new IllegalArgumentException("logicOperator must not be null");
        }
        return new DecisionGroup(flowId, groupName, logicOperator);
    }

    public void setTargetProcess(String targetProcessId) {
        if (targetProcessId == null || targetProcessId.trim().isEmpty()) {
            throw new IllegalArgumentException("targetProcessId must not be null or empty");
        }
        this.targetProcessId = targetProcessId;
    }

    public void setParentGroup(String parentGroupId) {
        if (parentGroupId == null || parentGroupId.trim().isEmpty()) {
            throw new IllegalArgumentException("parentGroupId must not be null or empty");
        }
        this.parentGroupId = parentGroupId;
    }

    public void addMember(String decisionNodeId, int order) {
        if (decisionNodeId == null || decisionNodeId.trim().isEmpty()) {
            throw new IllegalArgumentException("decisionNodeId must not be null or empty");
        }
        
        GroupMember member = new GroupMember(UUID.randomUUID().toString(), groupId, decisionNodeId, order);
        members.add(member);
        
        // 根據順序重新排序成員
        Collections.sort(members);
    }

    public void removeMember(String decisionNodeId) {
        members.removeIf(member -> member.getDecisionNodeId().equals(decisionNodeId));
    }

    public List<GroupMember> getMembers() {
        return Collections.unmodifiableList(members);
    }

    // Getters
    public String getGroupId() { return groupId; }
    public String getFlowId() { return flowId; }
    public String getGroupName() { return groupName; }
    public LogicOperator getLogicOperator() { return logicOperator; }
    public String getTargetProcessId() { return targetProcessId; }
    public String getParentGroupId() { return parentGroupId; }
    public LocalDateTime getCreatedTime() { return createdTime; }
}