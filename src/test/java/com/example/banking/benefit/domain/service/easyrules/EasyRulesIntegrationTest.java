package com.example.banking.benefit.domain.service.easyrules;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import com.example.banking.benefit.domain.model.common.DefaultExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.result.ExecutionStatus;
import com.example.banking.benefit.domain.service.easyrules.adapter.DecisionRuleAdapter;
import com.example.banking.benefit.domain.service.easyrules.adapter.RuleConverter;
import com.example.banking.benefit.domain.service.easyrules.engine.EasyRulesDecisionEngine;
import org.jeasy.rules.api.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Easy Rules 引擎整合測試
 */
@ExtendWith(MockitoExtension.class)
class EasyRulesIntegrationTest {

    private EasyRulesDecisionEngine easyRulesEngine;
    private RuleConverter ruleConverter;
    private BaseExecutionContext context;

    @BeforeEach
    void setUp() {
        ruleConverter = new RuleConverter();
        easyRulesEngine = new EasyRulesDecisionEngine(ruleConverter);
        
        // 設置測試上下文
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("age", CustomerAttribute.forInteger(30));
        attributes.put("accountBalance", CustomerAttribute.forDouble(50000.0));
        attributes.put("membershipLevel", CustomerAttribute.forString("GOLD"));
        attributes.put("vip", CustomerAttribute.forBoolean(true));
        
        CustomerData customerData = CustomerData.create("TEST001", attributes);
        context = DefaultExecutionContext.builder()
                .flowId("FLOW001")
                .customerId("TEST001")
                .customerData(customerData)
                .build();
    }

    @Test
    void testSpelDecisionExecution() {
        // 創建 SpEL 決策節點
        DecisionNode spelNode = DecisionNode.createSpELDecision(
            "TEST_NODE_001",
            "Age Check Decision",
            "Check if customer age is greater than 18",
            "#{#customerData.get('age') > 18}"
        );
        spelNode.setNodeOrder(1);

        // 執行決策
        ExecutionResult result = easyRulesEngine.executeDecision(spelNode, context);

        // 驗證結果
        assertNotNull(result);
        assertEquals(ExecutionStatus.SUCCESS, result.getStatus());
        assertTrue(result.getExecutionId().startsWith("ER-"));
        
        // 驗證決策結果被正確保存
        Object decisionResult = context.getVariable("decision_TEST_NODE_001");
        assertNotNull(decisionResult);
        assertEquals(Boolean.TRUE, decisionResult);
    }

    @Test
    void testJavaClassDecisionExecution() {
        // 創建 Java 類別決策節點
        DecisionNode javaNode = DecisionNode.createJavaClassDecision(
            "TEST_NODE_002",
            "Balance Check Decision", 
            "Check if customer has sufficient balance",
            "com.example.banking.benefit.domain.service.easyrules.EasyRulesIntegrationTest$TestBalanceChecker"
        );
        javaNode.setNodeOrder(2);

        // 執行決策
        ExecutionResult result = easyRulesEngine.executeDecision(javaNode, context);

        // 驗證結果
        assertNotNull(result);
        assertEquals(ExecutionStatus.SUCCESS, result.getStatus());
        
        // 驗證決策結果
        Object decisionResult = context.getVariable("decision_TEST_NODE_002");
        assertNotNull(decisionResult);
        assertEquals(Boolean.TRUE, decisionResult);
    }

    @Test
    void testMultipleDecisionExecution() {
        // 創建多個決策節點
        DecisionNode node1 = DecisionNode.createSpELDecision(
            "MULTI_001",
            "Age Check",
            "Age validation",
            "#{#customerData.get('age') >= 18}"
        );
        node1.setNodeOrder(1);

        DecisionNode node2 = DecisionNode.createSpELDecision(
            "MULTI_002", 
            "Balance Check",
            "Balance validation",
            "#{#customerData.get('accountBalance') > 1000}"
        );
        node2.setNodeOrder(2);

        List<DecisionNode> nodes = Arrays.asList(node1, node2);

        // 執行多個決策
        ExecutionResult result = easyRulesEngine.executeDecisions(nodes, context);

        // 驗證結果
        assertNotNull(result);
        assertEquals(ExecutionStatus.SUCCESS, result.getStatus());
        
        // 驗證兩個決策結果都被正確保存
        assertEquals(Boolean.TRUE, context.getVariable("decision_MULTI_001"));
        assertEquals(Boolean.TRUE, context.getVariable("decision_MULTI_002"));
    }

    @Test
    void testRuleConverter() {
        // 測試規則轉換器
        DecisionNode node = DecisionNode.createSpELDecision(
            "CONVERT_001",
            "Test Conversion",
            "Test rule conversion",
            "#{true}"
        );

        // 測試單個轉換
        assertTrue(ruleConverter.canConvert(node));
        DecisionRuleAdapter ruleAdapter = ruleConverter.convertToRule(node);
        assertNotNull(ruleAdapter);
        assertEquals("Test Conversion", ruleAdapter.getName());

        // 測試批量轉換
        Rules rules = ruleConverter.convertToRules(Arrays.asList(node));
        assertNotNull(rules);
        assertFalse(rules.isEmpty());
    }

    @Test
    void testInvalidDecisionHandling() {
        // 創建一個有效的決策節點，但手動設置為無效狀態
        DecisionNode invalidNode = DecisionNode.createSpELDecision(
            "INVALID_001",
            "Invalid Node",
            "Invalid decision node",
            "#{true}"  // 先創建有效的
        );
        
        // 手動設置為無效狀態（空表達式）
        invalidNode.setSpelExpression("");

        // 驗證無法轉換
        assertFalse(ruleConverter.canConvert(invalidNode));
        assertFalse(easyRulesEngine.canHandle(invalidNode));
    }

    @Test
    void testEngineStats() {
        // 測試引擎統計資訊
        String stats = easyRulesEngine.getEngineStats();
        assertNotNull(stats);
        assertTrue(stats.contains("EasyRules Engine"));
    }

    /**
     * 測試用的餘額檢查器
     */
    public static class TestBalanceChecker {
        public boolean evaluate(BaseExecutionContext context, java.util.Map<String, Object> variables) {
            // 從客戶資料中取得餘額
            Object balanceObj = context.getCustomerData().get("accountBalance");
            if (balanceObj instanceof Double) {
                return ((Double) balanceObj) > 10000;
            }
            return false;
        }
    }
}