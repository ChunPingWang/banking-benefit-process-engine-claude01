package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.process.ProcessNode;
import com.example.banking.benefit.domain.service.ProcessExecutionService;
import com.example.banking.benefit.domain.exception.ProcessExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 處理節點執行服務的基礎實作
 */
@Service
public class BaseProcessExecutionService implements ProcessExecutionService {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseProcessExecutionService.class);

    @Override
    public void execute(ProcessNode node, BaseExecutionContext context) {
        if (!canExecute(node, context)) {
            throw new ProcessExecutionException("無法執行處理節點: " + node.getNodeId());
        }

        try {
            logger.info("開始執行處理節點: {}", node.getNodeId());
            
            // 根據處理類型執行不同邏輯
            switch (node.getProcessType()) {
                case JAVA_CLASS:
                    executeJavaImplementation(node, context);
                    break;
                case SPEL:
                    executeSpelExpression(node, context);
                    break;
                default:
                    throw new ProcessExecutionException("不支援的處理類型: " + node.getProcessType());
            }

            logger.info("處理節點執行完成: {}", node.getNodeId());
        } catch (Exception e) {
            logger.error("處理節點執行失敗: {}", node.getNodeId(), e);
            throw new ProcessExecutionException("處理節點執行失敗", e);
        }
    }

    @Override
    public boolean canExecute(ProcessNode node, BaseExecutionContext context) {
        // 檢查節點是否為空
        if (node == null) {
            return false;
        }

        // 檢查執行內容是否為空
        if (context == null) {
            return false;
        }

        // 檢查處理類型是否支援
        if (node.getProcessType() == null) {
            return false;
        }

        // 檢查實作類別或表達式是否存在
        switch (node.getProcessType()) {
            case JAVA_CLASS:
                return node.getImplementationClass() != null;
            case SPEL:
                return node.getSpelExpression() != null;
            default:
                return false;
        }
    }

    @Override
    public void rollback(ProcessNode node, BaseExecutionContext context) {
        try {
            logger.info("開始回滾處理節點: {}", node.getNodeId());
            // 執行回滾邏輯
            // TODO: 實作回滾機制
            logger.info("處理節點回滾完成: {}", node.getNodeId());
        } catch (Exception e) {
            logger.error("處理節點回滾失敗: {}", node.getNodeId(), e);
            throw new ProcessExecutionException("處理節點回滾失敗", e);
        }
    }

    /**
     * 執行 Java 類別實作
     */
    private void executeJavaImplementation(ProcessNode node, BaseExecutionContext context) {
        // TODO: 使用反射機制執行實作類別
    }

    /**
     * 執行 SpEL 表達式
     */
    private void executeSpelExpression(ProcessNode node, BaseExecutionContext context) {
        // TODO: 使用 SpEL 表達式執行器
    }
}