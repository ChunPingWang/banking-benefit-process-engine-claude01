package com.example.banking.benefit.domain.model.state;

import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.result.ProcessResult;

/**
 * 處理狀態介面
 * 定義狀態轉換和處理邏輯
 */
public interface ProcessState {
    
    /**
     * 獲取狀態名稱
     *
     * @return 狀態名稱
     */
    String getStateName();
    
    /**
     * 檢查是否可以進入此狀態
     *
     * @param context 執行上下文
     * @return 如果可以進入返回 true，否則返回 false
     */
    boolean canEnter(ExecutionContext context);
    
    /**
     * 進入此狀態時的處理邏輯
     *
     * @param context 執行上下文
     */
    void onEnter(ExecutionContext context);
    
    /**
     * 執行狀態相關的處理邏輯
     *
     * @param context 執行上下文
     * @return 處理結果
     */
    ProcessResult execute(ExecutionContext context);
    
    /**
     * 離開此狀態時的處理邏輯
     *
     * @param context 執行上下文
     */
    void onExit(ExecutionContext context);
    
    /**
     * 確定下一個狀態
     *
     * @param context 執行上下文
     * @param result 當前執行結果
     * @return 下一個狀態，如果沒有下一個狀態則返回 null
     */
    ProcessState determineNextState(ExecutionContext context, ProcessResult result);
}