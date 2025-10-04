package com.example.banking.benefit.domain.model.relation;

/**
 * 關聯類型列舉
 */
public enum RelationType {
    PASS("通過"),
    FAIL("失敗"),
    NEXT("下一步");

    private final String description;

    RelationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}