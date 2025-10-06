package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.process.ProcessNode;
import com.example.banking.benefit.domain.exception.ProcessExecutionException;
import com.example.banking.benefit.domain.service.ProcessExecutionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BaseProcessExecutionServiceTest {

    private ProcessExecutionService processExecutionService;
    private ProcessNode processNode;
    private BaseExecutionContext context;

    @BeforeEach
    void setUp() {
        processExecutionService = new BaseProcessExecutionService();
        
        // 建立測試用的處理節點
        processNode = ProcessNode.createJavaClassProcess(
            "FLOW_001",
            "測試處理節點",
            "用於單元測試的處理節點",
            "com.example.TestProcessor"
        );
        processNode.setNodeOrder(1);
        
        // 建立測試用的執行內容
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("name", CustomerAttribute.forString("測試客戶"));
        CustomerData customerData = CustomerData.create("CUST_001", attributes);
        context = ExecutionContext.create("FLOW_001", "CUST_001", customerData);
    }

    @Test
    void shouldExecuteProcessNodeSuccessfully() {
        // 準備測試資料
        processNode = ProcessNode.createJavaClassProcess(
            "FLOW_001",
            "Java實作處理節點",
            "測試Java類別實作",
            "com.example.TestProcessor"
        );
        processNode.setNodeOrder(1);

        // 執行測試
        assertDoesNotThrow(() -> {
            processExecutionService.execute(processNode, context);
        });
    }

    @Test
    void shouldThrowExceptionWhenProcessNodeIsNull() {
        // 執行測試並驗證異常
        ProcessExecutionException exception = assertThrows(
            ProcessExecutionException.class,
            () -> processExecutionService.execute(null, context)
        );

        assertEquals("無法執行處理節點: null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenContextIsNull() {
        // 執行測試並驗證異常
        ProcessExecutionException exception = assertThrows(
            ProcessExecutionException.class,
            () -> processExecutionService.execute(processNode, null)
        );

        assertEquals("無法執行處理節點: " + processNode.getNodeId(), exception.getMessage());
    }

    @Test
    void shouldValidateProcessNodeBeforeExecution() {
        // 準備測試資料：無效的處理節點
        // The validation logic is now part of the factory or the node itself.
        // This test should check for illegal arguments during creation.
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessNode.createJavaClassProcess("FLOW_001", "無效處理節點", "desc", "");
        });
    }

    @Test
    void shouldRollbackProcessNodeExecution() {
        // 執行測試
        assertDoesNotThrow(() -> {
            processExecutionService.rollback(processNode, context);
        });
    }

    @Test
    void shouldHandleSpelExpressionExecution() {
        // 準備測試資料：SpEL表達式處理節點
        ProcessNode spelNode = ProcessNode.createSpELProcess(
            "FLOW_001",
            "SpEL處理節點",
            "測試SpEL表達式執行",
            "context.addVariable('result', true)"
        );
        spelNode.setNodeOrder(1);

        // 執行測試
        assertDoesNotThrow(() -> {
            processExecutionService.execute(spelNode, context);
        });
    }

    @Test
    void shouldHandleConcurrentExecution() throws InterruptedException {
        // 準備測試資料
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        
        // 創建多個執行緒同時執行處理節點
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                assertDoesNotThrow(() -> {
                    processExecutionService.execute(processNode, context);
                });
            });
        }
        
        // 啟動所有執行緒
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有執行緒完成
        for (Thread thread : threads) {
            thread.join();
        }
    }
}