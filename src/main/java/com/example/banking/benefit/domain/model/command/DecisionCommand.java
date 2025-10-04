package com.example.banking.benefit.domain.model.command;

import com.example.banking.benefit.domain.model.common.ExecutionContext;

/**
 * 決策命令介面
 * 所有決策節點的 Java 實作類別都必須實作此介面
 */
public interface DecisionCommand {
    
    /**
     * 檢查決策命令是否可以執行
     *
     * @param context 執行上下文
     * @return 如果可以執行返回 true，否則返回 false
     */
    boolean canExecute(ExecutionContext context);
    
    /**
     * 執行決策命令
     *
     * @param context 執行上下文
     * @return 決策結果
     */
    boolean evaluate(ExecutionContext context);
}