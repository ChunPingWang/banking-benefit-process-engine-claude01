package com.example.banking.benefit.domain.model.flow;

/**
 * Flow 的版本值物件
 */
public class Version {
    private final String value;

    private Version(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Version value must not be null or empty");
        }
        if (!value.matches("^\\d+\\.\\d+\\.\\d+$")) {
            throw new IllegalArgumentException("Version must be in format: MAJOR.MINOR.PATCH");
        }
        this.value = value;
    }

    public static Version of(String value) {
        return new Version(value);
    }

    public static Version initial() {
        return new Version("0.1.0");
    }

    public Version incrementMajor() {
        String[] parts = value.split("\\.");
        return new Version((Integer.parseInt(parts[0]) + 1) + ".0.0");
    }

    public Version incrementMinor() {
        String[] parts = value.split("\\.");
        return new Version(parts[0] + "." + (Integer.parseInt(parts[1]) + 1) + ".0");
    }

    public Version incrementPatch() {
        String[] parts = value.split("\\.");
        return new Version(parts[0] + "." + parts[1] + "." + (Integer.parseInt(parts[2]) + 1));
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return value.equals(version.value);
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