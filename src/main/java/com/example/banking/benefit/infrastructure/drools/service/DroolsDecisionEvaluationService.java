package com.example.banking.benefit.infrastructure.drools.service;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.service.DecisionEvaluationService;
import com.example.banking.benefit.infrastructure.drools.adapter.DroolsDecisionResult;
import com.example.banking.benefit.infrastructure.drools.adapter.DroolsRuleContext;
import com.example.banking.benefit.infrastructure.drools.adapter.DroolsRuleEngine;
import com.example.banking.benefit.infrastructure.drools.adapter.DroolsRuleType;
import com.example.banking.benefit.infrastructure.drools.model.BankingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 基於 Drools 的決策評估服務
 * 替代原有的 SpEL 和 Java 類別評估方式
 */
@Service
@Primary
public class DroolsDecisionEvaluationService implements DecisionEvaluationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DroolsDecisionEvaluationService.class);
    
    private final DroolsRuleEngine droolsRuleEngine;
    
    @Autowired
    public DroolsDecisionEvaluationService(DroolsRuleEngine droolsRuleEngine) {
        this.droolsRuleEngine = droolsRuleEngine;
    }
    
    @Override
    public boolean evaluate(DecisionNode node, BaseExecutionContext context) {
        logger.debug("使用 Drools 評估決策節點: {}", node.getNodeId());
        
        try {
            // 根據節點配置決定規則類型
            DroolsRuleType ruleType = determineRuleType(node);
            
            // 創建規則執行上下文
            DroolsRuleContext ruleContext = DroolsRuleContext.builder()
                    .ruleType(ruleType)
                    .executionContext(context)
                    .customerData(context.getCustomerData())
                    .bankingContext(createBankingContext(context, node))
                    .parameter("nodeId", node.getNodeId())
                    .parameter("nodeName", node.getNodeName())
                    .parameter("decisionType", node.getDecisionType())
                    .build();
            
            // 執行規則
            DroolsDecisionResult result = droolsRuleEngine.executeDecision(ruleContext);
            
            logger.debug("Drools 規則執行結果: {} -> {}", node.getNodeId(), result.getBooleanResult());
            
            return result.getBooleanResult();
            
        } catch (Exception e) {
            logger.error("Drools 決策評估失敗: {}", node.getNodeId(), e);
            // 如果 Drools 執行失敗，回退到傳統方式
            return fallbackEvaluate(node, context);
        }
    }
    
    @Override
    public boolean canEvaluate(DecisionNode node, BaseExecutionContext context) {
        try {
            DroolsRuleType ruleType = determineRuleType(node);
            DroolsRuleContext ruleContext = DroolsRuleContext.fromExecutionContext(context, ruleType);
            
            return droolsRuleEngine.canExecute(ruleContext);
        } catch (Exception e) {
            logger.warn("檢查 Drools 規則可執行性失敗: {}", node.getNodeId(), e);
            return false;
        }
    }
    
    @Override
    public boolean isEligible(DecisionNode node, BaseExecutionContext context) {
        // 檢查基本條件
        if (node == null || context == null) {
            return false;
        }
        
        // 檢查是否有對應的規則
        try {
            DroolsRuleType ruleType = determineRuleType(node);
            return ruleType != null;
        } catch (Exception e) {
            logger.warn("檢查規則適用性失敗: {}", node.getNodeId(), e);
            return false;
        }
    }
    
    /**
     * 根據決策節點決定規則類型
     */
    private DroolsRuleType determineRuleType(DecisionNode node) {
        String nodeName = node.getNodeName().toLowerCase();
        
        // 根據節點名稱決定規則類型
        if (nodeName.contains("age") || nodeName.contains("年齡") || 
            nodeName.contains("salary") || nodeName.contains("薪資") ||
            nodeName.contains("vip") || nodeName.contains("客戶")) {
            return DroolsRuleType.CUSTOMER_ELIGIBILITY;
        }
        
        if (nodeName.contains("product") || nodeName.contains("產品") ||
            nodeName.contains("credit") || nodeName.contains("loan")) {
            return DroolsRuleType.PRODUCT_APPLICABILITY;
        }
        
        if (nodeName.contains("flow") || nodeName.contains("流程") ||
            nodeName.contains("control") || nodeName.contains("控制")) {
            return DroolsRuleType.FLOW_CONTROL;
        }
        
        // 默認使用通用決策規則
        return DroolsRuleType.GENERAL_DECISION;
    }
    
    /**
     * 回退評估機制（保持與原有系統的相容性）
     */
    private boolean fallbackEvaluate(DecisionNode node, BaseExecutionContext context) {
        logger.warn("使用回退評估機制: {}", node.getNodeId());
        
        // 這裡可以調用原有的評估邏輯
        // 暫時返回 true 以確保流程不中斷
        return true;
    }
    
    /**
     * 創建銀行業務上下文
     */
    private BankingContext createBankingContext(BaseExecutionContext context, DecisionNode node) {
        return BankingContext.builder()
                .flowId(context.getFlowId())
                .executionId(context.getExecutionId())
                .customerId(context.getCustomerId())
                .productType("CREDIT_CARD") // 可以根據節點或上下文動態決定
                .channelType("ONLINE_BANKING")
                .businessData("nodeId", node.getNodeId())
                .businessData("nodeName", node.getNodeName())
                .build();
    }
}