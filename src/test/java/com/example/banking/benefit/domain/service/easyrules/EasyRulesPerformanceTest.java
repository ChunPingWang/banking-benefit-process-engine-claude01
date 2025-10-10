package com.example.banking.benefit.domain.service.easyrules;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import com.example.banking.benefit.domain.model.common.DefaultExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.service.easyrules.engine.EasyRulesDecisionEngine;
import com.example.banking.benefit.domain.service.easyrules.adapter.RuleConverter;
import com.example.banking.benefit.domain.service.executor.DecisionNodeExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Easy Rules 與原有引擎的效能比較測試
 */
class EasyRulesPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(EasyRulesPerformanceTest.class);
    
    private EasyRulesDecisionEngine easyRulesEngine;
    private DecisionNodeExecutor legacyExecutor;
    private BaseExecutionContext context;
    private DecisionNode testDecisionNode;
    
    @BeforeEach
    void setUp() {
        // 設置 Easy Rules 引擎
        RuleConverter ruleConverter = new RuleConverter();
        easyRulesEngine = new EasyRulesDecisionEngine(ruleConverter);
        
        // 設置原有引擎
        legacyExecutor = new DecisionNodeExecutor();
        
        // 設置測試上下文
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("age", CustomerAttribute.forInteger(30));
        attributes.put("accountBalance", CustomerAttribute.forDouble(50000.0));
        attributes.put("vip", CustomerAttribute.forBoolean(true));
        
        CustomerData customerData = CustomerData.create("PERF_TEST", attributes);
        context = DefaultExecutionContext.builder()
                .flowId("PERF_FLOW")
                .customerId("PERF_TEST")
                .customerData(customerData)
                .build();
        
        // 創建測試決策節點
        testDecisionNode = DecisionNode.createSpELDecision(
            "PERF_NODE",
            "Performance Test Node",
            "Test performance of decision execution",
            "#{#customerData.get('age') > 18 && #customerData.get('accountBalance') > 1000}"
        );
    }
    
    @Test
    void testSingleExecutionPerformance() {
        // 預熱
        warmUp();
        
        // 測試 Easy Rules 引擎
        long easyRulesTime = measureExecutionTime(() -> {
            ExecutionResult result = easyRulesEngine.executeDecision(testDecisionNode, context);
            assertNotNull(result);
        }, 100);
        
        // 測試原有引擎
        long legacyTime = measureExecutionTime(() -> {
            Map<String, Object> nodeContext = new HashMap<>();
            ExecutionResult result = legacyExecutor.execute(testDecisionNode, context, nodeContext);
            assertNotNull(result);
        }, 100);
        
        logger.info("單次執行效能比較:");
        logger.info("Easy Rules 引擎: {} ms (平均)", easyRulesTime / 100.0);
        logger.info("原有引擎: {} ms (平均)", legacyTime / 100.0);
        logger.info("效能比率: {}", (double) easyRulesTime / legacyTime);
        
        // 驗證 Easy Rules 的效能應該在合理範圍內（不超過原有引擎的10倍）
        assertTrue(easyRulesTime < legacyTime * 10, 
                  String.format("Easy Rules 效能過慢: %d ms vs %d ms", easyRulesTime, legacyTime));
    }
    
    @Test
    void testBatchExecutionPerformance() {
        // 預熱
        warmUp();
        
        int iterations = 1000;
        
        // 測試 Easy Rules 引擎批量執行
        long easyRulesBatchTime = measureExecutionTime(() -> {
            for (int i = 0; i < iterations; i++) {
                ExecutionResult result = easyRulesEngine.executeDecision(testDecisionNode, context);
                assertNotNull(result);
            }
        }, 1);
        
        // 測試原有引擎批量執行
        long legacyBatchTime = measureExecutionTime(() -> {
            for (int i = 0; i < iterations; i++) {
                Map<String, Object> nodeContext = new HashMap<>();
                ExecutionResult result = legacyExecutor.execute(testDecisionNode, context, nodeContext);
                assertNotNull(result);
            }
        }, 1);
        
        logger.info("批量執行效能比較 ({} 次迭代):", iterations);
        logger.info("Easy Rules 引擎: {} ms", easyRulesBatchTime);
        logger.info("原有引擎: {} ms", legacyBatchTime);
        logger.info("每次執行平均時間 - Easy Rules: {} ms", easyRulesBatchTime / (double) iterations);
        logger.info("每次執行平均時間 - 原有引擎: {} ms", legacyBatchTime / (double) iterations);
        logger.info("效能比率: {}", (double) easyRulesBatchTime / legacyBatchTime);
    }
    
    @Test
    void testMemoryUsage() {
        // 預熱
        warmUp();
        
        // 測試記憶體使用
        Runtime runtime = Runtime.getRuntime();
        
        // 執行 GC 並記錄基準記憶體
        System.gc();
        long baseMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // 執行 Easy Rules 引擎多次
        for (int i = 0; i < 1000; i++) {
            ExecutionResult result = easyRulesEngine.executeDecision(testDecisionNode, context);
            assertNotNull(result);
        }
        
        // 記錄 Easy Rules 記憶體使用
        System.gc();
        long easyRulesMemory = (runtime.totalMemory() - runtime.freeMemory()) - baseMemory;
        
        // 重置
        System.gc();
        baseMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // 執行原有引擎多次
        for (int i = 0; i < 1000; i++) {
            Map<String, Object> nodeContext = new HashMap<>();
            ExecutionResult result = legacyExecutor.execute(testDecisionNode, context, nodeContext);
            assertNotNull(result);
        }
        
        // 記錄原有引擎記憶體使用
        System.gc();
        long legacyMemory = (runtime.totalMemory() - runtime.freeMemory()) - baseMemory;
        
        logger.info("記憶體使用比較 (1000 次執行):");
        logger.info("Easy Rules 引擎: {} bytes", easyRulesMemory);
        logger.info("原有引擎: {} bytes", legacyMemory);
        
        if (legacyMemory > 0) {
            logger.info("記憶體使用比率: {}", (double) easyRulesMemory / legacyMemory);
        }
    }
    
    private void warmUp() {
        // 預熱兩個引擎
        for (int i = 0; i < 50; i++) {
            easyRulesEngine.executeDecision(testDecisionNode, context);
            
            Map<String, Object> nodeContext = new HashMap<>();
            legacyExecutor.execute(testDecisionNode, context, nodeContext);
        }
    }
    
    private long measureExecutionTime(Runnable task, int iterations) {
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            task.run();
        }
        long endTime = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }
}