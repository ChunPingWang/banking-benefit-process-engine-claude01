package com.example.banking.benefit.domain.service.easyrules.adapter;

import com.example.banking.benefit.domain.model.node.DecisionNode;
import org.jeasy.rules.api.Rules;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 規則轉換器
 * 負責將 DecisionNode 轉換為 Easy Rules 的 Rule
 */
@Component
public class RuleConverter {
    
    /**
     * 將單個 DecisionNode 轉換為 DecisionRuleAdapter
     */
    public DecisionRuleAdapter convertToRule(DecisionNode decisionNode) {
        if (decisionNode == null) {
            throw new IllegalArgumentException("DecisionNode cannot be null");
        }
        
        return new DecisionRuleAdapter(decisionNode);
    }
    
    /**
     * 將多個 DecisionNode 轉換為 Rules 集合
     */
    public Rules convertToRules(List<DecisionNode> decisionNodes) {
        Rules rules = new Rules();
        
        if (decisionNodes != null && !decisionNodes.isEmpty()) {
            for (DecisionNode node : decisionNodes) {
                try {
                    DecisionRuleAdapter ruleAdapter = convertToRule(node);
                    rules.register(ruleAdapter);
                } catch (Exception e) {
                    // 記錄轉換失敗的節點，但不中斷整個流程
                    throw new RuntimeException("Failed to convert DecisionNode to Rule: " + node.getNodeId(), e);
                }
            }
        }
        
        return rules;
    }
    
    /**
     * 檢查 DecisionNode 是否可以轉換為規則
     */
    public boolean canConvert(DecisionNode decisionNode) {
        if (decisionNode == null) {
            return false;
        }
        
        // 檢查必要的條件
        boolean hasValidType = decisionNode.getDecisionType() != null;
        boolean hasValidExpression = (decisionNode.getSpelExpression() != null && !decisionNode.getSpelExpression().trim().isEmpty()) ||
                                   (decisionNode.getImplementationClass() != null && !decisionNode.getImplementationClass().trim().isEmpty());
        
        return hasValidType && hasValidExpression;
    }
}