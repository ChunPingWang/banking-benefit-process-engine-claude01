---
description: New prompt created from chat session
mode: agent
tools: ['/codebase']
model: Claude Sonnet 3.5 (copilot)
---

## 專案技術規格

- 專案類型: Java
- JDK 版本: 21
- 框架: Spring Boot 3
- 測試框架: JUnit 5
- 建構工具: Gradle

### API 規格
- RESTful API
- OpenAPI (Swagger) 文件

### 資料庫設定
- 開發環境: H2
- 正式環境: PostgreSQL


---
<user>
#fetch https://claude.ai/public/artifacts/ca7afff6-a7c9-4e9b-ba22-84020a0c5bac
根據這個網址的內容，建立開發準則與相關的工作清單，但先不要開發
</user>
<assistant>
我將根據網頁內容來分析並制定開發準則和工作清單。讓我先取得網頁內容。 根據網頁內容，我將整理開發準則與工作清單如下：

一、開發準則：

1. 架構設計準則
- 採用六角形架構(Hexagonal Architecture)
- 嚴格遵循 SOLID 原則
- 使用領域驅動設計(DDD)方法論 
- 確保領域邏輯與技術實作完全隔離

2. 程式碼品質準則
- 每個類別只負責單一職責(SRP)
- 擴充功能時不修改現有程式碼(OCP)
- 子類別必須能替換父類別(LSP) 
- 介面盡量最小化(ISP)
- 高層不依賴低層實作(DIP)
- 程式碼覆蓋率要求 > 80%

3. 安全性準則
- SpEL表達式需有白名單機制
- 實作嚴格的權限管控(RBAC)
- 確保資料傳輸加密
- 記錄完整操作審計日誌

4. 效能準則
- 單一流程執行時間 < 500ms
- 系統整體 TPS > 1000
- Decision評估時間 < 50ms
- State處理時間 < 200ms
- 資料庫查詢時間 < 100ms

二、工作清單(依開發階段)：

第一階段：領域模型與核心架構 (4-6週)
1. 六角形架構搭建
- [ ] 定義Primary Ports(輸入埠介面)
- [ ] 定義Secondary Ports(輸出埠介面) 
- [ ] 建立分層結構

2. 領域模型設計
- [ ] 識別聚合根:Flow
- [ ] 設計實體:DecisionNode、ProcessNode
- [ ] 設計值物件:FlowId、Version、CustomerContext
- [ ] 定義領域事件

3. SOLID原則落實
- [ ] 確保類別遵循SRP
- [ ] 設計介面遵循ISP  
- [ ] 建立抽象層實現DIP

4. 資料庫設計
- [ ] 建立表格結構
- [ ] 設計索引與關聯
- [ ] 準備測試資料

第二階段：Command & State Pattern實作 (4-6週)
1. Command Pattern實作
- [ ] DecisionCommand介面設計
- [ ] BaseDecisionCommand抽象類別
- [ ] 具體Command實作
- [ ] DecisionCommandFactory

2. State Pattern實作
- [ ] ProcessState介面設計 
- [ ] BaseProcessState抽象類別
- [ ] 具體State實作
- [ ] ProcessStateFactory

3. Reflection機制
- [ ] 動態類別載入
- [ ] SpEL表達式執行器
- [ ] 快取機制優化

第三階段：領域服務與應用服務 (3-4週)
1. 領域服務實作
- [ ] FlowExecutionService
- [ ] DecisionEvaluationService
- [ ] LogicCompositionService
- [ ] ProcessExecutionService

2. 應用服務實作
- [ ] FlowExecutionUseCase
- [ ] FlowManagementUseCase
- [ ] MonitoringUseCase
- [ ] ConfigurationUseCase

3. 領域事件處理
- [ ] DomainEventPublisher
- [ ] Event Handlers
- [ ] 事件持久化

第四階段：基礎設施層實作 (4-5週)
1. Repository實作
- [ ] FlowRepositoryImpl
- [ ] DecisionRepositoryImpl 
- [ ] ProcessRepositoryImpl
- [ ] ExecutionLogRepositoryImpl

2. 外部服務Adapter
- [ ] NotificationAdapter
- [ ] AuditLogAdapter
- [ ] CacheAdapter
- [ ] CustomerServiceAdapter

3. Primary Adapters 
- [ ] REST Controller
- [ ] GraphQL API(選配)
- [ ] gRPC Service(選配)

第五階段：管理介面與視覺化 (4-5週)
1. 流程設計器
- [ ] 視覺化流程編輯器
- [ ] 節點拖拽功能
- [ ] 關聯線繪製
- [ ] 即時驗證

2. 配置管理介面
- [ ] Decision/Process配置
- [ ] Java/SpEL切換
- [ ] 版本管理
- [ ] 部署管理

3. 監控與日誌介面
- [ ] 執行監控儀表板
- [ ] 日誌查詢與過濾
- [ ] 效能分析圖表
- [ ] 告警設定

第六階段：測試與品質保證 (3-4週)
1. 單元測試
- [ ] Domain Model測試
- [ ] Command/State測試
- [ ] Service測試
- [ ] Repository測試

2. 整合測試
- [ ] API測試
- [ ] 資料庫整合測試
- [ ] 外部服務整合測試

3. 架構測試
- [ ] 依賴關係驗證
- [ ] 分層結構驗證
- [ ] 效能基準測試

這個開發計畫預計需要22-29週的時間完成。每個階段都有明確的目標和可交付成果，同時遵循嚴格的開發準則，確保系統品質。您可以根據實際情況調整各階段的時程和優先順序。
</assistant>