package com.example.banking.benefit.infrastructure.drools.adapter;

/**
 * Drools 規則類型枚舉
 */
public enum DroolsRuleType {
    /**
     * 客戶資格規則
     */
    CUSTOMER_ELIGIBILITY("customer-eligibility", "客戶資格檢核規則"),
    
    /**
     * 產品適用規則
     */
    PRODUCT_APPLICABILITY("product-applicability", "產品適用性規則"),
    
    /**
     * 優惠計算規則
     */
    BENEFIT_CALCULATION("benefit-calculation", "優惠計算規則"),
    
    /**
     * 流程控制規則
     */
    FLOW_CONTROL("flow-control", "流程控制規則"),
    
    /**
     * 通用決策規則
     */
    GENERAL_DECISION("general-decision", "通用決策規則");
    
    private final String code;
    private final String description;
    
    DroolsRuleType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根據代碼獲取規則類型
     */
    public static DroolsRuleType fromCode(String code) {
        for (DroolsRuleType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的規則類型代碼: " + code);
    }
}