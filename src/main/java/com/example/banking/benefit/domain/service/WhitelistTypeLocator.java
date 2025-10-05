package com.example.banking.benefit.domain.service;

import org.springframework.expression.TypeLocator;
import org.springframework.expression.spel.support.StandardTypeLocator;

import java.util.List;

public class WhitelistTypeLocator implements TypeLocator {
    private final List<String> whitelistedTypes;
    private final StandardTypeLocator delegate;

    public WhitelistTypeLocator(List<String> whitelistedTypes) {
        this.whitelistedTypes = whitelistedTypes;
        this.delegate = new StandardTypeLocator();
    }

    @Override
    public Class<?> findType(String typeName) {
        if (!whitelistedTypes.contains(typeName)) {
            throw new IllegalArgumentException("Type not in whitelist: " + typeName);
        }
        try {
            return Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Failed to load type: " + typeName, e);
        }
    }
}