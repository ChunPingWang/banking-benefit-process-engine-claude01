package com.example.banking.benefit.domain.service.expression;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java 類別執行器
 */
public class JavaClassEvaluator implements ExpressionEvaluator {
    
    private final ConcurrentHashMap<String, Class<?>> classCache;
    private final ConcurrentHashMap<String, Method> methodCache;
    
    public JavaClassEvaluator() {
        this.classCache = new ConcurrentHashMap<>();
        this.methodCache = new ConcurrentHashMap<>();
    }
    
    @Override
    public boolean evaluateCondition(String className, BaseExecutionContext context, Map<String, Object> variables) {
        try {
            Class<?> clazz = loadClass(className);
            Method method = findEvaluateMethod(clazz);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            return (boolean) method.invoke(instance, context, variables);
        } catch (Exception e) {
            throw new ExpressionEvaluationException("Failed to evaluate condition using class: " + className, e);
        }
    }
    
    @Override
    public <T> T evaluateExpression(String className, BaseExecutionContext context, Map<String, Object> variables, Class<T> expectedType) {
        try {
            Class<?> clazz = loadClass(className);
            Method method = findProcessMethod(clazz);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Object result = method.invoke(instance, context, variables);
            return expectedType.cast(result);
        } catch (Exception e) {
            throw new ExpressionEvaluationException("Failed to evaluate expression using class: " + className, e);
        }
    }
    
    @Override
    public boolean validateExpression(String className) {
        try {
            Class<?> clazz = loadClass(className);
            // 驗證必要的方法存在
            boolean hasEvaluate = findEvaluateMethod(clazz) != null;
            boolean hasProcess = findProcessMethod(clazz) != null;
            return hasEvaluate || hasProcess;
        } catch (Exception e) {
            return false;
        }
    }
    
    private Class<?> loadClass(String className) throws ClassNotFoundException {
        return classCache.computeIfAbsent(className, key -> {
            try {
                return Class.forName(key);
            } catch (ClassNotFoundException e) {
                throw new ExpressionEvaluationException("Class not found: " + key, e);
            }
        });
    }
    
    private Method findEvaluateMethod(Class<?> clazz) {
        String cacheKey = clazz.getName() + "#evaluate";
        return methodCache.computeIfAbsent(cacheKey, key -> {
            try {
                return clazz.getMethod("evaluate", BaseExecutionContext.class, Map.class);
            } catch (NoSuchMethodException e) {
                return null;
            }
        });
    }
    
    private Method findProcessMethod(Class<?> clazz) {
        String cacheKey = clazz.getName() + "#process";
        return methodCache.computeIfAbsent(cacheKey, key -> {
            try {
                return clazz.getMethod("process", BaseExecutionContext.class, Map.class);
            } catch (NoSuchMethodException e) {
                return null;
            }
        });
    }
}