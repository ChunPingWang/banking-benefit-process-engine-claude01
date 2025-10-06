package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.exception.LogicCompositionException;
import com.example.banking.benefit.domain.service.LogicCompositionService;
import com.example.banking.benefit.domain.service.DecisionEvaluationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

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
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("amount", CustomerAttribute.forInteger(1000));
        CustomerData customerData = CustomerData.create("CUST_001", attributes);
        context = ExecutionContext.create("FLOW_001", "CUST_001", customerData);
    }

    @Test
    void shouldComposeDecisionsWithAndOperator() {
        // 準備測試資料
        DecisionNode decision1 = DecisionNode.createSpELDecision(
            "DECISION_001", "決策1", "測試決策", "true"
        );
        DecisionNode decision2 = DecisionNode.createSpELDecision(
            "DECISION_002", "決策2", "測試決策", "true"
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
        DecisionNode decision1 = DecisionNode.createSpELDecision(
            "DECISION_003", "決策3", "測試決策", "false"
        );
        DecisionNode decision2 = DecisionNode.createSpELDecision(
            "DECISION_004", "決策4", "測試決策", "true"
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
        DecisionNode decision = DecisionNode.createSpELDecision(
            "DECISION_005", "決策5", "測試決策", "true"
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
        String expression = "#customer.get('amount') > 500 and #customer.get('amount') < 2000";

        // 執行測試
        boolean result = logicCompositionService.evaluateExpression(expression, context);
        assertTrue(result);
    }

    @Test
    void shouldValidateSpelExpression() {
        // 準備測試資料
        String validExpression = "#customer.get('amount') > 0";
        String invalidExpression = "#customer.get('amount') >";

        // 執行測試
        assertTrue(logicCompositionService.validateExpression(validExpression));
        assertFalse(logicCompositionService.validateExpression(invalidExpression));
    }

    @Test
    void shouldHandleNullValuesInExpression() {
        // 準備測試資料 - 測試訪問不存在的變量
        String expression = "#nonexistent == null";

        // 執行測試 - 不存在的變量應該返回 null
        boolean result = logicCompositionService.evaluateExpression(expression, context);
        assertTrue(result);
    }

    @Test
    void shouldHandleComplexLogicComposition() {
        // 準備測試資料
        DecisionNode decision1 = DecisionNode.createSpELDecision(
            "DECISION_006", "決策6", "測試決策", "true"
        );
        DecisionNode decision2 = DecisionNode.createSpELDecision(
            "DECISION_007", "決策7", "測試決策", "false"
        );
        DecisionNode decision3 = DecisionNode.createSpELDecision(
            "DECISION_008", "決策8", "測試決策", "true"
        );
        List<DecisionNode> decisions = Arrays.asList(decision1, decision2, decision3);

        // 設置模擬行為 - 使用 lenient 以允許不是所有 stubbing 都被使用
        lenient().when(decisionEvaluationService.evaluate(decision1, context)).thenReturn(true);
        lenient().when(decisionEvaluationService.evaluate(decision2, context)).thenReturn(false);
        lenient().when(decisionEvaluationService.evaluate(decision3, context)).thenReturn(true);

        // 測試 OR 組合 - 只要有一個 true 就返回 true，可能不會評估所有節點
        boolean orResult = logicCompositionService.composeDecisions(decisions, "OR", context);
        assertTrue(orResult);

        // 測試 AND 組合 - 所有節點都會被評估
        boolean andResult = logicCompositionService.composeDecisions(decisions, "AND", context);
        assertFalse(andResult);
    }

    @Test
    void shouldHandleConcurrentLogicComposition() throws InterruptedException {
        // 準備測試資料
        DecisionNode decision = DecisionNode.createSpELDecision(
            "DECISION_009", "決策9", "測試決策", "true"
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