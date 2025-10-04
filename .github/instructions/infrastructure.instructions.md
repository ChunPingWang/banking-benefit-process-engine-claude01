---
applyTo: '**/infrastructure/**/*.java'
---

# 基礎設施層開發指引

## 套件結構
```
infrastructure/
├── persistence/              # 持久化相關
│   ├── entity/             # 資料庫實體
│   │   ├── FlowEntity.java
│   │   └── NodeEntity.java
│   ├── repository/        # 資料庫存取
│   │   ├── JpaFlowRepository.java
│   │   └── JpaNodeRepository.java
│   └── mapper/           # ORM 映射
│       └── PersistenceMapper.java
├── adapter/              # 外部系統適配器
│   ├── notification/    # 通知服務
│   │   └── EmailNotificationAdapter.java
│   └── cache/          # 快取服務
│       └── RedisCacheAdapter.java
├── config/             # 基礎設施配置
│   ├── DatabaseConfig.java
│   ├── RedisConfig.java
│   └── SecurityConfig.java
└── security/          # 安全相關
    ├── JwtTokenProvider.java
    └── SecurityContextHelper.java
```

## 開發準則

### 1. 持久化層準則
- 使用 JPA 註解
- 實作審計欄位
- 定義索引策略
- 處理大型物件
- 優化查詢效能
- 實作樂觀鎖定
- 管理 EntityManager

### 2. 適配器準則
- 實作端口介面
- 處理外部系統異常
- 實作重試機制
- 設定超時處理
- 實作熔斷器
- 非同步處理
- 資源釋放

### 3. 安全性實作
- JWT 令牌管理
- 密碼雜湊處理
- 權限驗證實作
- Session 管理
- 安全標頭配置
- CORS 設定
- 稽核日誌

### 4. 快取實作
- 多級快取策略
- 快取失效機制
- 快取預熱
- 分散式鎖
- 資料一致性
- 效能監控

### 5. 資料庫設計
- 表格正規化
- 索引最佳化
- 分區策略
- 備份機制
- 效能監控
- 連接池管理

### 6. 外部服務整合
- 服務發現
- 負載平衡
- 健康檢查
- 服務降級
- API 版本管理
- 認證授權

### 7. 監控與追蹤
- 分散式追蹤
- 指標收集
- 日誌聚合
- 警報機制
- 效能分析

### 8. 效能優化
- 查詢優化
- 快取策略
- 連接池配置
- 資源限制
- 併發控制
- 非同步處理
Provide project context and coding guidelines that AI should follow when generating code, answering questions, or reviewing changes.