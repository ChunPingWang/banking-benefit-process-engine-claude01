package com.example.banking.benefit.domain.model.flow;

import java.util.UUID;

/**
 * Flow 的識別碼值物件
 */
public class FlowId {
    private final String value;

    private FlowId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("FlowId value must not be null or empty");
        }
        this.value = value;
    }

    public static FlowId generate() {
        return new FlowId(UUID.randomUUID().toString());
    }

    public static FlowId of(String value) {
        return new FlowId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowId flowId = (FlowId) o;
        return value.equals(flowId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}