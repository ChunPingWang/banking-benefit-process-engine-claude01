package com.example.banking.benefit.domain.service.executor;

import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.process.ProcessNode;
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

class ProcessNodeExecutorTest {

    private ProcessNodeExecutor executor;
    
    @Mock
    private BaseExecutionContext context;
    
    @Mock
    private ProcessNode processNode;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        executor = new ProcessNodeExecutor();
        
        // 設置基本的mock行為
        when(context.getFlowId()).thenReturn("test-flow");
        when(context.getExecutionId()).thenReturn("test-execution");
        when(context.getCustomerId()).thenReturn("test-customer");
        
        Map<String, Object> customerData = new HashMap<>();
        customerData.put("points", 1000);
        customerData.put("tier", "gold");
        when(context.getCustomerData()).thenReturn(customerData);
        when(context.getVariables()).thenReturn(new HashMap<>());
        
        when(processNode.getNodeId()).thenReturn("test-node");
    }
    
    @Test
    void execute_WithSpelExpression_ShouldReturnSuccess() {
        // 準備數據
        when(processNode.getSpelExpression()).thenReturn("#{#customerData['points'] += 100}");
        when(processNode.getImplementationClass()).thenReturn(null);
        
        // 執行
        Map<String, Object> nodeContext = new HashMap<>();
        nodeContext.put("executionId", "test-execution");
        ExecutionResult result = executor.execute(processNode, context, nodeContext);
        
        // 驗證
        assertNotNull(result);
        assertTrue(result.getStatus().isSuccess());
        assertEquals(1100, context.getCustomerData().get("points"));
    }
    
    @Test
    void execute_WithJavaClass_ShouldReturnSuccess() {
        // 準備數據
        when(processNode.getImplementationClass()).thenReturn("com.example.TestProcess");
        when(processNode.getSpelExpression()).thenReturn(null);
        
        // 執行
        Map<String, Object> nodeContext = new HashMap<>();
        nodeContext.put("executionId", "test-execution");
        ExecutionResult result = executor.execute(processNode, context, nodeContext);
        
        // 驗證
        assertNotNull(result);
        assertTrue(result.getStatus().isSuccess());
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
            public boolean isDecisionNode() {
                return true;
            }
            
            @Override
            public boolean isProcessNode() {
                return false;
            }
        };
        
        // 驗證
        assertThrows(FlowExecutionException.class, () -> 
            executor.execute(invalidNode, context, new HashMap<>()));
    }
    
    @Test
    void execute_WithNoLogic_ShouldThrowException() {
        // 準備數據
        when(processNode.getImplementationClass()).thenReturn(null);
        when(processNode.getSpelExpression()).thenReturn(null);
        
        // 驗證
        assertThrows(FlowExecutionException.class, () -> 
            executor.execute(processNode, context, new HashMap<>()));
    }
    
    @Test
    void supports_WithProcessNode_ShouldReturnTrue() {
        assertTrue(executor.supports(processNode));
    }
    
    @Test
    void supports_WithNonProcessNode_ShouldReturnFalse() {
        Node nonProcessNode = new Node() {
            @Override
            public String getNodeId() {
                return "non-process";
            }
            
            @Override
            public boolean isDecisionNode() {
                return true;
            }
            
            @Override
            public boolean isProcessNode() {
                return false;
            }
        };
        
        assertFalse(executor.supports(nonProcessNode));
    }
    
    @Test
    void execute_WithStateUpdate_ShouldReturnSuccess() {
        // 準備數據
        when(processNode.getStateName()).thenReturn("PROCESSING");
        when(processNode.getSpelExpression()).thenReturn("#{#customerData['tier'] = 'platinum'}");
        
        // 執行
        Map<String, Object> nodeContext = new HashMap<>();
        nodeContext.put("executionId", "test-execution");
        ExecutionResult result = executor.execute(processNode, context, nodeContext);
        
        // 驗證
        assertNotNull(result);
        assertTrue(result.getStatus().isSuccess());
        assertEquals("PROCESSING", result.getVariables().get("state"));
        assertEquals("platinum", context.getCustomerData().get("tier"));
    }
}