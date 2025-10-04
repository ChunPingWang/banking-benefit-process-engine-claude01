---
applyTo: '**'
---

# 資料庫設計指引

## 資料表結構

### flow_definition (流程定義表)
```sql
CREATE TABLE flow_definition (
    flow_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_name VARCHAR(100) NOT NULL,
    flow_description CLOB,
    version VARCHAR(20) NOT NULL, 
    status VARCHAR(20) NOT NULL,
    start_node_id VARCHAR(50),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);
CREATE INDEX idx_status ON flow_definition(status);
```

主要用途:
- 儲存流程定義的基本資訊
- 管理流程版本
- 追蹤流程狀態(啟用/停用/草稿)

### decision_node (決策節點表)
```sql
CREATE TABLE decision_node (
    node_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    node_name VARCHAR(100) NOT NULL,
    node_description CLOB,
    decision_type VARCHAR(50) NOT NULL,
    implementation_class VARCHAR(255),
    spel_expression CLOB,
    node_order INT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_decision_flow FOREIGN KEY (flow_id) REFERENCES flow_definition(flow_id)
);
CREATE INDEX idx_decision_flow ON decision_node(flow_id);
```

主要用途:
- 定義決策節點的邏輯
- 支援 Java 類別或 SpEL 表達式
- 維護節點執行順序

### process_node (處理節點表)
```sql
CREATE TABLE process_node (
    node_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    node_name VARCHAR(100) NOT NULL,
    node_description CLOB,
    process_type VARCHAR(50) NOT NULL,
    implementation_class VARCHAR(255),
    spel_expression CLOB,
    state_name VARCHAR(50),
    node_order INT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_process_flow FOREIGN KEY (flow_id) REFERENCES flow_definition(flow_id)
);
CREATE INDEX idx_process_flow ON process_node(flow_id);
```

主要用途:
- 定義處理節點的行為
- 支援 Java 類別或 SpEL 表達式
- 管理節點狀態

### node_relation (節點關聯表)
```sql
CREATE TABLE node_relation (
    relation_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    source_node_id VARCHAR(50) NOT NULL,
    source_node_type VARCHAR(20) NOT NULL,
    target_node_id VARCHAR(50) NOT NULL,
    target_node_type VARCHAR(20) NOT NULL,
    relation_type VARCHAR(20) NOT NULL,
    logic_operator VARCHAR(10),
    condition_expression CLOB,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_relation_flow FOREIGN KEY (flow_id) REFERENCES flow_definition(flow_id)
);
CREATE INDEX idx_relation_source ON node_relation(source_node_id);
CREATE INDEX idx_relation_target ON node_relation(target_node_id);
```

主要用途:
- 定義節點間的連接關係
- 設定轉換條件
- 支援複雜的邏輯組合

### decision_group (決策群組表)
```sql
CREATE TABLE decision_group (
    group_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    group_name VARCHAR(100) NOT NULL,
    logic_operator VARCHAR(10) NOT NULL,
    target_process_id VARCHAR(50),
    parent_group_id VARCHAR(50),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_group_flow FOREIGN KEY (flow_id) REFERENCES flow_definition(flow_id),
    CONSTRAINT fk_group_process FOREIGN KEY (target_process_id) REFERENCES process_node(node_id)
);
CREATE INDEX idx_group_flow ON decision_group(flow_id);
```

主要用途:
- 組織多個決策節點
- 定義群組邏輯關係
- 支援巢狀群組結構

### decision_group_member (決策群組成員表)
```sql
CREATE TABLE decision_group_member (
    member_id VARCHAR(50) NOT NULL PRIMARY KEY,
    group_id VARCHAR(50) NOT NULL,
    decision_node_id VARCHAR(50) NOT NULL,
    member_order INT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_member_group FOREIGN KEY (group_id) REFERENCES decision_group(group_id),
    CONSTRAINT fk_member_decision FOREIGN KEY (decision_node_id) REFERENCES decision_node(node_id)
);
CREATE INDEX idx_member_group ON decision_group_member(group_id);
```

主要用途:
- 管理群組成員關係
- 維護成員執行順序

### execution_log (執行日誌表)
```sql
CREATE TABLE execution_log (
    log_id VARCHAR(50) NOT NULL PRIMARY KEY,
    flow_id VARCHAR(50) NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    execution_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    node_id VARCHAR(50),
    node_type VARCHAR(20),
    execution_result VARCHAR(20),
    result_data CLOB,
    error_message CLOB,
    execution_duration_ms INT
);
CREATE INDEX idx_log_flow_customer ON execution_log(flow_id, customer_id);
CREATE INDEX idx_log_execution_time ON execution_log(execution_time);
```

主要用途:
- 記錄流程執行歷程
- 追蹤執行結果與錯誤
- 支援效能監控

## 資料庫規範

### 開發環境 (H2)
- 使用內嵌模式
- 自動建立結構
- 自動載入測試資料

### 正式環境 (PostgreSQL)
- 版本: 13+
- 使用 HikariCP 連接池
- 使用 Flyway 版本控制

### 命名規範
- 表名使用小寫與底線
- 欄位名稱使用小寫與底線
- 主鍵統一使用 id
- 索引命名格式: idx_[表名]_[欄位名]

### 效能考量
- 建立適當索引
- 避免 select *
- 分頁查詢大量資料
- 定期更新統計資訊

### 安全性
- 敏感資料加密
- 使用參數化查詢
- 適當的存取權限
Provide project context and coding guidelines that AI should follow when generating code, answering questions, or reviewing changes.