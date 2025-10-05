package com.example.banking.benefit.domain.model.command;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.exception.DecisionExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 決策命令的基礎抽象類別
 * 提供共用功能實作和記錄功能
 */
public abstract class BaseDecisionCommand implements DecisionCommand {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseDecisionCommand.class);
    
    protected final String flowId;
    protected final String nodeId;
    
    protected BaseDecisionCommand(String flowId, String nodeId) {
        this.flowId = flowId;
        this.nodeId = nodeId;
    }
    
    @Override
    public boolean canExecute(ExecutionContext context) {
        try {
            validateContext(context);
            return true;
        } catch (Exception e) {
            logger.warn("決策命令無法執行: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public final boolean evaluate(ExecutionContext context) {
        validateContext(context);
        
        try {
            logger.debug("開始執行決策命令: {} [flowId={}, nodeId={}]", 
                this.getClass().getSimpleName(), flowId, nodeId);
            boolean result = doEvaluate(context);
            logger.debug("決策命令執行完成: {}, 結果: {} [flowId={}, nodeId={}]", 
                this.getClass().getSimpleName(), result, flowId, nodeId);
            return result;
        } catch (Exception e) {
            logger.error("決策命令執行失敗: {} [flowId={}, nodeId={}]", 
                e.getMessage(), flowId, nodeId, e);
            throw new DecisionExecutionException("決策執行失敗", flowId, nodeId, e);
        }
    }
    
    /**
     * 執行具體的決策邏輯
     *
     * @param context 執行上下文
     * @return 決策結果
     */
    protected abstract boolean doEvaluate(ExecutionContext context);
    
    /**
     * 驗證執行上下文
     *
     * @param context 執行上下文
     * @throws IllegalArgumentException 如果上下文無效
     */
    protected void validateContext(ExecutionContext context) {
        if (context == null) {
            throw new IllegalArgumentException("執行上下文不可為 null");
        }
        
        // 子類別可以覆寫此方法增加額外的驗證邏輯
    }
}