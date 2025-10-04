package com.example.banking.benefit.domain.port.output;

import com.example.banking.benefit.domain.model.customer.CustomerContext;
import com.example.banking.benefit.domain.model.customer.CustomerProfile;
import java.util.Optional;
import java.util.List;
import java.util.Map;

/**
 * 客戶服務介面
 * Secondary Port - 輸出埠
 */
public interface CustomerServicePort {
    
    /**
     * 取得客戶資料
     *
     * @param customerId 客戶ID
     * @return 客戶資料
     */
    Optional<CustomerProfile> getCustomerProfile(String customerId);
    
    /**
     * 取得客戶執行上下文
     *
     * @param customerId 客戶ID
     * @return 客戶上下文
     */
    CustomerContext getCustomerContext(String customerId);
    
    /**
     * 驗證客戶資格
     *
     * @param customerId 客戶ID
     * @param benefitCode 權益代碼
     * @return 是否符合資格
     */
    boolean validateCustomerEligibility(String customerId, String benefitCode);
    
    /**
     * 更新客戶權益使用紀錄
     *
     * @param customerId 客戶ID
     * @param benefitCode 權益代碼
     * @param usageDetails 使用詳情
     */
    void updateBenefitUsage(String customerId, String benefitCode, Map<String, Object> usageDetails);
    
    /**
     * 查詢客戶權益使用歷史
     *
     * @param customerId 客戶ID
     * @param benefitCode 權益代碼
     * @return 使用歷史列表
     */
    List<Map<String, Object>> getBenefitUsageHistory(String customerId, String benefitCode);
    
    /**
     * 檢查客戶狀態
     *
     * @param customerId 客戶ID
     * @return 客戶狀態
     */
    String getCustomerStatus(String customerId);
    
    /**
     * 檢查客戶是否活躍
     *
     * @param customerId 客戶ID
     * @return 是否活躍
     */
    boolean isCustomerActive(String customerId);
}