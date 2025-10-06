---
applyTo: '**/test/**/*.java'
---

# 測試開發規範

## 概述

本文件基於銀行福利流程引擎專案的測試修復經驗，制定測試程式碼的開發規範，確保測試程式碼的品質、可維護性和一致性。

## 通用測試準則

### 1. 測試命名規範

```java
// 方法命名格式：操作_條件_期望結果
@Test
void executeFlow_Success() { }

@Test
void executeFlow_ValidationError() { }

@Test  
void getFlowStatistics_ShouldReturnStatistics() { }
```

### 2. 測試結構

每個測試方法應遵循 AAA 模式：
- **Arrange**：準備測試資料和環境
- **Act**：執行被測試的操作
- **Assert**：驗證結果

```java
@Test
void shouldExecuteFlowSuccessfully() {
    // Arrange
    Flow flow = createTestFlow();
    ExecutionContext context = createTestContext();
    
    // Act
    ExecutionResult result = flowExecutionService.execute(flow, context);
    
    // Assert
    assertThat(result.isSuccess()).isTrue();
}
```

## API 控制器測試規範

### 1. MockMvc 測試設定

```java
@WebMvcTest(FlowExecutionController.class)
class FlowExecutionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private FlowExecutionServiceExtended flowExecutionService;
}
```

### 2. API 回應格式驗證

**統一回應格式檢查**：
```java
// 成功回應檢查
.andExpect(status().isOk())
.andExpect(jsonPath("$.success").value(true))
.andExpect(jsonPath("$.code").value("200"))
.andExpect(jsonPath("$.data").exists());

// 錯誤回應檢查
.andExpect(status().isBadRequest())
.andExpect(jsonPath("$.success").value(false))
.andExpect(jsonPath("$.code").value("400"))
.andExpect(jsonPath("$.message").exists());
```

### 3. 請求/回應處理

**JSON 序列化測試**：
```java
@Test
void executeFlow_Success() throws Exception {
    // 準備請求物件
    ExecuteFlowRequest request = ExecuteFlowRequest.builder()
            .flowId("F001")
            .version("1.0.0")
            .customerData(customerData)
            .build();
    
    // Mock 服務回應
    when(flowExecutionService.execute(any(), any()))
            .thenReturn(executionResult);
    
    // 執行測試
    mockMvc.perform(post("/api/v1/flow-executions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
}
```

### 4. 驗證錯誤處理

**輸入驗證測試**：
```java
@Test
void executeFlow_ValidationError() throws Exception {
    request.setFlowId(""); // 設置無效資料
    
    mockMvc.perform(post("/api/v1/flow-executions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest()) // 注意：實際狀態碼
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.code").value("400"));
}
```

## Mock 物件使用規範

### 1. 參數匹配準則

**精確匹配 vs 通用匹配**：
```java
// 精確匹配 - 推薦用於關鍵參數
when(service.getFlowStatistics(eq("F001"), isNull(), isNull()))
        .thenReturn(statistics);

// 通用匹配 - 用於非關鍵參數
when(service.execute(any(Flow.class), any(ExecutionContext.class)))
        .thenReturn(result);
```

**null 參數處理**：
```java
// 正確的 null 參數 mock
when(flowExecutionService.getFlowStatistics(eq(flowId), isNull(), isNull()))
        .thenReturn(mockStatistics);

// 對應的 verify
verify(flowExecutionService).getFlowStatistics(eq(flowId), isNull(), isNull());
```

### 2. Mock 設定最佳實踐

```java
@BeforeEach
void setUp() {
    // 在 setUp 中準備通用的測試資料
    customerData = CustomerData.create("C001", attributes);
    
    // 設定預設的 mock 行為
    when(flowManagementService.getFlow(any(), any()))
            .thenReturn(Optional.of(flow));
}
```

## 領域服務測試規範

### 1. 單元測試隔離

```java
@ExtendWith(MockitoExtension.class)
class FlowExecutionServiceImplTest {
    
    @Mock
    private FlowRepository flowRepository;
    
    @Mock 
    private ExecutionLogRepository executionLogRepository;
    
    @InjectMocks
    private FlowExecutionServiceImpl flowExecutionService;
}
```

### 2. 測試資料建立

**使用 Builder 模式**：
```java
private ExecutionDetails createExecutionDetails() {
    return ExecutionDetails.builder()
            .executionId("exec-1")
            .flowId("flow-1")
            .customerId("customer-1")
            .startTime(LocalDateTime.now())
            .executionTime(Duration.ofSeconds(2))
            .status("SUCCESS")
            .build();
}
```

### 3. 並行測試處理

```java
@Test
void shouldHandleConcurrentExecution() {
    // 準備並行測試環境
    CountDownLatch latch = new CountDownLatch(threadCount);
    
    // 執行並行測試
    IntStream.range(0, threadCount).forEach(i -> {
        executor.submit(() -> {
            try {
                // 測試邏輯
                service.execute(flow, context);
            } finally {
                latch.countDown();
            }
        });
    });
    
    // 等待完成並驗證
    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
}
```

## JSON 序列化測試規範

### 1. DTO 序列化驗證

```java
@Test
void shouldSerializeCustomerDataCorrectly() {
    // Arrange
    Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
    attributes.put("age", CustomerAttribute.forInteger(25));
    CustomerData customerData = CustomerData.create("C001", attributes);
    
    // Act & Assert
    assertThatNoException().isThrownBy(() -> 
        objectMapper.writeValueAsString(customerData));
}
```

### 2. Jackson 註解測試

確保領域物件具備正確的序列化支援：
```java
// 在領域物件中添加適當的 Jackson 註解
@JsonCreator
private CustomerData(@JsonProperty("id") String id, 
                    @JsonProperty("allAttributes") Map<String, CustomerAttribute<?>> attributes) {
    // 建構邏輯
}

@JsonProperty("allAttributes") 
public Map<String, CustomerAttribute<?>> getAllAttributes() {
    return Collections.unmodifiableMap(attributes);
}
```

## 測試資料管理

### 1. 測試資料建立

```java
public class TestDataBuilder {
    
    public static CustomerData createDefaultCustomerData() {
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("age", CustomerAttribute.forInteger(25));
        attributes.put("salary", CustomerAttribute.forLong(50000L));
        return CustomerData.create("C001", attributes);
    }
    
    public static ExecuteFlowRequest createDefaultExecuteRequest() {
        return ExecuteFlowRequest.builder()
                .flowId("F001")
                .version("1.0.0")
                .customerData(createDefaultCustomerData())
                .build();
    }
}
```

### 2. 測試清理

```java
@AfterEach
void tearDown() {
    // 清理測試資料
    // 重置 Mock 物件狀態
    reset(mockService);
}
```

## 錯誤處理測試

### 1. 異常情況測試

```java
@Test
void shouldThrowExceptionWhenFlowNotFound() {
    // Arrange
    when(flowRepository.existsById(any())).thenReturn(false);
    
    // Act & Assert
    assertThatThrownBy(() -> 
        flowExecutionService.getFlowStatistics("non-exist", null, null))
        .isInstanceOf(FlowNotFoundException.class)
        .hasMessageContaining("找不到流程");
}
```

### 2. 邊界條件測試

```java
@Test
void shouldHandleNullTimeParameters() {
    // 測試 null 時間參數的處理
    when(executionLogRepository.findByFlowIdAndExecutionTimeBetween(
        eq("F001"), any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(Collections.emptyList());
    
    FlowStatistics stats = flowExecutionService.getFlowStatistics("F001", null, null);
    
    assertThat(stats.getTotalExecutions()).isEqualTo(0);
}
```

## 測試執行與維護

### 1. 測試執行命令

```bash
# 執行所有測試
./gradlew test --console=plain

# 執行特定測試類別
./gradlew test --tests FlowExecutionControllerTest --console=plain

# 執行特定測試方法
./gradlew test --tests FlowExecutionControllerTest.executeFlow_Success --console=plain
```

### 2. 測試失敗分析

當測試失敗時，按以下順序檢查：

1. **編譯錯誤**：先確保程式碼可以編譯
2. **API 格式不匹配**：檢查回應格式是否符合測試期望
3. **Mock 設定錯誤**：確認 Mock 參數匹配正確
4. **序列化問題**：驗證 JSON 序列化/反序列化是否正常
5. **業務邏輯錯誤**：檢查實際業務邏輯實作

### 3. 測試重構指導

當需要重構測試時：

1. **保持測試簡單**：一個測試只驗證一個行為
2. **避免測試實作細節**：專注於行為而非實作
3. **使用適當的抽象**：建立測試 Helper 方法
4. **保持測試獨立**：每個測試應該可以獨立執行

## 常見問題與解決方案

### 1. Mock 參數不匹配

**問題**：Mock 設定與實際呼叫參數不匹配
```java
// 錯誤
when(service.method(any())).thenReturn(result);
verify(service).method(eq(specificValue)); // 失敗

// 正確  
when(service.method(eq(specificValue))).thenReturn(result);
verify(service).method(eq(specificValue)); // 成功
```

### 2. JSON 序列化失敗

**問題**：領域物件缺少序列化支援
**解決方案**：添加適當的 Jackson 註解

### 3. 測試環境隔離

**問題**：測試之間互相影響
**解決方案**：確保每個測試獨立設定和清理

## 總結

遵循本規範可以確保：

1. **測試可靠性**：減少因環境或時序問題導致的測試不穩定
2. **程式碼品質**：通過完整的測試覆蓋確保程式碼品質
3. **維護性**：清晰的測試結構便於後續維護和擴展
4. **一致性**：統一的測試風格提高團隊協作效率

持續更新此規範以反映專案發展和最佳實踐的演進。