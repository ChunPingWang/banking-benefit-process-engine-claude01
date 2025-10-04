package com.example.banking.benefit.domain.model.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 客戶資料值物件
 * 包含客戶相關的所有資訊
 */
public class CustomerData {
    private final Map<String, CustomerAttribute<?>> attributes;

    private CustomerData(Map<String, CustomerAttribute<?>> attributes) {
        this.attributes = new HashMap<>(attributes);
    }

    public static CustomerData create(Map<String, CustomerAttribute<?>> attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("attributes must not be null");
        }
        return new CustomerData(attributes);
    }

    public Optional<CustomerAttribute<?>> getAttribute(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    /**
     * 獲取屬性的原始值，用於 SpEL 表達式訪問
     * 支援 #customerData.get('key') 或 #customerData['key'] 語法
     */
    public Object get(String key) {
        return getAttribute(key)
                .map(CustomerAttribute::getValue)
                .orElse(null);
    }

    public <T> Optional<T> getValue(String key, Class<T> type) {
        return getAttribute(key)
                .filter(attr -> type.isAssignableFrom(attr.getType()))
                .map(attr -> type.cast(attr.getValue()));
    }

    public boolean hasKey(String key) {
        return attributes.containsKey(key);
    }

    public Map<String, CustomerAttribute<?>> getAllAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}