---
applyTo: '**/domain/**/*.java'
---

# 領域層開發指引

## 套件結構
```
domain/
├── flow/                    # 流程相關領域物件
│   ├── aggregate/          # 聚合根
│   │   └── Flow.java
│   ├── entity/            # 實體
│   │   ├── DecisionNode.java
│   │   └── ProcessNode.java
│   ├── valueobject/       # 值物件
│   │   ├── FlowId.java
│   │   ├── Version.java
│   │   └── CustomerContext.java
│   └── event/            # 領域事件
│       └── FlowExecutedEvent.java
├── command/              # 命令模式相關類別
│   ├── DecisionCommand.java
│   └── BaseDecisionCommand.java
├── state/               # 狀態模式相關類別
│   ├── ProcessState.java
│   └── BaseProcessState.java
├── service/            # 領域服務
│   ├── FlowExecutionService.java
│   └── DecisionEvaluationService.java
└── port/              # 六角形架構端口
    ├── input/        # 輸入端口
    │   └── FlowExecutionUseCase.java
    └── output/       # 輸出端口
        └── FlowRepository.java
```

## 開發準則

### 1. 通用準則
- 領域模型必須完全獨立於基礎設施
- 不允許依賴任何外部框架（除了基本的 Java SE API）
- 遵循 SOLID 原則
- 確保領域物件的不可變性

### 2. 聚合根 (Aggregate Root)
- 必須實作領域事件發布機制
- 確保聚合邊界的一致性
- 透過 ID 參考其他聚合根
- 確保事務一致性範圍

### 3. 實體 (Entity)
- 必須有明確的識別屬性
- 實作 equals() 和 hashCode()
- 確保狀態變更的有效性
- 使用防禦性複製

### 4. 值物件 (Value Object)
- 必須是不可變的
- 不需要識別屬性
- 使用 Builder 模式建立
- 實作完整的值比較

### 5. 領域事件 (Domain Event)
- 使用不可變類別
- 包含發生時間
- 包含必要的上下文資訊
- 使用明確的命名慣例

### 6. 端口 (Ports)
- 清楚定義輸入/輸出界面
- 使用領域模型作為參數和返回值
- 不洩漏基礎設施細節
- 遵循介面隔離原則

### 7. 命名規範
- 聚合根：使用領域概念名稱
- 實體：具體名詞
- 值物件：描述性名稱
- 事件：過去式動詞
- 端口：以用途命名

### 8. 測試規範
- 每個領域物件都需要單元測試
- 測試案例應反映領域規則
- 使用領域專有名詞
- 確保邊界條件的測試