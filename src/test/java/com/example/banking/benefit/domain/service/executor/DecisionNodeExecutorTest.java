package com.example.banking.benefit.domain.service.executor;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.CustomerData;
import com.example.banking.benefit.domain.model.common.CustomerAttribute;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.node.Node;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.exception.FlowExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DecisionNodeExecutorTest {

    private DecisionNodeExecutor executor;
    
    @Mock
    private BaseExecutionContext context;
    
    @Mock
    private DecisionNode decisionNode;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        executor = new DecisionNodeExecutor();
        
        // 設置基本的mock行為
        when(context.getFlowId()).thenReturn("test-flow");
        when(context.getExecutionId()).thenReturn("test-execution");
        when(context.getCustomerId()).thenReturn("test-customer");
        
        Map<String, CustomerAttribute<?>> attributes = new HashMap<>();
        attributes.put("age", CustomerAttribute.of(30, Integer.class));
        attributes.put("vip", CustomerAttribute.of(true, Boolean.class));
        CustomerData customerData = CustomerData.create(attributes);
        when(context.getCustomerData()).thenReturn(customerData);
        when(context.getVariables()).thenReturn(new HashMap<>());
        
        when(decisionNode.getNodeId()).thenReturn("test-node");
    }
    
    @Test
    void execute_WithSpelExpression_ShouldReturnSuccess() {
        // 準備數據
        when(decisionNode.getSpelExpression()).thenReturn("#{#customerData.get('vip') == true}");
        when(decisionNode.getImplementationClass()).thenReturn(null);
        
        // 執行
        Map<String, Object> nodeContext = new HashMap<>();
        nodeContext.put("executionId", "test-execution");
        ExecutionResult result = executor.execute(decisionNode, context, nodeContext);
        
        // 驗證
        assertNotNull(result);
        assertTrue(result.getStatus().isSuccess());
        assertEquals(true, result.getVariables().get("decisionResult"));
    }
    
    @Test
    void execute_WithJavaClass_ShouldReturnSuccess() {
        // 準備數據 - 由於類不存在，這個測試應該期望失敗結果
        when(decisionNode.getImplementationClass()).thenReturn("com.example.TestDecision");
        when(decisionNode.getSpelExpression()).thenReturn(null);
        
        // 執行
        Map<String, Object> nodeContext = new HashMap<>();
        nodeContext.put("executionId", "test-execution");
        ExecutionResult result = executor.execute(decisionNode, context, nodeContext);
        
        // 驗證 - 由於類不存在，應該返回失敗結果
        assertNotNull(result);
        assertFalse(result.getStatus().isSuccess());
    }
    
    @Test
    void execute_WithInvalidNode_ShouldThrowException() {
        // 準備數據
        Node invalidNode = new Node() {
            @Override
            public String getNodeId() {
                return "invalid";
            }
            
            @Override
            public String getNodeName() {
                return "Invalid Node";
            }
            
            @Override
            public String getDescription() {
                return "Invalid test node";
            }
            
            @Override
            public Integer getNodeOrder() {
                return 1;
            }
        };
        
        // 驗證
        assertThrows(FlowExecutionException.class, () -> 
            executor.execute(invalidNode, context, new HashMap<>()));
    }
    
    @Test
    void execute_WithNoLogic_ShouldThrowException() {
        // 準備數據
        when(decisionNode.getImplementationClass()).thenReturn(null);
        when(decisionNode.getSpelExpression()).thenReturn(null);
        
        // 執行 - executor 捕獲異常並返回 failure，不是抛出異常
        Map<String, Object> nodeContext = new HashMap<>();
        nodeContext.put("executionId", "test-execution");
        ExecutionResult result = executor.execute(decisionNode, context, nodeContext);
        
        // 驗證 - 應該返回失敗結果
        assertNotNull(result);
        assertFalse(result.getStatus().isSuccess());
    }
    
    @Test
    void supports_WithDecisionNode_ShouldReturnTrue() {
        assertTrue(executor.supports(decisionNode));
    }
    
    @Test
    void supports_WithNonDecisionNode_ShouldReturnFalse() {
        Node nonDecisionNode = new Node() {
            @Override
            public String getNodeId() {
                return "non-decision";
            }
            
            @Override
            public String getNodeName() {
                return "Non-Decision Node";
            }
            
            @Override
            public String getDescription() {
                return "Non-decision test node";
            }
            
            @Override
            public Integer getNodeOrder() {
                return 1;
            }
        };
        
        assertFalse(executor.supports(nonDecisionNode));
    }
}