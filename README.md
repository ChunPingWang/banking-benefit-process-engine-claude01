# 銀行權益流程引擎 (Banking Benefit Process Engine)

## 專案概述

銀行權益流程引擎是一個基於 Java 21 和 Spring Boot 3 的高效能流程執行系統，採用六角形架構（Hexagonal Architecture）和領域驅動設計（DDD）方法論。系統主要用於執行銀行客戶權益相關的業務流程，支援動態決策節點和處理節點的組合執行。

### 核心特色
- 🏗️ **六角形架構**：清楚分離業務邏輯與技術實作
- 🎯 **領域驅動設計**：以業務領域為核心的設計方法
- ⚡ **高效能執行**：支援並行處理與快取機制
- 🔄 **動態流程**：支援 Java 類別與 SpEL 表達式雙模式
- 📊 **完整監控**：提供執行日誌與效能監控
- 🔐 **安全可靠**：完整的錯誤處理與審計功能
- ✅ **高測試覆蓋率**：46 個單元測試，100% 通過率

### 最新更新 (2025-10-04)
- ✨ 重構 `ExecutionContext` 與 `CustomerData` 模型，提升類型安全性
- 🔧 引入 `CustomerAttribute` 泛型類別，支援強型別屬性管理
- 🐛 修復所有測試案例，達成 100% 測試通過率
- 📝 統一 SpEL 表達式語法，改善可維護性
- 🏗️ 優化領域模型設計，遵循 SOLID 原則

## 技術規格

### 核心技術棧
- **程式語言**: Java 21
- **框架**: Spring Boot 3.2.0
- **建構工具**: Gradle 8.x
- **測試框架**: JUnit 5 + Mockito
- **測試覆蓋率**: 46 個測試，100% 通過
- **資料庫**: H2 (開發) / PostgreSQL (正式)
- **快取**: Caffeine
- **表達式引擎**: Spring Expression Language (SpEL)
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
│   └── cache/                                 # 快取實作
└── config/                                     # 應用配置
    └── ApplicationConfig.java
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

### 快速開始

```bash
# 複製專案
git clone <repository-url>
cd banking-benefit-process-engine

# 建構專案
./gradlew build

# 執行測試
./gradlew test

# 啟動應用程式
./gradlew bootRun

# 查看 API 文件
open http://localhost:8080/swagger-ui.html

# 查看 H2 資料庫控制台
open http://localhost:8080/h2-console
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
- **總測試數**: 46 個
- **通過率**: 100%
- **測試類別**:
  - ✅ SpelExpressionEvaluatorTest (7/7)
  - ✅ JavaClassEvaluatorTest (5/5)
  - ✅ BaseDecisionEvaluationServiceTest (6/6)
  - ✅ BaseLogicCompositionServiceTest (8/8)
  - ✅ BaseProcessExecutionServiceTest (5/5)
  - ✅ DecisionNodeExecutorTest (6/6)
  - ✅ ProcessNodeExecutorTest (7/7)
  - ✅ BaseFlowExecutionServiceTest (2/2)

### 最近修復
- 統一 `ExecutionContext` 模型，解決套件衝突
- 引入 `CustomerAttribute` 泛型類別，提升類型安全
- 修正 SpEL 表達式語法 (`#customerData.get('key')`)
- 完善 mock 依賴注入，修復服務層測試
- 調整測試預期，處理不存在類別的情境

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

**最後更新**: 2025年10月4日  
**版本**: 1.0.0  
**測試狀態**: ✅ 46/46 通過 (100%)  
**維護者**: Banking Benefit Team