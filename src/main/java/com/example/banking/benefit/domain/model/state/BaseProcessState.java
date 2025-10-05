package com.example.banking.benefit.domain.model.state;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.result.ProcessResult;
import com.example.banking.benefit.domain.exception.ProcessStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 處理狀態的基礎抽象類別
 * 提供共用功能實作和記錄功能
 */
public abstract class BaseProcessState implements ProcessState {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseProcessState.class);
    
    protected final String flowId;
    protected final String nodeId;
    protected final String stateName;
    
    protected BaseProcessState(String flowId, String nodeId, String stateName) {
        this.flowId = flowId;
        this.nodeId = nodeId;
        this.stateName = stateName;
    }
    
    @Override
    public String getStateName() {
        return stateName;
    }
    
    @Override
    public boolean canEnter(ExecutionContext context) {
        try {
            validateContext(context);
            return doCanEnter(context);
        } catch (Exception e) {
            logger.warn("無法進入狀態 {} [flowId={}, nodeId={}]: {}", 
                stateName, flowId, nodeId, e.getMessage());
            return false;
        }
    }
    
    @Override
    public void onEnter(ExecutionContext context) {
        logger.debug("進入狀態 {} [flowId={}, nodeId={}]", 
            stateName, flowId, nodeId);
        validateContext(context);
        doOnEnter(context);
    }
    
    @Override
    public ProcessResult execute(ExecutionContext context) {
        validateContext(context);
        
        try {
            logger.debug("開始執行狀態 {} [flowId={}, nodeId={}]", 
                stateName, flowId, nodeId);
            ProcessResult result = doExecute(context);
            logger.debug("狀態執行完成 {} [flowId={}, nodeId={}], 結果: {}", 
                stateName, flowId, nodeId, result);
            return result;
        } catch (Exception e) {
            logger.error("狀態執行失敗 {} [flowId={}, nodeId={}]: {}", 
                stateName, flowId, nodeId, e.getMessage(), e);
            throw new ProcessStateException("狀態執行失敗", flowId, nodeId, e);
        }
    }
    
    @Override
    public void onExit(ExecutionContext context) {
        logger.debug("離開狀態 {} [flowId={}, nodeId={}]", 
            stateName, flowId, nodeId);
        validateContext(context);
        doOnExit(context);
    }
    
    @Override
    public ProcessState determineNextState(ExecutionContext context, ProcessResult result) {
        validateContext(context);
        return doDetermineNextState(context, result);
    }
    
    /**
     * 驗證是否可以進入此狀態的具體實作
     */
    protected boolean doCanEnter(ExecutionContext context) {
        return true;  // 預設允許進入
    }
    
    /**
     * 進入狀態時的具體處理邏輯
     */
    protected void doOnEnter(ExecutionContext context) {
        // 預設不執行任何動作
    }
    
    /**
     * 執行狀態相關的具體處理邏輯
     */
    protected abstract ProcessResult doExecute(ExecutionContext context);
    
    /**
     * 離開狀態時的具體處理邏輯
     */
    protected void doOnExit(ExecutionContext context) {
        // 預設不執行任何動作
    }
    
    /**
     * 確定下一個狀態的具體邏輯
     */
    protected abstract ProcessState doDetermineNextState(ExecutionContext context, ProcessResult result);
    
    /**
     * 驗證執行上下文
     */
    protected void validateContext(ExecutionContext context) {
        if (context == null) {
            throw new IllegalArgumentException("執行上下文不可為 null");
        }
        
        // 子類別可以覆寫此方法增加額外的驗證邏輯
    }
}