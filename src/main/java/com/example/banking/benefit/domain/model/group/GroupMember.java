package com.example.banking.benefit.domain.model.group;

import java.time.LocalDateTime;

/**
 * 群組成員值物件
 */
public class GroupMember implements Comparable<GroupMember> {
    private final String memberId;
    private final String groupId;
    private final String decisionNodeId;
    private final int memberOrder;
    private final LocalDateTime createdTime;

    public GroupMember(String memberId, String groupId, String decisionNodeId, int memberOrder) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("memberId must not be null or empty");
        }
        if (groupId == null || groupId.trim().isEmpty()) {
            throw new IllegalArgumentException("groupId must not be null or empty");
        }
        if (decisionNodeId == null || decisionNodeId.trim().isEmpty()) {
            throw new IllegalArgumentException("decisionNodeId must not be null or empty");
        }

        this.memberId = memberId;
        this.groupId = groupId;
        this.decisionNodeId = decisionNodeId;
        this.memberOrder = memberOrder;
        this.createdTime = LocalDateTime.now();
    }

    @Override
    public int compareTo(GroupMember other) {
        return Integer.compare(this.memberOrder, other.memberOrder);
    }

    // Getters
    public String getMemberId() { return memberId; }
    public String getGroupId() { return groupId; }
    public String getDecisionNodeId() { return decisionNodeId; }
    public int getMemberOrder() { return memberOrder; }
    public LocalDateTime getCreatedTime() { return createdTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMember that = (GroupMember) o;
        return memberId.equals(that.memberId);
    }

    @Override
    public int hashCode() {
        return memberId.hashCode();
    }
}