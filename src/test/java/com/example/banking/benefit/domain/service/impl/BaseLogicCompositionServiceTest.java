package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.decision.DecisionNode;
import com.example.banking.benefit.domain.model.decision.DecisionType;
import com.example.banking.benefit.domain.exception.LogicCompositionException;
import com.example.banking.benefit.domain.service.LogicCompositionService;
import com.example.banking.benefit.domain.service.DecisionEvaluationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseLogicCompositionServiceTest {

    @Mock
    private DecisionEvaluationService decisionEvaluationService;

    private LogicCompositionService logicCompositionService;
    private ExecutionContext context;

    @BeforeEach
    void setUp() {
        logicCompositionService = new BaseLogicCompositionService(decisionEvaluationService);
        
        // 建立測試用的執行內容
        CustomerData customerData = new CustomerData("CUST_001", "測試客戶");
        context = new ExecutionContext("CUST_001", customerData);
        context.addVariable("amount", 1000);
    }

    @Test
    void shouldComposeDecisionsWithAndOperator() {
        // 準備測試資料
        DecisionNode decision1 = new DecisionNode(
            "DECISION_001", "決策1", "測試決策", 1,
            DecisionType.SPEL, null, "true"
        );
        DecisionNode decision2 = new DecisionNode(
            "DECISION_002", "決策2", "測試決策", 2,
            DecisionType.SPEL, null, "true"
        );
        List<DecisionNode> decisions = Arrays.asList(decision1, decision2);

        // 設置模擬行為
        when(decisionEvaluationService.evaluate(decision1, context)).thenReturn(true);
        when(decisionEvaluationService.evaluate(decision2, context)).thenReturn(true);

        // 執行測試
        boolean result = logicCompositionService.composeDecisions(decisions, "AND", context);
        assertTrue(result);

        // 驗證呼叫
        verify(decisionEvaluationService, times(1)).evaluate(decision1, context);
        verify(decisionEvaluationService, times(1)).evaluate(decision2, context);
    }

    @Test
    void shouldComposeDecisionsWithOrOperator() {
        // 準備測試資料
        DecisionNode decision1 = new DecisionNode(
            "DECISION_003", "決策3", "測試決策", 1,
            DecisionType.SPEL, null, "false"
        );
        DecisionNode decision2 = new DecisionNode(
            "DECISION_004", "決策4", "測試決策", 2,
            DecisionType.SPEL, null, "true"
        );
        List<DecisionNode> decisions = Arrays.asList(decision1, decision2);

        // 設置模擬行為
        when(decisionEvaluationService.evaluate(decision1, context)).thenReturn(false);
        when(decisionEvaluationService.evaluate(decision2, context)).thenReturn(true);

        // 執行測試
        boolean result = logicCompositionService.composeDecisions(decisions, "OR", context);
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionForUnsupportedOperator() {
        // 準備測試資料
        DecisionNode decision = new DecisionNode(
            "DECISION_005", "決策5", "測試決策", 1,
            DecisionType.SPEL, null, "true"
        );
        List<DecisionNode> decisions = Arrays.asList(decision);

        // 執行測試並驗證異常
        assertThrows(LogicCompositionException.class, () -> {
            logicCompositionService.composeDecisions(decisions, "XOR", context);
        });
    }

    @Test
    void shouldEvaluateSpelExpression() {
        // 準備測試資料
        String expression = "#amount > 500 and #amount < 2000";

        // 執行測試
        boolean result = logicCompositionService.evaluateExpression(expression, context);
        assertTrue(result);
    }

    @Test
    void shouldValidateSpelExpression() {
        // 準備測試資料
        String validExpression = "#amount > 0";
        String invalidExpression = "#amount >";

        // 執行測試
        assertTrue(logicCompositionService.validateExpression(validExpression));
        assertFalse(logicCompositionService.validateExpression(invalidExpression));
    }

    @Test
    void shouldHandleNullValuesInExpression() {
        // 準備測試資料
        String expression = "context.getVariable('nonexistent') == null";

        // 執行測試
        boolean result = logicCompositionService.evaluateExpression(expression, context);
        assertTrue(result);
    }

    @Test
    void shouldHandleComplexLogicComposition() {
        // 準備測試資料
        DecisionNode decision1 = new DecisionNode(
            "DECISION_006", "決策6", "測試決策", 1,
            DecisionType.SPEL, null, "true"
        );
        DecisionNode decision2 = new DecisionNode(
            "DECISION_007", "決策7", "測試決策", 2,
            DecisionType.SPEL, null, "false"
        );
        DecisionNode decision3 = new DecisionNode(
            "DECISION_008", "決策8", "測試決策", 3,
            DecisionType.SPEL, null, "true"
        );
        List<DecisionNode> decisions = Arrays.asList(decision1, decision2, decision3);

        // 設置模擬行為
        when(decisionEvaluationService.evaluate(decision1, context)).thenReturn(true);
        when(decisionEvaluationService.evaluate(decision2, context)).thenReturn(false);
        when(decisionEvaluationService.evaluate(decision3, context)).thenReturn(true);

        // 測試 OR 組合
        boolean orResult = logicCompositionService.composeDecisions(decisions, "OR", context);
        assertTrue(orResult);

        // 測試 AND 組合
        boolean andResult = logicCompositionService.composeDecisions(decisions, "AND", context);
        assertFalse(andResult);
    }

    @Test
    void shouldHandleConcurrentLogicComposition() throws InterruptedException {
        // 準備測試資料
        DecisionNode decision = new DecisionNode(
            "DECISION_009", "決策9", "測試決策", 1,
            DecisionType.SPEL, null, "true"
        );
        List<DecisionNode> decisions = Arrays.asList(decision);
        when(decisionEvaluationService.evaluate(decision, context)).thenReturn(true);

        // 創建多個執行緒
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        boolean[] results = new boolean[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = logicCompositionService.composeDecisions(decisions, "AND", context);
            });
        }

        // 執行所有執行緒
        for (Thread thread : threads) {
            thread.start();
        }

        // 等待所有執行緒完成
        for (Thread thread : threads) {
            thread.join();
        }

        // 驗證所有結果一致
        for (boolean result : results) {
            assertTrue(result);
        }
    }
}