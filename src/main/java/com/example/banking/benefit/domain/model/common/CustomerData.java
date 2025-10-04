package com.example.banking.benefit.domain.model.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 客戶資料值物件
 * 包含客戶相關的所有資訊
 */
public class CustomerData {
    private final Map<String, Object> data;

    private CustomerData(Map<String, Object> data) {
        this.data = new HashMap<>(data);
    }

    public static CustomerData create(Map<String, Object> data) {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        return new CustomerData(data);
    }

    public Object getValue(String key) {
        return data.get(key);
    }

    public boolean hasKey(String key) {
        return data.containsKey(key);
    }

    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(data);
    }
}