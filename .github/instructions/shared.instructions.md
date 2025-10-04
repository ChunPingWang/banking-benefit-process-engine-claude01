---
applyTo: '**/shared/**/*.java'
---

# 服務層開發指引

## API 開發規範

### RESTful API 設計準則
1. URI 命名規範
   - 使用名詞而非動詞
   - 使用複數形式表示資源集合
   - 使用 kebab-case 命名方式
   - 範例: `/api/v1/flow-definitions`

2. HTTP 方法使用
   - GET: 查詢資源
   - POST: 建立資源
   - PUT: 完整更新資源
   - PATCH: 部分更新資源
   - DELETE: 刪除資源

3. 狀態碼使用
   - 200: 請求成功
   - 201: 資源建立成功
   - 400: 請求格式錯誤
   - 401: 未認證
   - 403: 權限不足
   - 404: 資源不存在
   - 500: 伺服器錯誤

4. 回應格式標準化
```json
{
    "status": "SUCCESS",
    "data": {
        // 實際資料
    },
    "message": "操作說明訊息",
    "timestamp": "2025-10-04T10:00:00Z"
}
```

### API 文件
- 使用 OpenAPI (Swagger) 規範
- 所有 API 端點必須包含完整文件
- 包含請求/回應範例
- 說明所有可能的錯誤狀況

## 資料庫使用規範

### 開發環境 (H2)
- 使用內嵌模式運行
- 自動建立資料表結構
- 載入測試資料
- URL: jdbc:h2:mem:benefitdb
- 設定檔: application-dev.yml

### 正式環境 (PostgreSQL)
- 版本要求: 13 以上
- 使用連接池: HikariCP
- 設定檔: application-prod.yml
- 交易隔離級別: READ_COMMITTED
- 使用 Flyway 進行資料庫版本控制

### 通用資料庫規範
1. 命名規範
   - 表名使用小寫，單字間用底線分隔
   - 欄位名稱使用小寫，單字間用底線分隔
   - 主鍵統一命名為 id
   - 索引命名: idx_[表名]_[欄位名]

2. 稽核欄位
   - created_at: 建立時間
   - updated_at: 更新時間
   - created_by: 建立者
   - updated_by: 更新者

3. 效能考量
   - 適當的索引設計
   - 避免使用 select *
   - 大量資料分頁查詢
   - 定期維護統計資訊

4. 資料安全
   - 敏感資料加密儲存
   - 使用參數化查詢
   - 適當的資料庫權限設定

## 快取策略
1. 使用 Spring Cache Abstraction
2. 支援多級快取
   - 本機快取: Caffeine
   - 分散式快取: Redis (可選)

# 服務層開發指引

## API 開發規範

### RESTful API 設計準則
1. URI 命名規範
   - 使用名詞而非動詞
   - 使用複數形式表示資源集合
   - 使用 kebab-case 命名方式
   - 範例: `/api/v1/flow-definitions`

2. HTTP 方法使用
   - GET: 查詢資源
   - POST: 建立資源
   - PUT: 完整更新資源
   - PATCH: 部分更新資源
   - DELETE: 刪除資源

3. 狀態碼使用
   - 200: 請求成功
   - 201: 資源建立成功
   - 400: 請求格式錯誤
   - 401: 未認證
   - 403: 權限不足
   - 404: 資源不存在
   - 500: 伺服器錯誤

4. 回應格式標準化
```json
{
    "status": "SUCCESS",
    "data": {
        // 實際資料
    },
    "message": "操作說明訊息",
    "timestamp": "2025-10-04T10:00:00Z"
}
```

### API 文件
- 使用 OpenAPI (Swagger) 規範
- 所有 API 端點必須包含完整文件
- 包含請求/回應範例
- 說明所有可能的錯誤狀況

## 資料庫使用規範

### 開發環境 (H2)
- 使用內嵌模式運行
- 自動建立資料表結構
- 載入測試資料
- URL: jdbc:h2:mem:benefitdb
- 設定檔: application-dev.yml

### 正式環境 (PostgreSQL)
- 版本要求: 13 以上
- 使用連接池: HikariCP
- 設定檔: application-prod.yml
- 交易隔離級別: READ_COMMITTED
- 使用 Flyway 進行資料庫版本控制

### 通用資料庫規範
1. 命名規範
   - 表名使用小寫，單字間用底線分隔
   - 欄位名稱使用小寫，單字間用底線分隔
   - 主鍵統一命名為 id
   - 索引命名: idx_[表名]_[欄位名]

2. 稽核欄位
   - created_at: 建立時間
   - updated_at: 更新時間
   - created_by: 建立者
   - updated_by: 更新者

3. 效能考量
   - 適當的索引設計
   - 避免使用 select *
   - 大量資料分頁查詢
   - 定期維護統計資訊

4. 資料安全
   - 敏感資料加密儲存
   - 使用參數化查詢
   - 適當的資料庫權限設定

## 快取策略
1. 使用 Spring Cache Abstraction
2. 支援多級快取
   - 本機快取: Caffeine
   - 分散式快取: Redis (可選)