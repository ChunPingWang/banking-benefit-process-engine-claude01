---
applyTo: '**/application/**/*.java'
---

# 應用層開發指引

## 套件結構
```
application/
├── service/                   # 應用服務
│   ├── FlowExecutionService.java
│   ├── FlowManagementService.java
│   └── MonitoringService.java
├── dto/                      # 數據傳輸物件
│   ├── request/             # 請求 DTO
│   │   ├── ExecuteFlowRequest.java
│   │   └── CreateFlowRequest.java
│   └── response/           # 回應 DTO
│       ├── FlowExecutionResponse.java
│       └── FlowDetailResponse.java
├── mapper/                  # DTO 轉換器
│   └── FlowMapper.java
├── exception/              # 應用層例外
│   ├── ApplicationException.java
│   └── ValidationException.java
└── config/                 # 應用層配置
    └── ApplicationConfig.java
```

## 開發準則

### 1. 應用服務準則
- 應用服務應該是薄的協調層
- 不包含業務邏輯
- 負責事務管理
- 處理安全性驗證
- 執行輸入驗證
- 進行日誌記錄
- 處理例外轉換

### 2. DTO 設計準則
- 根據 API 需求設計
- 與領域模型分離
- 包含驗證註解
- 提供詳細的文件說明
- 實作序列化介面
- 使用建造者模式

### 3. Mapper 設計準則
- 使用 MapStruct 框架
- 明確定義映射規則
- 處理空值檢查
- 提供雙向映射方法
- 避免循環依賴

### 4. 例外處理準則
- 定義應用層特定例外
- 統一例外處理機制
- 適當的錯誤代碼
- 詳細的錯誤訊息
- 安全的錯誤處理

### 5. 配置管理準則
- 環境特定配置
- 外部化配置
- 敏感資訊保護
- 配置驗證
- 預設值設定

### 6. 安全性準則
- 輸入資料驗證
- SQL 注入防護
- XSS 防護
- CSRF 防護
- 權限檢查

### 7. 效能考量
- 使用非同步處理
- 實作快取機制
- 批次處理優化
- 資源釋放管理
- 連接池配置

### 8. 監控與日誌
- 關鍵操作日誌
- 效能指標收集
- 健康檢查端點
- 監控預警機制
- 追蹤 ID 實作