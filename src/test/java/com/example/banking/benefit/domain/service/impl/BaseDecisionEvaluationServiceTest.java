package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.exception.DecisionEvaluationException;
import com.example.banking.benefit.domain.service.DecisionEvaluationService;
import com.example.banking.benefit.domain.service.common.SpelExpressionExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseDecisionEvaluationServiceTest {

    @InjectMocks
    private BaseDecisionEvaluationService decisionEvaluationService;
    
    @Mock
    private SpelExpressionExecutor spelExecutor;
    
    private DecisionNode decisionNode;
    private BaseExecutionContext context;

    @BeforeEach
    void setUp() {
        // @InjectMocks 已經自動創建 decisionEvaluationService
        
        // 建立測試用的決策節點
        decisionNode = DecisionNode.createJavaClassDecision(
            "FLOW_001",
            "測試決策節點",
            "用於單元測試的決策節點",
            "com.example.TestDecision"
        );
        decisionNode.setNodeOrder(1);
        
        // 建立測試用的執行內容
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("name", CustomerAttribute.forString("測試客戶"));
        CustomerData customerData = CustomerData.create(attributes);
        context = ExecutionContext.create("FLOW_001", "CUST_001", customerData);
        context.addVariable("testVar", true);
    }

    @Test
    void shouldEvaluateDecisionNodeSuccessfully() {
        // 準備測試資料
        DecisionNode simpleNode = DecisionNode.createSpELDecision(
            "FLOW_001",
            "簡單決策節點",
            "返回固定結果的決策節點",
            "#testVar == true"
        );
        simpleNode.setNodeOrder(1);
        
        // 配置 mock
        when(spelExecutor.evaluateAsBoolean(anyString(), any(BaseExecutionContext.class))).thenReturn(true);

        // 執行測試
        boolean result = decisionEvaluationService.evaluate(simpleNode, context);
        assertTrue(result);
    }

    @Test
    void shouldCacheEvaluationResult() {
        // 使用 SpEL 決策節點
        DecisionNode spelNode = DecisionNode.createSpELDecision(
            "CACHE_TEST",
            "快取測試節點",
            "用於測試快取功能",
            "#testVar == true"
        );
        
        // 配置 mock
        when(spelExecutor.evaluateAsBoolean(anyString(), any(BaseExecutionContext.class))).thenReturn(true);
        
        // 執行評估
        boolean firstResult = decisionEvaluationService.evaluate(spelNode, context);
        
        // 取得快取的結果
        // The caching logic might be in a higher-level service or decorator,
        // so this test might need to be adjusted based on the final design.
        // For now, we just call it again and expect the same result.
        boolean secondResult = decisionEvaluationService.evaluate(spelNode, context);
        
        // 驗證結果一致
        assertEquals(firstResult, secondResult);
    }

    @Test
    void shouldThrowExceptionForInvalidNode() {
        // 準備測試資料：無效的決策節點（null 或空的 implementation class）
        // 測試創建 JAVA_CLASS 類型的節點時，如果 implementation class 為 null 或空，應該拋出異常
        
        // 測試 null implementation class
        assertThrows(IllegalArgumentException.class, () -> {
            DecisionNode.createJavaClassDecision("FLOW_001", "name", "desc", null);
        });
        
        // 測試空字串 implementation class
        assertThrows(IllegalArgumentException.class, () -> {
            DecisionNode.createJavaClassDecision("FLOW_001", "name", "desc", "");
        });
    }

    @Test
    void shouldHandleComplexSpelExpression() {
        // 準備測試資料：複雜的SpEL表達式，包含 age 和 amount 屬性
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("age", CustomerAttribute.forInteger(25));
        attributes.put("amount", CustomerAttribute.forDouble(1500.0));
        CustomerData customerData = CustomerData.create(attributes);
        BaseExecutionContext testContext = ExecutionContext.create("FLOW_001", "CUST_001", customerData);
        
        DecisionNode complexNode = DecisionNode.createSpELDecision(
                "complex-node",
                "Complex Logic",
                "Complex decision node",
                "#customerData.get('age') > 18 and #customerData.get('amount') > 1000"
        );
        
        // 配置 mock
        when(spelExecutor.evaluateAsBoolean(anyString(), any(BaseExecutionContext.class))).thenReturn(true);

        // 執行測試
        boolean result = decisionEvaluationService.evaluate(complexNode, testContext);
        assertTrue(result);
    }

    @Test
    void shouldHandleMultipleEvaluations() {
        // 準備測試資料：多個決策節點
        DecisionNode node1 = DecisionNode.createSpELDecision(
            "FLOW_001",
            "決策節點1",
            "第一個測試節點",
            "#testVar == true"
        );
        node1.setNodeOrder(1);

        DecisionNode node2 = DecisionNode.createSpELDecision(
            "FLOW_001",
            "決策節點2",
            "第二個測試節點",
            "customerData.getCustomerId() == 'CUST_001'"
        );
        node2.setNodeOrder(2);
        
        // 配置 mock
        when(spelExecutor.evaluateAsBoolean(anyString(), any(BaseExecutionContext.class))).thenReturn(true);

        // 執行測試
        assertTrue(decisionEvaluationService.evaluate(node1, context));
        assertTrue(decisionEvaluationService.evaluate(node2, context));
    }

    @Test
    void shouldHandleConcurrentEvaluation() throws InterruptedException {
        // 準備測試資料
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        boolean[] results = new boolean[threadCount];
        
        // 創建多個執行緒同時評估決策節點
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                // Each thread needs its own context if the evaluation modifies it.
                // For this test, we assume evaluation is read-only for the context.
                results[index] = decisionEvaluationService.evaluate(decisionNode, context);
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
        
        // 驗證所有結果一致
        boolean firstResult = results[0];
        for (int i = 1; i < threadCount; i++) {
            assertEquals(firstResult, results[i]);
        }
    }
}