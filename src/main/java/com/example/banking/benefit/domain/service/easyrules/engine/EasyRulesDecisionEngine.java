package com.example.banking.benefit.domain.service.easyrules.engine;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.service.easyrules.adapter.DecisionRuleAdapter;
import com.example.banking.benefit.domain.service.easyrules.adapter.RuleConverter;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Easy Rules 決策執行引擎
 * 使用 Easy Rules 框架來執行決策邏輯
 */
@Service
public class EasyRulesDecisionEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(EasyRulesDecisionEngine.class);
    
    private final RuleConverter ruleConverter;
    private final RulesEngine rulesEngine;
    
    @Autowired
    public EasyRulesDecisionEngine(RuleConverter ruleConverter) {
        this.ruleConverter = ruleConverter;
        this.rulesEngine = new DefaultRulesEngine();
        
        // 配置規則引擎
        configureRulesEngine();
    }
    
    /**
     * 執行單個決策節點
     */
    public ExecutionResult executeDecision(DecisionNode decisionNode, BaseExecutionContext context) {
        try {
            // 轉換為規則
            DecisionRuleAdapter ruleAdapter = ruleConverter.convertToRule(decisionNode);
            Rules rules = new Rules();
            rules.register(ruleAdapter);
            
            // 準備事實
            Facts facts = new Facts();
            facts.put("context", context);
            
            // 執行規則
            rulesEngine.fire(rules, facts);
            
            // 取得執行結果
            boolean result = ruleAdapter.getConditionResult();
            String executionId = generateExecutionId();
            
            logger.info("Decision node {} executed with result: {}", decisionNode.getNodeId(), result);
            
            return ExecutionResult.success(
                FlowId.of(context.getFlowId()), 
                executionId, 
                context.getVariables()
            );
            
        } catch (Exception e) {
            logger.error("Error executing decision node {}: {}", decisionNode.getNodeId(), e.getMessage(), e);
            
            return ExecutionResult.failure(
                FlowId.of(context.getFlowId()), 
                generateExecutionId(), 
                "Decision execution failed: " + e.getMessage()
            );
        }
    }
    
    /**
     * 執行多個決策節點
     */
    public ExecutionResult executeDecisions(List<DecisionNode> decisionNodes, BaseExecutionContext context) {
        try {
            // 轉換為規則集合
            Rules rules = ruleConverter.convertToRules(decisionNodes);
            
            // 準備事實
            Facts facts = new Facts();
            facts.put("context", context);
            
            // 執行所有規則
            rulesEngine.fire(rules, facts);
            
            String executionId = generateExecutionId();
            logger.info("Executed {} decision rules", decisionNodes.size());
            
            return ExecutionResult.success(
                FlowId.of(context.getFlowId()), 
                executionId, 
                context.getVariables()
            );
            
        } catch (Exception e) {
            logger.error("Error executing decision nodes: {}", e.getMessage(), e);
            
            return ExecutionResult.failure(
                FlowId.of(context.getFlowId()), 
                generateExecutionId(), 
                "Decisions execution failed: " + e.getMessage()
            );
        }
    }
    
    /**
     * 檢查決策節點是否可以被處理
     */
    public boolean canHandle(DecisionNode decisionNode) {
        return ruleConverter.canConvert(decisionNode);
    }
    
    /**
     * 配置規則引擎參數
     */
    private void configureRulesEngine() {
        // 這裡可以設置規則引擎的參數
        // 例如：是否跳過後續規則、最大規則數量等
        logger.info("Easy Rules engine configured successfully");
    }
    
    /**
     * 生成執行 ID
     */
    private String generateExecutionId() {
        return "ER-" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 取得規則引擎統計資訊
     */
    public String getEngineStats() {
        return String.format("EasyRules Engine - Type: %s", rulesEngine.getClass().getSimpleName());
    }
}