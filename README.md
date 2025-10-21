# 客戶優惠流程引擎 (Banking Benefit Process Engine)

## 專案概述

客戶優惠流程引擎是一個基於 Java 21 和 Spring Boot 3 的高效能流程執行系統，採用六角形架構（Hexagonal Architecture）和領域驅動設計（DDD）方法論。系統主要用於執行銀行客戶優惠相關的業務流程，支援動態決策節點和處理節點的組合執行。

### 核心特色
- 🏗️ **六角形架構**：清楚分離業務邏輯與技術實作
- 🎯 **領域驅動設計**：以業務領域為核心的設計方法
- ⚡ **高效能執行**：支援並行處理與快取機制
- 🔄 **動態流程**：支援 Java 類別與 SpEL 表達式雙模式
- 🧠 **智能規則引擎**：整合 Drools 規則引擎，支援聲明式業務規則
- 📊 **完整監控**：提供執行日誌與效能監控
- 🔐 **安全可靠**：完整的錯誤處理與審計功能
- ✅ **高測試覆蓋率**：70 個單元測試，100% 通過率

### 最新更新 (2025-10-10)
- 🆕 **Drools 規則引擎整合**：完整整合 Drools 8.44.0.Final 規則引擎
- 🔧 **DroolsDecisionEvaluationService**：@Primary 服務實現無縫替換
- � **DRL 規則文件**：建立 customer-eligibility.drl 和 flow-control.drl
- 🏗️ **適配器模式**：DroolsRuleEngine 隔離規則引擎依賴
- ✅ **完整測試套件**：DroolsDecisionEvaluationServiceUnitTest 7 個測試案例
- 🎯 **聲明式規則**：支援業務規則的聲明式管理和執行
- 📊 **規則監控**：完整的規則執行日誌與效能監控

## 工作時程與人天估計

### 風險評估矩陣
| 風險類型 | 影響程度 | 發生機率 | 風險等級 | 額外時間預估 |
|---------|----------|----------|----------|--------------|
| 技術複雜度 | 高 | 高 | 極高 | +40% |
| 團隊協作 | 高 | 中 | 高 | +30% |
| 需求變更 | 中 | 高 | 高 | +35% |
| 外部依賴 | 中 | 中 | 中 | +25% |
| 環境配置 | 中 | 低 | 中 | +20% |

### 學習期 (35人天)
1. 領域知識學習 (15人天)
   - 銀行優惠業務流程分析 (6天)
     - 現有優惠機制深入研究
     - 各類優惠規則完整整理
     - 跨部門業務流程理解
     - 效能需求詳細分析
     - 異常情況處理研究
     - 法規遵循要求確認
   - 規則引擎深入研究 (5天)
     - Drools核心概念掌握
     - 規則編寫最佳實踐
     - 效能優化技巧
     - 版本控制策略
     - 熱更新機制研究
   - 流程引擎設計模式 (4天)
     - 命令模式應用場景分析
     - 狀態模式實現方式研究
     - 工廠模式整合策略
     - 設計模式組合運用
     - 擴展性考量

2. 技術框架學習 (20人天)
   - Java 21 新特性實戰 (5天)
     - Virtual Threads深入理解與實戰
     - Pattern Matching最佳實踐
     - Record Patterns應用場景
     - 效能優化與測試
     - 向下相容性處理
   - Spring Boot 3.x進階 (5天)
     - 新版特性完整掌握
     - 效能優化深入研究
     - 安全配置強化
     - 監控機制建立
     - 最佳實踐驗證
   - Drools規則引擎整合 (5天)
     - KieSession完整管理方案
     - 規則部署與版本控制
     - 動態規則載入機制
     - 效能調優與監控
     - 容錯機制設計
   - 架構與設計模式應用 (5天)
     - 六角形架構完整實踐
     - DDD戰術模式深入應用
     - CQRS與事件溯源
     - 微服務架構考量
     - 可測試性設計

### 開發期 (140人天，含Buffer)
1. 第一階段：領域模型與核心架構 (40人天)
   - 六角形架構搭建 (10天)
     - 詳細架構設計文檔 (2天)
     - Port介面完整定義 (3天)
     - Primary Adapters實現 (3天)
     - Secondary Adapters配置 (2天)
   - 領域模型設計 (15天)
     - 領域專家訪談與確認 (2天)
     - Flow聚合根完整設計 (4天)
     - Node實體詳細建模 (4天)
     - 值物件系統性定義 (3天)
     - 領域事件完整設計 (2天)
   - SOLID原則落實 (8天)
     - 架構評審與討論 (1天)
     - 接口分離原則實現 (2天)
     - 依賴反轉完整設計 (2天)
     - 程式碼品質優化 (2天)
     - 架構重構與調整 (1天)
   - 資料庫設計 (7天)
     - 資料模型評審 (1天)
     - 表結構完整設計 (2天)
     - 索引策略優化 (2天)
     - 效能測試與調優 (2天)

2. 第二階段：設計模式實作 (35天)
   - Command Pattern實作 (15天)
     - 架構與模式評審 (2天)
     - DecisionCommand介面設計 (3天)
     - 具體Command類別實作 (5天)
     - Command Factory完整實現 (3天)
     - 效能優化與測試 (2天)
   - State Pattern實作 (12天)
     - 狀態機制詳細設計 (2天)
     - ProcessState介面定義 (3天)
     - 狀態轉換完整邏輯 (4天)
     - State Factory實現 (3天)
   - Reflection機制 (8天)
     - 安全性評估 (1天)
     - 動態類別載入機制 (3天)
     - SpEL整合與優化 (2天)
     - 快取策略實現 (2天)

3. 第三階段：服務層實作 (25天)
   - 領域服務實作 (12天)
     - 服務設計評審 (1天)
     - Flow執行服務完整實現 (4天)
     - Decision評估服務優化 (4天)
     - Process處理服務實作 (3天)
   - 應用服務實作 (8天)
     - UseCase詳細設計 (2天)
     - UseCase完整實現 (4天)
     - DTO轉換與驗證 (2天)
   - 領域事件處理 (5天)
     - 事件機制設計評審 (1天)
     - 事件發布機制實現 (2天)
     - 事件訂閱處理完整化 (2天)

4. 第四階段：基礎設施層實作 (25天)
   - Repository實作 (10天)
     - 資料訪問策略評審 (1天)
     - Flow儲存庫實現 (3天)
     - Node儲存庫實作 (3天)
     - 查詢優化與效能調校 (3天)
   - 外部服務Adapter (8天)
     - 外部介接評估與設計 (1天)
     - 通知服務完整實現 (2天)
     - 日誌服務架構優化 (2天)
     - 快取服務強化 (2天)
     - 異常處理機制 (1天)
   - Primary Adapters (7天)
     - API設計評審 (1天)
     - REST API完整實現 (3天)
     - GraphQL支援優化 (2天)
     - 效能測試與調優 (1天)

5. 第五階段：管理介面與視覺化 (15天)
   - 流程設計器 (5天)
     - 視覺化編輯 (3天)
     - 驗證功能 (2天)
   - 配置管理介面 (4天)
     - 規則配置 (2天)
     - 版本管理 (2天)
   - 監控與日誌介面 (3天)
     - 效能監控 (2天)
     - 日誌查詢 (1天)

### 測試期 (65人天)
1. 單元測試 (25天)
   - 測試策略規劃 (2天)
     - 測試範圍定義
     - 測試標準制定
     - 測試工具選擇
   - Domain Model測試 (8天)
     - 聚合根完整測試
     - 實體行為測試
     - 值物件驗證測試
     - 邊界條件測試
   - Command/State測試 (8天)
     - Command完整測試
     - State轉換測試
     - 異常情況測試
     - 效能基準測試
   - Service測試 (7天)
     - 領域服務測試
     - 應用服務測試
     - 整合情境測試
     - 併發測試

2. 整合測試 (25天)
   - 整合測試策略 (2天)
     - 測試環境規劃
     - 測試數據準備
     - 自動化流程設計
   - API測試 (8天)
     - REST端點完整測試
     - GraphQL查詢測試
     - 安全性測試
     - 效能測試
   - 資料庫整合測試 (8天)
     - Repository完整測試
     - 交易一致性測試
     - 併發處理測試
     - 資料遷移測試
   - 外部服務整合測試 (7天)
     - 通知服務測試
     - 快取服務測試
     - 異常處理測試
     - 故障恢復測試

3. 架構與效能測試 (15天)
   - 架構測試規劃 (2天)
     - 測試目標定義
     - 測試工具選擇
     - 測試環境準備
   - 依賴關係驗證 (4天)
     - 層級依賴完整檢查
     - 接口實現驗證
     - 循環依賴檢測
     - 未使用依賴清理
   - 分層結構驗證 (4天)
     - 架構合規完整檢查
     - 程式碼品質深入分析
     - 技術債務評估
     - 重構建議產出
   - 效能基準測試 (5天)
     - 負載測試執行
     - 壓力測試分析
     - 效能瓶頸識別
     - 優化方案制定

### 風險評估與緩解
1. 技術風險 (高)
   - 風險：Java 21與Spring Boot 3.x新特性的學習曲線
   - 緩解：前期增加學習時間，建立概念驗證(POC)
   - Buffer：各階段預留20%緩衝時間

2. 整合風險 (中)
   - 風險：Drools規則引擎的整合複雜性
   - 緩解：提前進行原型驗證，準備降級方案
   - Buffer：整合測試期增加5天緩衝

3. 效能風險 (中)
   - 風險：高併發下的系統效能
   - 緩解：早期進行效能測試，識別瓶頸
   - 方案：準備效能優化方案清單

4. 需求風險 (低)
   - 風險：業務規則變更
   - 緩解：採用靈活的設計模式，提高可擴展性
   - 策略：規則引擎設計預留擴展空間

### 時程安排最佳實踐
1. 並行開發策略
   - 領域模型設計與資料庫設計可並行
   - 單元測試與功能開發同步進行
   - UI介面開發與後端服務可並行

2. 關鍵路徑管理
   - 優先完成核心領域模型
   - 早期進行效能測試
   - 持續整合與部署

3. 敏捷迭代計劃
   - 每2週一次迭代
   - 每日站立會議
   - 週期性代碼審查

### 總計: 240人天 (含Buffer)
- 學習期: 35人天 (15%)
- 開發期: 140人天 (58%)
- 測試期: 65人天 (27%)

### 風險緩解策略

1. 技術風險 (極高)
   - 預先技術驗證 (POC) +15天
   - 架構評審與專家諮詢 +5天
   - 定期技術回顧與調整 +5天
   - 團隊技術培訓 +5天

2. 團隊協作風險 (高)
   - 詳細設計文檔 +7天
   - 代碼審查流程 +5天
   - 團隊溝通機制 +3天
   - 知識共享會議 +3天

3. 需求變更風險 (高)
   - 需求凍結期設定 +5天
   - 變更影響評估 +5天
   - 彈性設計buffer +10天
   - 迭代計畫調整 +5天

4. 外部依賴風險 (中)
   - 外部系統模擬 +5天
   - 降級方案準備 +3天
   - 整合測試強化 +5天
   - 監控告警機制 +2天

5. 效能風險 (中)
   - 效能基準建立 +3天
   - 效能測試自動化 +4天
   - 效能優化預留 +5天
   - 擴展性設計 +3天

### 額外考量因素

1. 團隊建設
   - 新技術學習曲線陡峭
   - 團隊技能不均衡
   - 跨團隊協作複雜
   - 需要持續技術支持

2. 品質保證
   - 嚴格的程式碼審查
   - 完整的測試覆蓋
   - 持續的架構優化
   - 定期的安全審核

3. 維護與支持
   - 文檔完整性要求
   - 知識轉移需求
   - 運維支持準備
   - 問題排查能力

### 人力資源配置
1. 開發團隊
   - 高級開發者 x 1 (架構設計，技術決策)
   - 中級開發者 x 2 (功能開發，單元測試)
   - 初級開發者 x 1 (UI開發，文檔編寫)

2. 支援團隊
   - 測試工程師 x 1 (測試用例，自動化測試)
   - DevOps工程師 x 0.5 (部署，監控配置)
   - 業務分析師 x 0.5 (需求分析，規則設計)

## 技術規格

### 核心技術棧
- **程式語言**: Java 21
- **框架**: Spring Boot 3.2.0
- **建構工具**: Gradle 8.x
- **測試框架**: JUnit 5 + Mockito
- **測試覆蓋率**: 70 個測試，100% 通過
- **資料庫**: H2 (開發) / PostgreSQL (正式)
- **快取**: Caffeine
- **規則引擎**: Drools 8.44.0.Final
- **表達式引擎**: Spring Expression Language (SpEL) + Drools Rules
- **文件**: OpenAPI (Swagger)

### 效能指標
- 單一流程執行時間 < 500ms
- 系統整體 TPS > 1000
- Decision評估時間 < 50ms
- State處理時間 < 200ms
- 資料庫查詢時間 < 100ms

## 專案架構

### 六角形架構設計

```
┌─────────────────────────────────────────────────────────────┐
│                    Adapters (外層)                          │
├─────────────────────────────────────────────────────────────┤
│  Primary Adapters (驅動端)  │  Secondary Adapters (被驅動端) │
│  ├─ REST Controllers       │  ├─ Database Repositories      │
│  ├─ GraphQL Endpoints      │  ├─ External Service Clients   │
│  └─ Message Consumers       │  └─ Notification Services      │
├─────────────────────────────────────────────────────────────┤
│                    Application Layer (應用層)                │
│  ├─ Application Services    │  ├─ DTOs                       │
│  ├─ Use Cases              │  └─ Mappers                    │
├─────────────────────────────────────────────────────────────┤
│                    Domain Layer (領域層/核心)                │
│  ├─ Aggregates             │  ├─ Domain Services            │
│  ├─ Entities               │  ├─ Factories                  │
│  ├─ Value Objects          │  └─ Domain Events              │
│  └─ Ports (介面定義)                                        │
└─────────────────────────────────────────────────────────────┘
```

### 套件結構

```
src/main/java/com/example/banking/benefit/
├── BankingBenefitProcessEngineApplication.java    # 應用程式啟動點
├── application/                                   # 應用層
│   ├── service/                                  # 應用服務
│   ├── dto/                                      # 數據傳輸物件
│   │   ├── request/                             # 請求 DTO
│   │   └── response/                            # 回應 DTO
│   └── mapper/                                   # DTO 轉換器
├── domain/                                       # 領域層（核心）
│   ├── model/                                   # 領域模型
│   │   ├── flow/                               # 流程聚合
│   │   │   ├── aggregate/                      # 聚合根
│   │   │   ├── entity/                         # 實體
│   │   │   ├── valueobject/                    # 值物件
│   │   │   └── event/                          # 領域事件
│   │   ├── node/                               # 節點模型
│   │   │   ├── DecisionNode.java              # 決策節點
│   │   │   └── ProcessNode.java               # 處理節點
│   │   ├── command/                            # 命令模式
│   │   ├── state/                              # 狀態模式
│   │   ├── common/                             # 共用模型
│   │   ├── customer/                           # 客戶模型
│   │   └── result/                             # 執行結果
│   ├── service/                                # 領域服務
│   │   ├── FlowExecutionService.java          # 流程執行服務
│   │   ├── DecisionEvaluationService.java     # 決策評估服務
│   │   └── LogicCompositionService.java       # 邏輯組合服務
│   ├── port/                                   # 端口（六角形架構）
│   │   ├── input/                             # 輸入端口
│   │   └── output/                            # 輸出端口
│   ├── repository/                             # 儲存庫介面
│   └── exception/                              # 領域例外
├── infrastructure/                              # 基礎設施層
│   ├── config/                                # 配置
│   ├── persistence/                           # 持久化實作
│   ├── adapter/                               # 外部服務適配器
│   ├── drools/                               # Drools 規則引擎相關
│   │   ├── config/                           # Drools 配置
│   │   │   └── DroolsConfig.java            # KieServices 配置
│   │   ├── adapter/                          # Drools 適配器
│   │   │   └── DroolsRuleEngine.java        # 規則執行引擎
│   │   └── service/                          # Drools 服務
│   │       └── DroolsDecisionEvaluationService.java  # Drools 決策評估服務
│   └── cache/                                 # 快取實作
└── config/                                     # 應用配置
    └── ApplicationConfig.java

src/main/resources/
├── rules/                                    # Drools 規則文件
│   ├── customer/                            # 客戶相關規則
│   │   └── customer-eligibility.drl        # 客戶資格規則
│   └── flow/                                # 流程控制規則
│       └── flow-control.drl                 # 流程控制規則
├── application.yml                          # 應用配置
├── logback-spring.xml                       # 日誌配置
└── schema.sql                               # 資料庫 Schema
```

## 核心模型設計

### 類別圖

```
┌─────────────────────────────────────────────────────────────┐
│                        Flow (聚合根)                        │
├─────────────────────────────────────────────────────────────┤
│ - flowId: FlowId                                           │
│ - flowName: String                                         │
│ - description: String                                      │
│ - version: Version                                         │
│ - status: FlowStatus                                       │
│ - startNodeId: String                                      │
├─────────────────────────────────────────────────────────────┤
│ + execute(): ExecutionResult                               │
│ + activate(): void                                         │
│ + deactivate(): void                                       │
│ + addDecisionNode(): void                                  │
│ + addProcessNode(): void                                   │
│ + getNextNode(): Node                                      │
│ + findNodeById(): Node                                     │
└─────────────────────────────────────────────────────────────┘
                                │
                   ┌────────────┼────────────┐
                   │            │            │
                   ▼            ▼            ▼
    ┌─────────────────┐  ┌─────────────┐  ┌─────────────┐
    │   DecisionNode  │  │ ProcessNode │  │    Node     │
    │    (實體)       │  │   (實體)    │  │  (抽象)     │
    ├─────────────────┤  ├─────────────┤  ├─────────────┤
    │ - nodeId        │  │ - nodeId    │  │ - nodeId    │
    │ - flowId        │  │ - flowId    │  │ - flowId    │
    │ - nodeName      │  │ - nodeName  │  │ - nodeName  │
    │ - decisionType  │  │ - stateName │  │ - nodeOrder │
    │ - implClass     │  │ - implClass │  ├─────────────┤
    │ - spelExpr      │  │ - spelExpr  │  │ + getNodeId │
    ├─────────────────┤  ├─────────────┤  │ + getOrder  │
    │ + evaluate()    │  │ + execute() │  └─────────────┘
    └─────────────────┘  └─────────────┘          ▲
                                                   │
                                        ┌─────────┴─────────┐
                                        │                   │
                                 DecisionNode        ProcessNode

    ┌─────────────────┐  ┌─────────────┐  ┌─────────────────┐
    │     FlowId      │  │   Version   │  │ ExecutionResult │
    │   (值物件)      │  │  (值物件)   │  │    (結果)       │
    ├─────────────────┤  ├─────────────┤  ├─────────────────┤
    │ - value: String │  │ - value     │  │ - flowId        │
    ├─────────────────┤  ├─────────────┤  │ - executionId   │
    │ + of(): FlowId  │  │ + of()      │  │ - status        │
    │ + getValue()    │  │ + increment │  │ - errorMessage  │
    └─────────────────┘  └─────────────┘  └─────────────────┘

領域服務接口:
• FlowExecutionService: 流程執行服務
• DecisionEvaluationService: 決策評估服務  
• LogicCompositionService: 邏輯組合服務
```

### 狀態圖

```
流程執行狀態轉換圖:

    [開始]
       │
       ▼
   ┌─────────┐     activate     ┌─────────┐
   │  DRAFT  │ ───────────────► │ ACTIVE  │
   │ (草稿)  │                  │ (啟用)  │
   └─────────┘                  └─────────┘
       │                            │    │
       │ delete                     │    │ deactivate
       ▼                            │    ▼
   ┌─────────┐                      │ ┌─────────┐
   │ DELETED │                      │ │INACTIVE │
   │ (已刪除) │                      │ │ (停用)  │
   └─────────┘                      │ └─────────┘
       │                            │    │    ▲
       ▼                            │    │    │ reactivate
    [結束]                          │    │ delete
                                    │    ▼    │
                               start│ ┌─────────┐
                                    │ │ DELETED │
                                    │ └─────────┘
                                    ▼    │
                               ┌─────────┐▼
                               │IN_PROG  │[結束]
                               │(執行中) │
                               └─────────┘
                          ┌─────────┼─────────┐
                    pause │         │ error   │ terminate
                          ▼         ▼         ▼
                    ┌─────────┐┌─────────┐┌─────────┐
                    │ PAUSED  ││ FAILURE ││TERMINAT │
                    │ (暫停)  ││ (失敗)  ││ (終止)  │
                    └─────────┘└─────────┘└─────────┘
                          │         │         │
                    resume│         │         │
                          ▼         ▼         ▼
                    ┌─────────┐   [結束]    [結束]
                    │IN_PROG  │
                    └─────────┘
                          │
                  complete│
                          ▼
                    ┌─────────┐
                    │ SUCCESS │
                    │ (成功)  │
                    └─────────┘
                          │
                          ▼
                       [結束]
```

## 流程執行循序圖

### 基本流程執行

```
流程執行循序圖:

Client      Controller   AppService   DomainService   NodeExecutor   Repository   Database
  │             │            │              │              │             │           │
  │ POST execute│            │              │              │             │           │
  │────────────►│            │              │              │             │           │
  │             │executeFlow │              │              │             │           │
  │             │───────────►│              │              │             │           │
  │             │            │   findById   │              │             │           │
  │             │            │─────────────────────────────────────────►│           │
  │             │            │              │              │             │  SELECT   │
  │             │            │              │              │             │──────────►│
  │             │            │              │              │             │   data    │
  │             │            │              │              │             │◄──────────│
  │             │            │    Flow      │              │             │           │
  │             │            │◄─────────────────────────────────────────│           │
  │             │            │   execute    │              │             │           │
  │             │            │─────────────►│              │             │           │
  │             │            │              │   execute    │             │           │
  │             │            │              │─────────────►│             │           │
  │             │            │              │    result    │             │           │
  │             │            │              │◄─────────────│             │           │
  │             │            │ExecutionResult              │             │           │
  │             │            │◄─────────────│              │             │           │
  │             │            │   saveLog    │              │             │           │
  │             │            │─────────────────────────────────────────►│           │
  │             │   result   │              │              │             │           │
  │             │◄───────────│              │              │             │           │
  │  response   │            │              │              │             │           │
  │◄────────────│            │              │              │             │           │
  │             │            │              │              │             │           │

執行流程說明:
1. 客戶端發送 POST 請求執行流程
2. Controller 呼叫 AppService 執行流程
3. AppService 透過 Repository 查詢流程定義
4. Repository 從資料庫取得流程資料
5. AppService 呼叫 DomainService 執行業務邏輯
6. DomainService 透過 NodeExecutor 執行各個節點
7. 執行完成後儲存執行日誌
8. 返回執行結果給客戶端
```

### 決策節點評估

```
決策節點評估循序圖:

DomainService   DecisionExecutor   EvaluationService   SpelExecutor   JavaExecutor
      │               │                    │                │             │
      │    execute    │                    │                │             │
      │──────────────►│                    │                │             │
      │               │     evaluate       │                │             │
      │               │───────────────────►│                │             │
      │               │                    │                │             │
      │               │                    │ ┌─────────────┐│             │
      │               │                    │ │ 判斷類型    ││             │
      │               │                    │ └─────────────┘│             │
      │               │                    │                │             │
      │               │              ┌─────┴─────┐          │             │
      │               │              │ SpEL?     │          │             │
      │               │              └─────┬─────┘          │             │
      │               │                    │ YES            │             │
      │               │                    │   evaluate     │             │
      │               │                    │───────────────►│             │
      │               │                    │    result      │             │
      │               │                    │◄───────────────│             │
      │               │                    │                │             │
      │               │                    │ NO (Java)      │             │
      │               │                    │                │  execute    │
      │               │                    │────────────────────────────►│
      │               │                    │                │   result    │
      │               │                    │◄────────────────────────────│
      │               │      result        │                │             │
      │               │◄───────────────────│                │             │
      │ ExecutionResult                    │                │             │
      │◄──────────────│                    │                │             │
      │               │                    │                │             │

決策評估流程說明:
1. DomainService 呼叫 DecisionExecutor 執行決策節點
2. DecisionExecutor 呼叫 EvaluationService 進行評估
3. EvaluationService 根據節點類型選擇執行器:
   - SpEL 表達式: 使用 SpelExecutor 解析並執行
   - Java 類別: 使用 JavaExecutor 動態載入並執行
4. 返回評估結果給上層服務
```

## 資料庫設計 (ER Diagram)

```
資料庫實體關係圖 (ER Diagram):

┌─────────────────────────────────────────────────────────────────────────┐
│                           FLOW_DEFINITION                              │
├─────────────────────────────────────────────────────────────────────────┤
│ • flow_id (PK)           VARCHAR(50)    - 流程ID                       │
│ • flow_name              VARCHAR(100)   - 流程名稱                     │
│ • flow_description       CLOB          - 流程描述                      │
│ • version                VARCHAR(20)    - 版本號                       │
│ • status                 VARCHAR(20)    - 狀態                         │
│ • start_node_id          VARCHAR(50)    - 起始節點ID                   │
│ • created_time           TIMESTAMP      - 建立時間                     │
│ • updated_time           TIMESTAMP      - 更新時間                     │
│ • created_by             VARCHAR(50)    - 建立者                       │
└─────────────────────────────────────────────────────────────────────────┘
                                      │
                   ┌──────────────────┼──────────────────┐
                   │                  │                  │
                   ▼                  ▼                  ▼

┌─────────────────────────────┐ ┌─────────────────────────────┐ ┌──────────────────────────┐
│       DECISION_NODE         │ │       PROCESS_NODE          │ │     EXECUTION_LOG        │
├─────────────────────────────┤ ├─────────────────────────────┤ ├──────────────────────────┤
│ • node_id (PK)              │ │ • node_id (PK)              │ │ • log_id (PK)            │
│ • flow_id (FK)              │ │ • flow_id (FK)              │ │ • flow_id (FK)           │
│ • node_name                 │ │ • node_name                 │ │ • customer_id            │
│ • node_description          │ │ • node_description          │ │ • execution_time         │
│ • decision_type             │ │ • process_type              │ │ • node_id                │
│ • implementation_class      │ │ • implementation_class      │ │ • node_type              │
│ • spel_expression          │ │ • spel_expression          │ │ • execution_result       │
│ • node_order               │ │ • state_name               │ │ • result_data            │
│ • created_time             │ │ • node_order               │ │ • error_message          │
│ • updated_time             │ │ • created_time             │ │ • execution_duration_ms  │
└─────────────────────────────┘ │ • updated_time             │ └──────────────────────────┘
                                └─────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                         NODE_RELATION                                  │
├─────────────────────────────────────────────────────────────────────────┤
│ • relation_id (PK)       VARCHAR(50)    - 關係ID                       │
│ • flow_id (FK)           VARCHAR(50)    - 流程ID                       │
│ • source_node_id         VARCHAR(50)    - 來源節點ID                   │
│ • source_node_type       VARCHAR(20)    - 來源節點類型                 │
│ • target_node_id         VARCHAR(50)    - 目標節點ID                   │
│ • target_node_type       VARCHAR(20)    - 目標節點類型                 │
│ • relation_type          VARCHAR(20)    - 關係類型                     │
│ • logic_operator         VARCHAR(10)    - 邏輯運算子                   │
│ • condition_expression   CLOB          - 條件表達式                    │
│ • created_time           TIMESTAMP      - 建立時間                     │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────┐    ┌─────────────────────────────────────┐
│     DECISION_GROUP          │    │      DECISION_GROUP_MEMBER          │
├─────────────────────────────┤    ├─────────────────────────────────────┤
│ • group_id (PK)             │◄───┤ • member_id (PK)                    │
│ • flow_id (FK)              │    │ • group_id (FK)                     │
│ • group_name                │    │ • decision_node_id (FK)             │
│ • logic_operator            │    │ • member_order                      │
│ • target_process_id (FK)    │    │ • created_time                      │
│ • parent_group_id (FK)      │    └─────────────────────────────────────┘
│ • created_time              │
└─────────────────────────────┘

主要表格說明:
• FLOW_DEFINITION: 流程定義主表，儲存流程基本資訊
• DECISION_NODE: 決策節點表，支援 Java 類別與 SpEL 表達式
• PROCESS_NODE: 處理節點表，支援狀態模式與動態執行
• NODE_RELATION: 節點關係表，定義節點間的轉換條件
• DECISION_GROUP: 決策群組表，支援複雜邏輯組合 (AND/OR)
• DECISION_GROUP_MEMBER: 群組成員表，管理群組內的決策節點
• EXECUTION_LOG: 執行日誌表，記錄所有執行歷程與效能資料
```

### 資料表說明

#### 核心表格
- **flow_definition**: 流程定義主表，儲存流程基本資訊與版本控制
- **decision_node**: 決策節點表，支援 Java 類別與 SpEL 表達式
- **process_node**: 處理節點表，支援狀態模式與動態執行

#### 關係表格
- **node_relation**: 節點間關係定義，支援條件式轉換
- **decision_group**: 決策群組，支援複雜邏輯組合（AND/OR）
- **decision_group_member**: 群組成員關係，維護執行順序

#### 日誌表格
- **execution_log**: 執行日誌，記錄所有執行歷程與效能資料

## API 設計

### RESTful API 端點

```yaml
# 流程管理 API
GET    /api/v1/flows                    # 查詢所有流程
GET    /api/v1/flows/{flowId}          # 查詢特定流程
POST   /api/v1/flows                   # 建立新流程
PUT    /api/v1/flows/{flowId}          # 更新流程
DELETE /api/v1/flows/{flowId}          # 刪除流程

# 流程執行 API
POST   /api/v1/flows/{flowId}/execute  # 執行流程
POST   /api/v1/flows/{flowId}/pause    # 暫停流程
POST   /api/v1/flows/{flowId}/resume   # 繼續流程
POST   /api/v1/flows/{flowId}/terminate # 終止流程

# 監控與日誌 API
GET    /api/v1/executions              # 查詢執行記錄
GET    /api/v1/executions/{executionId} # 查詢特定執行記錄
GET    /api/v1/statistics              # 查詢統計資訊
GET    /api/v1/health                  # 健康檢查

# 節點管理 API
GET    /api/v1/flows/{flowId}/nodes    # 查詢流程節點
POST   /api/v1/flows/{flowId}/nodes    # 新增節點
PUT    /api/v1/flows/{flowId}/nodes/{nodeId} # 更新節點
DELETE /api/v1/flows/{flowId}/nodes/{nodeId} # 刪除節點
```

### 標準回應格式

```json
{
  "status": "SUCCESS",
  "data": {
    "flowId": "FLOW_001",
    "executionId": "EXEC_123456",
    "executionStatus": "SUCCESS",
    "duration": 234,
    "resultData": {
      "decision": true,
      "processedAmount": 1000.00
    }
  },
  "message": "流程執行成功",
  "timestamp": "2025-10-04T10:00:00Z"
}
```

## 設計模式應用

### 1. 六角形架構 (Hexagonal Architecture)
- **目的**: 分離業務邏輯與技術實作
- **實作**: Port & Adapter 模式
- **範例**: `FlowExecutionUseCase` (輸入埠) + `FlowRepository` (輸出埠)

### 2. 命令模式 (Command Pattern)
- **目的**: 封裝決策邏輯為可執行命令
- **實作**: `DecisionCommand` 介面與具體實作
- **範例**: `JavaClassDecisionCommand`, `SpelDecisionCommand`

### 3. 狀態模式 (State Pattern)
- **目的**: 管理處理節點的不同狀態
- **實作**: `ProcessState` 介面與狀態實作
- **範例**: `InitialState`, `ProcessingState`, `CompletedState`

### 4. 工廠模式 (Factory Pattern)
- **目的**: 動態建立節點執行器
- **實作**: `NodeExecutorFactory`
- **範例**: 根據節點類型建立對應執行器

### 5. 策略模式 (Strategy Pattern)
- **目的**: 支援不同的表達式評估策略
- **實作**: `ExpressionEvaluator` 介面
- **範例**: `SpelExpressionEvaluator`, `JavaClassEvaluator`

## 開發指南

### 環境需求
- JDK 21 或以上
- Gradle 8.x
- IDE: IntelliJ IDEA 或 Eclipse
- 資料庫: H2 (開發) / PostgreSQL 13+ (正式)

## 使用指南

### 安裝與設定

1. **環境準備**
```bash
# 確認 JDK 版本
java -version  # 需要 JDK 21 或以上

# 複製專案
git clone <repository-url>
cd banking-benefit-process-engine

# 建構專案
./gradlew build

# 執行測試
./gradlew test
```

2. **資料庫設定**
```bash
# 開發環境 (H2)
spring.datasource.url=jdbc:h2:mem:benefitdb
spring.datasource.username=sa
spring.datasource.password=

# 正式環境 (PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/benefitdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **啟動應用程式**
```bash
# 開發模式啟動
./gradlew bootRun

# 生產模式啟動
java -jar build/libs/banking-benefit-process-engine-1.0.0.jar
```

4. **檢查服務狀態**
```bash
# 查看 API 文件
open http://localhost:8080/swagger-ui.html

# 查看 H2 資料庫控制台 (僅開發環境)
open http://localhost:8080/h2-console
```

### 基本使用流程

1. **建立流程**
```http
POST /api/v1/flows
Content-Type: application/json

{
  "flowName": "客戶權益檢核",
  "description": "檢查客戶是否符合權益資格",
  "version": "1.0"
}
```

2. **新增決策節點**
```http
POST /api/v1/flows/{flowId}/nodes
Content-Type: application/json

{
  "nodeType": "DECISION",
  "nodeName": "年齡檢核",
  "decisionType": "SPEL",
  "spelExpression": "#customerData.age >= 20"
}
```

3. **新增處理節點**
```http
POST /api/v1/flows/{flowId}/nodes
Content-Type: application/json

{
  "nodeType": "PROCESS",
  "nodeName": "發送權益通知",
  "implementationClass": "com.example.NotificationProcessor"
}
```

4. **執行流程**
```http
POST /api/v1/flows/{flowId}/execute
Content-Type: application/json

{
  "customerId": "CUST_001",
  "customerData": {
    "age": 25,
    "accountBalance": 100000,
    "membershipLevel": "GOLD"
  }
}
```

### 實作指南

#### 1. 新增決策節點 (Decision Node)

決策節點用於執行條件判斷，支援兩種實作方式：

1. **使用 SpEL 表達式**
```java
// 建立基於 SpEL 的決策節點
DecisionNode ageCheck = DecisionNode.create()
    .flowId("FLOW_001")
    .nodeId("D001")
    .name("年齡檢核")
    .description("檢查客戶年齡是否符合資格")
    .spelExpression("#customerData.get('age') >= 20")
    .nodeOrder(1)
    .build();

// SpEL 表達式範例
String[] spelExamples = {
    "#customerData.get('age') >= 20",                          // 基本比較
    "#customerData.get('vip') == true",                        // 布林判斷
    "#customerData.get('balance') >= 50000",                   // 數值比較
    "T(java.time.LocalDate).now().getYear() - #customerData.get('birthYear') >= 20"  // 日期計算
};
```

2. **使用 Java 類別**
```java
// 建立基於 Java 類別的決策節點
DecisionNode creditCheck = DecisionNode.create()
    .flowId("FLOW_001")
    .nodeId("D002")
    .name("信用評分檢核")
    .description("檢查客戶信用評分")
    .implementationClass("com.example.banking.benefit.domain.decision.CreditScoreDecision")
    .nodeOrder(2)
    .build();

// 實作 DecisionCommand 介面
@Component
public class CreditScoreDecision implements DecisionCommand {
    @Override
    public boolean evaluate(ExecutionContext context) {
        CustomerData customerData = context.getCustomerData();
        Integer creditScore = customerData.get("creditScore", Integer.class);
        return creditScore >= 700;
    }
}
```

#### 2. 新增處理節點 (Process Node)

處理節點用於執行業務邏輯，同樣支援兩種實作方式：

1. **使用 SpEL 表達式**
```java
// 建立基於 SpEL 的處理節點
ProcessNode updatePoints = ProcessNode.create()
    .flowId("FLOW_001")
    .nodeId("P001")
    .name("更新積分")
    .description("增加客戶積分")
    .spelExpression("#customerData.attributes['points'] += 100")
    .nodeOrder(1)
    .stateName("PROCESSING")
    .build();

// SpEL 處理範例
String[] processingExamples = {
    "#customerData.attributes['points'] += 100",              // 更新數值
    "#customerData.attributes['level'] = 'GOLD'",            // 設定狀態
    "#customerData.attributes['lastUpdate'] = T(java.time.LocalDateTime).now()"  // 更新時間
};
```

2. **使用 Java 類別**
```java
// 建立基於 Java 類別的處理節點
ProcessNode sendNotification = ProcessNode.create()
    .flowId("FLOW_001")
    .nodeId("P002")
    .name("發送通知")
    .description("發送權益啟用通知")
    .implementationClass("com.example.banking.benefit.domain.process.NotificationProcessor")
    .nodeOrder(2)
    .stateName("NOTIFYING")
    .build();

// 實作處理邏輯
@Component
public class NotificationProcessor implements ProcessCommand {
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public ExecutionResult execute(ExecutionContext context) {
        String customerId = context.getCustomerId();
        String message = String.format("尊敬的客戶 %s，您的權益已啟用", customerId);
        notificationService.sendNotification(customerId, message);
        return ExecutionResult.success();
    }
}
```

#### 3. 設定節點關係

節點之間的關係定義了流程的執行順序：

```java
// 建立流程
Flow flow = Flow.create(FlowId.generate(), "信用卡權益啟用流程", "自動檢核並啟用信用卡權益", Version.of("1.0"));

// 添加節點
flow.addDecisionNode(ageCheck);
flow.addDecisionNode(creditCheck);
flow.addProcessNode(updatePoints);
flow.addProcessNode(sendNotification);

// 設定節點關係
flow.addRelation(NodeRelation.create()
    .from(ageCheck)
    .to(creditCheck)
    .condition(true));

flow.addRelation(NodeRelation.create()
    .from(creditCheck)
    .to(updatePoints)
    .condition(true));

flow.addRelation(NodeRelation.create()
    .from(updatePoints)
    .to(sendNotification)
    .condition(true));

// 設定起始節點
flow.setStartNode(ageCheck.getNodeId());

// 啟用流程
flow.activate();
```

#### 4. 執行流程

```java
// 準備執行環境
Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
attributes.put("age", CustomerAttribute.of(25, Integer.class));
attributes.put("creditScore", CustomerAttribute.of(750, Integer.class));
attributes.put("points", CustomerAttribute.of(0, Integer.class));
CustomerData customerData = CustomerData.create("CUST_001", attributes);

// 建立執行上下文
ExecutionContext context = new DefaultExecutionContext();
context.setFlowId(flow.getFlowId());
context.setCustomerId("CUST_001");
context.setCustomerData(customerData);

// 執行流程
ExecutionResult result = flowExecutionService.execute(flow, context);

// 檢查執行結果
if (result.getStatus().isSuccess()) {
    System.out.println("流程執行成功");
    System.out.println("客戶積分: " + customerData.get("points", Integer.class));
} else {
    System.out.println("流程執行失敗: " + result.getErrorMessage());
}
```

### 常見使用情境

1. **權益資格檢核流程**
```java
// 1. 建立決策節點
DecisionNode ageCheck = DecisionNode.create()
    .name("年齡檢核")
    .spelExpression("#customerData.get('age') >= 20");

DecisionNode balanceCheck = DecisionNode.create()
    .name("帳戶餘額檢核")
    .spelExpression("#customerData.get('balance') >= 50000");

// 2. 建立處理節點
ProcessNode notifyCustomer = ProcessNode.create()
    .name("發送通知")
    .implementationClass("com.example.NotificationProcessor");

// 3. 設定節點關係
flow.addDecisionNode(ageCheck);
flow.addDecisionNode(balanceCheck);
flow.addProcessNode(notifyCustomer);
flow.addRelation(NodeRelation.create()
    .from(ageCheck)
    .to(balanceCheck)
    .condition(true));
```

2. **動態決策邏輯**
```java
// 使用 SpEL 表達式
spelExpression = "#customerData.membershipLevel == 'GOLD' " +
                "and #customerData.transactionCount > 10";

// 使用 Java 類別
@Component
public class CustomDecisionLogic implements DecisionCommand {
    @Override
    public boolean evaluate(ExecutionContext context) {
        CustomerData customer = context.getCustomerData();
        return customer.getMembershipLevel() == MembershipLevel.GOLD &&
               customer.getTransactionCount() > 10;
    }
}
```

3. **Drools 規則開發**

系統現已整合 Drools 規則引擎，支援聲明式業務規則管理：

**規則文件結構**
```
src/main/resources/rules/
├── customer/                        # 客戶相關規則
│   └── customer-eligibility.drl     # 客戶資格規則
└── flow/                            # 流程控制規則
    └── flow-control.drl             # 流程控制規則
```

**DRL 規則範例**
```drl
package com.example.banking.benefit.rules.customer

import com.example.banking.benefit.domain.model.common.CustomerData
import com.example.banking.benefit.domain.model.common.BankingContext

rule "客戶年齡驗證"
when
    $customer: CustomerData(get("age", Integer.class) >= 20, get("age", Integer.class) <= 65)
    $context: BankingContext()
then
    System.out.println("客戶年齡驗證通過: " + $customer.get("age", Integer.class));
    $context.setDecisionResult(true);
end
```

**DroolsDecisionEvaluationService 使用**
```java
@Service
@Primary
public class DroolsDecisionEvaluationService implements DecisionEvaluationService {
    
    @Override
    public boolean evaluate(DecisionNode decisionNode, BaseExecutionContext context) {
        // 透過 Drools 規則引擎評估決策
        return droolsRuleEngine.executeDecisionRules(decisionNode.getNodeId(), context);
    }
}
```

4. **監控流程執行**
```http
# 查詢執行統計
GET /api/v1/statistics

# 查詢特定流程執行記錄
GET /api/v1/executions?flowId={flowId}

# 查看詳細執行日誌
GET /api/v1/executions/{executionId}/logs
```

### 開發規範

#### 程式碼風格
- 遵循 Google Java Style Guide
- 使用 Lombok 減少 boilerplate code
- 完整的 JavaDoc 文件
- 單元測試覆蓋率 > 80%

#### 分支策略
- `main`: 正式環境代碼
- `develop`: 開發環境代碼
- `feature/*`: 功能開發分支
- `hotfix/*`: 緊急修復分支

#### 提交訊息格式
```
<type>(<scope>): <subject>

<body>

<footer>
```

範例:
```
feat(flow): add decision node evaluation

- Implement SpEL expression evaluator
- Add Java class executor
- Support dynamic decision logic

Closes #123
```

## 測試狀態

### 測試覆蓋率
- **總測試數**: 70 個
- **通過率**: 100%
- **測試類別**:
  - ✅ SpelExpressionEvaluatorTest (7/7)
  - ✅ JavaClassEvaluatorTest (5/5)
  - ✅ BaseDecisionEvaluationServiceTest (6/6)
  - ✅ BaseLogicCompositionServiceTest (8/8)
  - ✅ BaseProcessExecutionServiceTest (5/5)
  - ✅ DecisionNodeExecutorTest (6/6)
  - ✅ ProcessNodeExecutorTest (7/7)
  - ✅ SimpleFlowExecutionServiceImplTest (2/2)
  - ✅ FlowExecutionControllerTest (17/17)
  - ✅ DroolsDecisionEvaluationServiceUnitTest (7/7)

### 最近修復
- 🆕 **Drools 規則引擎整合**：完整的 Drools 決策評估服務測試
- ✅ **規則執行驗證**：7 個 Drools 測試案例，包含規則觸發驗證
- 🔧 **獨立單元測試**：避免 Spring Boot 上下文複雜性的測試設計
- 📊 **規則監控測試**：驗證 KieContainer 創建和規則編譯過程
- 🎯 **業務規則測試**：客戶資格和流程控制規則的完整測試覆蓋

## 測試策略

### 測試金字塔
1. **單元測試** (70%): 測試個別類別與方法
2. **整合測試** (20%): 測試模組間互動
3. **端到端測試** (10%): 測試完整流程

#### 測試類型
- **領域測試**: 驗證業務邏輯正確性
- **架構測試**: 驗證分層與依賴關係
- **效能測試**: 驗證效能指標達成
- **契約測試**: 驗證 API 契約一致性

## 部署與運維

### 部署環境

#### 開發環境
- 內嵌 H2 資料庫
- 記憶體快取
- Debug 日誌等級

#### 測試環境
- PostgreSQL 資料庫
- Redis 快取
- 完整監控

#### 正式環境
- 高可用 PostgreSQL
- Redis Cluster
- 完整日誌與監控

### 監控指標

#### 業務指標
- 流程執行成功率
- 平均執行時間
- 決策節點評估時間
- 客戶滿意度

#### 技術指標
- CPU 使用率
- 記憶體使用率
- 資料庫連接數
- 快取命中率
- API 回應時間

### 日誌管理

#### 日誌等級
- **ERROR**: 系統錯誤與例外
- **WARN**: 警告訊息
- **INFO**: 一般操作記錄
- **DEBUG**: 詳細除錯資訊

#### 日誌格式
```json
{
  "timestamp": "2025-10-04T10:00:00.000Z",
  "level": "INFO",
  "logger": "com.example.banking.benefit.domain.service",
  "message": "Flow execution completed",
  "mdc": {
    "flowId": "FLOW_001",
    "executionId": "EXEC_123456",
    "customerId": "CUST_001"
  }
}
```

## 安全性考量

### 認證與授權
- JWT Token 認證
- RBAC 角色權限控制
- API 金鑰管理

### 資料保護
- 敏感資料加密儲存
- 資料傳輸 HTTPS 加密
- 個人資料去識別化

### 審計追蹤
- 完整操作日誌
- 資料變更追蹤
- 合規性報告

## 效能優化

### 快取策略
- Redis 分散式快取
- 本地記憶體快取
- 查詢結果快取

### 資料庫優化
- 索引最佳化
- 查詢效能調優
- 連接池設定

### 併發處理
- 異步執行機制
- 執行緒池管理
- 背壓控制

## 未來發展規劃

### 功能擴展
- [ ] 視覺化流程設計器
- [ ] 規則引擎整合
- [ ] 機器學習決策支援
- [ ] 多租戶支援

### 技術升級
- [ ] 容器化部署 (Docker + Kubernetes)
- [ ] 微服務架構拆分
- [ ] 事件驅動架構
- [ ] GraphQL API 支援

### 營運優化
- [ ] 自動化 CI/CD
- [ ] 混沌工程測試
- [ ] 效能基準測試
- [ ] 安全性滲透測試

## 參考資料

### 架構設計
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design](https://domainlanguage.com/ddd/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

### Spring Framework
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Expression Language](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

### 測試指南
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Test-Driven Development](https://www.amazon.com/Test-Driven-Development-Kent-Beck/dp/0321146530)

---

## 授權聲明

本專案採用 MIT 授權條款，詳見 [LICENSE](LICENSE) 檔案。

## 貢獻指南

歡迎提交 Issue 和 Pull Request，請參考 [CONTRIBUTING.md](CONTRIBUTING.md) 了解詳細的貢獻流程。

---

**最後更新**: 2025年10月10日  
**版本**: 1.0.0  
**測試狀態**: ✅ 70/70 通過 (100%)  
**Drools 整合**: ✅ 完成  
**維護者**: Banking Benefit Team