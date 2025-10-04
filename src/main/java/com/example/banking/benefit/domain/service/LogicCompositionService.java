package com.example.banking.benefit.domain.service;

import java.util.List;
import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.process.ProcessNode;

/**
 * 邏輯組合服務介面
 */
public interface LogicCompositionService {
    
    /**
     * 組合多個決策節點的評估結果
     *
     * @param decisions 決策節點列表
     * @param operator 邏輯運算子 (AND, OR)
     * @param context 執行內容
     * @return 組合結果
     */
    boolean composeDecisions(List<DecisionNode> decisions, String operator, BaseExecutionContext context);
    
    /**
     * 檢查節點之間的轉換條件
     *
     * @param sourceNode 來源節點
     * @param targetNode 目標節點
     * @param context 執行內容
     * @return 是否符合轉換條件
     */
    boolean checkTransitionCondition(Object sourceNode, Object targetNode, BaseExecutionContext context);
    
    /**
     * 評估SpEL表達式
     *
     * @param expression SpEL表達式
     * @param context 執行內容
     * @return 評估結果
     */
    boolean evaluateExpression(String expression, BaseExecutionContext context);
    
    /**
     * 驗證表達式語法
     *
     * @param expression SpEL表達式
     * @return 是否為有效表達式
     */
    boolean validateExpression(String expression);
}