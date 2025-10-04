package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.decision.DecisionNode;
import com.example.banking.benefit.domain.model.decision.DecisionType;
import com.example.banking.benefit.domain.exception.DecisionEvaluationException;
import com.example.banking.benefit.domain.service.DecisionEvaluationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BaseDecisionEvaluationServiceTest {

    private DecisionEvaluationService decisionEvaluationService;
    private DecisionNode decisionNode;
    private ExecutionContext context;

    @BeforeEach
    void setUp() {
        decisionEvaluationService = new BaseDecisionEvaluationService();
        
        // 建立測試用的決策節點
        decisionNode = new DecisionNode(
            "DECISION_001",
            "測試決策節點",
            "用於單元測試的決策節點",
            1,
            DecisionType.JAVA_CLASS,
            "com.example.TestDecision",
            null
        );
        
        // 建立測試用的執行內容
        CustomerData customerData = new CustomerData("CUST_001", "測試客戶");
        context = new ExecutionContext("CUST_001", customerData);
        context.addVariable("testVar", true);
    }

    @Test
    void shouldEvaluateDecisionNodeSuccessfully() {
        // 準備測試資料
        DecisionNode simpleNode = new DecisionNode(
            "DECISION_002",
            "簡單決策節點",
            "返回固定結果的決策節點",
            1,
            DecisionType.SPEL,
            null,
            "#testVar == true"
        );

        // 執行測試
        boolean result = decisionEvaluationService.evaluate(simpleNode, context);
        assertTrue(result);
    }

    @Test
    void shouldCacheEvaluationResult() {
        // 執行評估
        boolean firstResult = decisionEvaluationService.evaluate(decisionNode, context);
        
        // 取得快取的結果
        boolean cachedResult = decisionEvaluationService.getLastResult(decisionNode, context);
        
        // 驗證結果一致
        assertEquals(firstResult, cachedResult);
    }

    @Test
    void shouldThrowExceptionForInvalidNode() {
        // 準備測試資料：無效的決策節點
        DecisionNode invalidNode = new DecisionNode(
            "DECISION_003",
            "無效決策節點",
            "缺少必要設定的決策節點",
            1,
            null,
            null,
            null
        );

        // 執行測試並驗證異常
        assertThrows(DecisionEvaluationException.class, () -> {
            decisionEvaluationService.evaluate(invalidNode, context);
        });
    }

    @Test
    void shouldHandleComplexSpelExpression() {
        // 準備測試資料：複雜的SpEL表達式
        DecisionNode complexNode = new DecisionNode(
            "DECISION_004",
            "複雜決策節點",
            "使用複雜SpEL表達式的決策節點",
            1,
            DecisionType.SPEL,
            null,
            "context.getCustomerData().getCustomerId() == 'CUST_001' and #testVar == true"
        );

        // 執行測試
        boolean result = decisionEvaluationService.evaluate(complexNode, context);
        assertTrue(result);
    }

    @Test
    void shouldHandleMultipleEvaluations() {
        // 準備測試資料：多個決策節點
        DecisionNode node1 = new DecisionNode(
            "DECISION_005",
            "決策節點1",
            "第一個測試節點",
            1,
            DecisionType.SPEL,
            null,
            "#testVar == true"
        );

        DecisionNode node2 = new DecisionNode(
            "DECISION_006",
            "決策節點2",
            "第二個測試節點",
            2,
            DecisionType.SPEL,
            null,
            "context.getCustomerData().getCustomerId() == 'CUST_001'"
        );

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