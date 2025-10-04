package com.example.banking.benefit.domain.model.decision;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;

/**
 * 決策命令介面
 * 所有自訂的決策邏輯類別都必須實作此介面
 */
public interface DecisionCommand {
    
    /**
     * 評估決策邏輯
     * @param context 執行內容
     * @return 決策結果
     */
    boolean evaluate(BaseExecutionContext context);
    
    /**
     * 驗證決策命令是否可以執行
     * @param context 執行內容
     * @return 是否可執行
     */
    default boolean canExecute(BaseExecutionContext context) {
        return true;
    }
    
    /**
     * 取得決策說明
     * @return 決策說明
     */
    default String getDescription() {
        return getClass().getSimpleName();
    }
}