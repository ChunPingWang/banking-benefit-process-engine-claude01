package com.example.banking.benefit.domain.service;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;

/**
 * 決策執行服務介面
 */
public interface DecisionEvaluationService {
    
    /**
     * 評估決策節點
     *
     * @param node 決策節點
     * @param context 執行內容
     * @return 評估結果（true/false）
     * @throws DecisionExecutionException 決策執行異常
     */
    boolean evaluate(DecisionNode node, BaseExecutionContext context);
    
    /**
     * 檢查決策節點的執行條件是否滿足
     *
     * @param node 決策節點
     * @param context 執行內容
     * @return 條件是否滿足
     */
    boolean isEligible(DecisionNode node, BaseExecutionContext context);

    /**
     * 檢查決策節點是否可以被評估
     *
     * @param node 決策節點
     * @param context 執行內容
     * @return 是否可評估
     */
    boolean canEvaluate(DecisionNode node, BaseExecutionContext context);
}