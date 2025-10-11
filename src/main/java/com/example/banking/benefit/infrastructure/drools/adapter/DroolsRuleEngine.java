package com.example.banking.benefit.infrastructure.drools.adapter;

import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Drools 規則引擎適配器
 * 負責執行規則並返回決策結果
 */
@Service
public class DroolsRuleEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(DroolsRuleEngine.class);
    
    private final KieSession defaultKieSession;
    private final KieSession customerKieSession;
    private final KieSession productKieSession;
    private final KieSession flowKieSession;
    
    // 結果快取
    private final Map<String, Object> resultCache = new ConcurrentHashMap<>();
    
    @Autowired
    public DroolsRuleEngine(
            KieSession defaultKieSession,
            @Qualifier("customerKieSession") KieSession customerKieSession,
            @Qualifier("productKieSession") KieSession productKieSession,
            @Qualifier("flowKieSession") KieSession flowKieSession) {
        this.defaultKieSession = defaultKieSession;
        this.customerKieSession = customerKieSession;
        this.productKieSession = productKieSession;
        this.flowKieSession = flowKieSession;
    }
    
    /**
     * 執行決策規則
     * 
     * @param ruleContext 規則執行上下文
     * @return 決策結果
     */
    public DroolsDecisionResult executeDecision(DroolsRuleContext ruleContext) {
        logger.debug("開始執行決策規則: {}", ruleContext.getRuleType());
        
        try {
            KieSession session = getKieSessionByType(ruleContext.getRuleType());
            
            // 插入事實物件
            insertFacts(session, ruleContext);
            
            // 執行規則
            int firedRules = session.fireAllRules();
            
            // 收集結果
            DroolsDecisionResult result = collectResults(session, ruleContext);
            
            // 清理 session
            session.dispose();
            
            logger.debug("規則執行完成，觸發 {} 條規則，結果: {}", firedRules, result.isSuccess());
            
            return result;
            
        } catch (Exception e) {
            logger.error("執行規則失敗: {}", ruleContext.getRuleType(), e);
            return DroolsDecisionResult.failure("規則執行失敗: " + e.getMessage());
        }
    }
    
    /**
     * 批次執行規則
     * 
     * @param contexts 規則上下文列表
     * @return 批次執行結果
     */
    public List<DroolsDecisionResult> executeBatch(List<DroolsRuleContext> contexts) {
        return contexts.stream()
                .map(this::executeDecision)
                .toList();
    }
    
    /**
     * 驗證規則是否可以執行
     * 
     * @param ruleContext 規則上下文
     * @return 是否可以執行
     */
    public boolean canExecute(DroolsRuleContext ruleContext) {
        try {
            return ruleContext != null && 
                   ruleContext.getRuleType() != null && 
                   ruleContext.getExecutionContext() != null;
        } catch (Exception e) {
            logger.warn("檢查規則執行條件失敗", e);
            return false;
        }
    }
    
    /**
     * 根據規則類型獲取對應的 KieSession
     */
    private KieSession getKieSessionByType(DroolsRuleType ruleType) {
        return switch (ruleType) {
            case CUSTOMER_ELIGIBILITY -> customerKieSession;
            case PRODUCT_APPLICABILITY -> productKieSession;
            case FLOW_CONTROL -> flowKieSession;
            default -> defaultKieSession;
        };
    }
    
    /**
     * 插入事實物件到 KieSession
     */
    private void insertFacts(KieSession session, DroolsRuleContext ruleContext) {
        // 插入客戶資料
        if (ruleContext.getCustomerData() != null) {
            session.insert(ruleContext.getCustomerData());
        }
        
        // 插入銀行上下文
        if (ruleContext.getBankingContext() != null) {
            session.insert(ruleContext.getBankingContext());
        }
        
        // 插入執行上下文
        if (ruleContext.getExecutionContext() != null) {
            session.insert(ruleContext.getExecutionContext());
        }
        
        // 插入其他事實物件
        if (ruleContext.getAdditionalFacts() != null) {
            ruleContext.getAdditionalFacts().forEach(session::insert);
        }
    }
    
    /**
     * 收集規則執行結果
     */
    private DroolsDecisionResult collectResults(KieSession session, DroolsRuleContext ruleContext) {
        // 從 session 中收集 DroolsDecisionResult 類型的物件
        return session.getObjects(object -> object instanceof DroolsDecisionResult)
                .stream()
                .map(obj -> (DroolsDecisionResult) obj)
                .findFirst()
                .orElse(DroolsDecisionResult.success("規則執行完成但無明確結果"));
    }
    
    /**
     * 清理資源
     */
    public void cleanup() {
        resultCache.clear();
        logger.info("Drools 規則引擎資源清理完成");
    }
}