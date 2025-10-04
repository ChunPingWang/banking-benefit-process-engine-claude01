package com.example.banking.benefit.domain.model.common;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 客戶屬性值物件
 * @param <T>
 */
public class CustomerAttribute<T> {
    private final T value;
    private final Class<T> type;

    private CustomerAttribute(T value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    public static <T> CustomerAttribute<T> of(T value, Class<T> type) {
        Objects.requireNonNull(value, "Attribute value cannot be null");
        Objects.requireNonNull(type, "Attribute type cannot be null");
        return new CustomerAttribute<>(value, type);
    }

    public T getValue() {
        return value;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAttribute<?> that = (CustomerAttribute<?>) o;
        return Objects.equals(value, that.value) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    @Override
    public String toString() {
        return "CustomerAttribute{" +
                "value=" + value +
                ", type=" + type.getSimpleName() +
                '}';
    }

    // Convenience factory methods
    public static CustomerAttribute<String> forString(String value) {
        return of(value, String.class);
    }

    public static CustomerAttribute<Integer> forInteger(Integer value) {
        return of(value, Integer.class);
    }

    public static CustomerAttribute<Long> forLong(Long value) {
        return of(value, Long.class);
    }

    public static CustomerAttribute<Double> forDouble(Double value) {
        return of(value, Double.class);
    }

    public static CustomerAttribute<Boolean> forBoolean(Boolean value) {
        return of(value, Boolean.class);
    }

    public static CustomerAttribute<LocalDate> forDate(LocalDate value) {
        return of(value, LocalDate.class);
    }
}
