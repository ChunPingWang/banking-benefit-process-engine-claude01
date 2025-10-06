package com.example.banking.benefit.domain.service.impl;

import com.example.banking.benefit.domain.model.flow.Flow;
import com.example.banking.benefit.domain.model.flow.FlowId;
import com.example.banking.benefit.domain.model.common.BaseExecutionContext;
import com.example.banking.benefit.domain.model.common.ExecutionContext;
import com.example.banking.benefit.domain.model.result.ExecutionResult;
import com.example.banking.benefit.domain.model.result.ProcessResult;
import com.example.banking.benefit.domain.model.node.NodeType;
import com.example.banking.benefit.domain.model.node.DecisionNode;
import com.example.banking.benefit.domain.model.node.ProcessNode;
import com.example.banking.benefit.domain.model.state.ProcessState;
import com.example.banking.benefit.domain.model.statistics.ExecutionDetails;
import com.example.banking.benefit.domain.model.statistics.FlowStatistics;
import com.example.banking.benefit.domain.model.log.ExecutionLog;
import com.example.banking.benefit.domain.model.command.DecisionCommand;
import com.example.banking.benefit.domain.service.FlowExecutionServiceExtended;
import com.example.banking.benefit.domain.repository.ExecutionLogRepository;
import com.example.banking.benefit.domain.repository.FlowRepository;
import com.example.banking.benefit.domain.exception.*;
import com.example.banking.benefit.domain.service.WhitelistTypeLocator;

import jakarta.inject.Inject;
import org.springframework.data.domain.PageRequest;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;
import java.lang.reflect.InvocationTargetException;

@Service
public class FlowExecutionServiceImpl implements FlowExecutionServiceExtended {

    private final FlowRepository flowRepository;
    private final ExecutionLogRepository executionLogRepository;

    @Inject
    public FlowExecutionServiceImpl(FlowRepository flowRepository, ExecutionLogRepository executionLogRepository) {
        this.flowRepository = flowRepository;
        this.executionLogRepository = executionLogRepository;
    }
    
    @Override
    public ExecutionResult execute(Flow flow, BaseExecutionContext context) {
        // 檢查流程是否存在
        if (!flowRepository.existsById(flow.getFlowId())) {
            throw new FlowNotFoundException("找不到流程：" + flow.getFlowId().getValue());
        }

        var executionId = generateExecutionId();
        
        try {
            // 記錄開始執行
            logExecution(executionId, flow.getFlowId().getValue(), context.getCustomerId(), 
                        "START", null, null, "開始執行流程");

            // 取得起始節點
            var currentNodeOpt = flow.getStartNode();
            if (currentNodeOpt.isEmpty()) {
                throw new FlowExecutionException("找不到起始節點");
            }
            var currentNode = currentNodeOpt.get();
            
            while (currentNode != null) {
                // 執行決策節點
                if (currentNode.getNodeType() == NodeType.DECISION) {
                    var decisionNode = (DecisionNode) currentNode;
                    var decisionResult = executeDecision(decisionNode, context);
                    
                    logExecution(executionId, flow.getFlowId().getValue(), context.getCustomerId(),
                               "DECISION", currentNode.getNodeId(), String.valueOf(decisionResult), null);
                    
                    var nextNode = flow.getNextNode(currentNode.getNodeId(), decisionResult);
                    if (nextNode.isEmpty()) {
                        break;
                    }
                    currentNode = nextNode.get();
                }
                // 執行處理節點
                else if (currentNode.getNodeType() == NodeType.PROCESS) {
                    var processNode = (ProcessNode) currentNode;
                    var processResult = executeProcess(processNode, context);
                    
                    logExecution(executionId, flow.getFlowId().getValue(), context.getCustomerId(),
                               "PROCESS", currentNode.getNodeId(), processResult.isSuccess() ? "SUCCESS" : "FAILURE", null);
                    
                    var nextNode = flow.getNextNode(currentNode.getNodeId(), processResult.isSuccess());
                    if (nextNode.isEmpty()) {
                        break;
                    }
                    currentNode = nextNode.get();
                }
            }

            // 記錄完成執行
            logExecution(executionId, flow.getFlowId().getValue(), context.getCustomerId(),
                        "COMPLETE", null, "SUCCESS", "流程執行完成");

            return ExecutionResult.success(flow.getFlowId(), executionId, null);
            
        } catch (Exception e) {
            // 記錄執行失敗
            logExecution(executionId, flow.getFlowId().getValue(), context.getCustomerId(),
                        "ERROR", null, "ERROR", e.getMessage());
                        
            return ExecutionResult.failure(flow.getFlowId(), executionId, e.getMessage());
        }
    }
    
    @Override
    public void pause(Flow flow, BaseExecutionContext context) {
        // 原有的暫停邏輯
    }
    
    @Override
    public ExecutionResult resume(Flow flow, BaseExecutionContext context) {
        // 原有的繼續執行邏輯
        return null;  // TODO: 實作繼續執行邏輯
    }
    
    @Override
    public void terminate(Flow flow, BaseExecutionContext context) {
        // 原有的中止邏輯
    }
    
    @Override
    public String getExecutionStatus(Flow flow, BaseExecutionContext context) {
        // 原有的取得狀態邏輯
        return null;  // TODO: 實作取得狀態邏輯
    }

    @Override
    public FlowStatistics getFlowStatistics(String flowId, LocalDateTime startTime, LocalDateTime endTime) {
        FlowId id = FlowId.of(flowId);
        // 檢查流程是否存在
        if (!flowRepository.existsById(id)) {
            throw new FlowNotFoundException("找不到流程：" + flowId);
        }
        
        // 如果時間參數為null，設定預設值
        LocalDateTime effectiveStartTime = startTime != null ? startTime : LocalDateTime.now().minusDays(30);
        LocalDateTime effectiveEndTime = endTime != null ? endTime : LocalDateTime.now();
        
        // 從 ExecutionLogRepository 取得執行記錄
        List<ExecutionLog> logs = executionLogRepository.findByFlowIdAndExecutionTimeBetween(flowId, effectiveStartTime, effectiveEndTime);
        
        // 計算成功和失敗的執行數量
        long totalExecutions = logs.size();
        long successfulExecutions = logs.stream()
            .filter(log -> "SUCCESS".equals(log.getExecutionResult().toString()))
            .count();
        long failedExecutions = totalExecutions - successfulExecutions;
        
        // 計算平均、最大和最小執行時間
        Duration averageExecutionTime = calculateAverageExecutionTime(logs);
        Duration maxExecutionTime = calculateMaxExecutionTime(logs);
        Duration minExecutionTime = calculateMinExecutionTime(logs);
        
        // 建立並返回統計資訊
        return new FlowStatistics(
            totalExecutions,
            successfulExecutions,
            failedExecutions,
            averageExecutionTime,
            maxExecutionTime,
            minExecutionTime
        );
    }

    @Override
    public List<ExecutionDetails> getFlowExecutions(String flowId, LocalDateTime startTime, LocalDateTime endTime, 
                                                  String status, int pageSize, int pageNumber) {
        FlowId id = FlowId.of(flowId);
        // 檢查流程是否存在
        if (!flowRepository.existsById(id)) {
            throw new FlowNotFoundException("找不到流程：" + flowId);
        }
        
        // 如果時間參數為null，設定預設值
        LocalDateTime effectiveStartTime = startTime != null ? startTime : LocalDateTime.now().minusDays(30);
        LocalDateTime effectiveEndTime = endTime != null ? endTime : LocalDateTime.now();
        
        // 轉換為分頁參數
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        
        // 查詢執行記錄
        var executionLogs = executionLogRepository.findByFlowIdAndExecutionResultAndExecutionTimeBetween(
            flowId, status, effectiveStartTime, effectiveEndTime, pageRequest);
            
        // 將執行記錄轉換為 ExecutionDetails
        return executionLogs.getContent().stream()
            .map(this::convertToExecutionDetails)
            .collect(Collectors.toList());
    }

    @Override
    public ExecutionDetails getExecutionDetails(String executionId) {
        ExecutionLog log = executionLogRepository.findById(executionId)
            .orElseThrow(() -> new ExecutionNotFoundException("執行記錄不存在: " + executionId));
            
        return convertToExecutionDetails(log);
    }

    @Override
    public List<FlowStatistics> getAllFlowStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        List<Flow> flows = flowRepository.findAll();
        
        // 如果時間參數為null，設定預設值
        LocalDateTime effectiveStartTime = startTime != null ? startTime : LocalDateTime.now().minusDays(30);
        LocalDateTime effectiveEndTime = endTime != null ? endTime : LocalDateTime.now();
        
        return flows.stream()
            .map(flow -> getFlowStatistics(flow.getFlowId().getValue(), effectiveStartTime, effectiveEndTime))
            .collect(Collectors.toList());
    }

    @Override
    public ExecutionResult getExecutionResult(String executionId) {
        ExecutionLog log = executionLogRepository.findById(executionId)
            .orElseThrow(() -> new RuntimeException("執行結果不存在"));
            
        // 根據執行記錄建立ExecutionResult
        FlowId flowId = FlowId.of(log.getFlowId());
        if ("SUCCESS".equals(log.getExecutionResult().toString())) {
            return ExecutionResult.success(flowId, executionId, null);
        } else {
            return ExecutionResult.failure(flowId, executionId, log.getErrorMessage());
        }
    }

    @Override
    public void cancelExecution(String executionId) {
        // 檢查執行記錄是否存在
        executionLogRepository.findById(executionId)
            .orElseThrow(() -> new RuntimeException("執行記錄不存在"));
            
        // 這裡可以實作取消邏輯，暫時拋出異常表示無法取消
        throw new RuntimeException("無法取消執行");
    }

    private ExecutionDetails convertToExecutionDetails(ExecutionLog log) {
        return ExecutionDetails.builder()
            .executionId(log.getLogId())
            .flowId(log.getFlowId())
            .customerId(log.getCustomerId())
            .startTime(log.getExecutionTime())
            .executionTime(Duration.ofMillis(log.getExecutionDurationMs()))
            .status(log.getExecutionResult().toString())
            .errorMessage(log.getErrorMessage())
            .build();
    }

    private Duration calculateAverageExecutionTime(List<ExecutionLog> logs) {
        if (logs.isEmpty()) {
            return Duration.ZERO;
        }
        
        double avgMillis = logs.stream()
            .mapToInt(ExecutionLog::getExecutionDurationMs)
            .average()
            .orElse(0.0);
            
        return Duration.ofMillis(Math.round(avgMillis));
    }

    private Duration calculateMaxExecutionTime(List<ExecutionLog> logs) {
        if (logs.isEmpty()) {
            return Duration.ZERO;
        }
        
        OptionalInt maxMillis = logs.stream()
            .mapToInt(ExecutionLog::getExecutionDurationMs)
            .max();
            
        return maxMillis.isPresent() ? Duration.ofMillis(maxMillis.getAsInt()) : Duration.ZERO;
    }

    private Duration calculateMinExecutionTime(List<ExecutionLog> logs) {
        if (logs.isEmpty()) {
            return Duration.ZERO;
        }
        
        OptionalInt minMillis = logs.stream()
            .mapToInt(ExecutionLog::getExecutionDurationMs)
            .min();
            
        return minMillis.isPresent() ? Duration.ofMillis(minMillis.getAsInt()) : Duration.ZERO;
    }

    private String generateExecutionId() {
        return UUID.randomUUID().toString();
    }

    private void logExecution(String executionId, String flowId, String customerId, 
                            String type, String nodeId, String result, String message) {
        // 確保 nodeId 不為 null
        String safeNodeId = nodeId != null ? nodeId : "UNKNOWN";
        
        // 確保 type 是有效的 NodeType
        com.example.banking.benefit.domain.model.node.NodeType nodeType;
        try {
            nodeType = com.example.banking.benefit.domain.model.node.NodeType.valueOf(type);
        } catch (IllegalArgumentException e) {
            nodeType = com.example.banking.benefit.domain.model.node.NodeType.PROCESS;
        }
        
        ExecutionLog log = ExecutionLog.create(
            flowId,
            customerId,
            safeNodeId,
            nodeType,
            "SUCCESS".equals(result) ? com.example.banking.benefit.domain.model.log.ExecutionResult.PASS 
                                   : com.example.banking.benefit.domain.model.log.ExecutionResult.FAIL
        );
        log.setResultData(result);
        log.setErrorMessage(message);
        log.setExecutionDuration(0);
        
        executionLogRepository.save(log);
    }

    private boolean executeDecision(DecisionNode node, BaseExecutionContext context) {
        try {
            if (node.isSpelExpression()) {
                return evaluateSpelExpression(node.getSpelExpression(), context);
            } else {
                return executeJavaDecision(node.getImplementationClass(), context);
            }
        } catch (Exception e) {
            throw new DecisionEvaluationException("決策執行失敗：" + node.getNodeId(), e.getMessage(), e);
        }
    }

    private ProcessResult executeProcess(ProcessNode node, BaseExecutionContext context) {
        try {
            if (node.isSpelExpression()) {
                return evaluateProcessSpelExpression(node.getSpelExpression(), context);
            } else {
                return executeJavaProcess(node.getImplementationClass(), context);
            }
        } catch (Exception e) {
            throw new ProcessExecutionException("處理節點執行失敗：" + node.getNodeId(), e);
        }
    }

    private boolean evaluateSpelExpression(String expression, BaseExecutionContext context) {
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evalContext = createSecureContext();
        evalContext.setVariable("context", context);
        
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(evalContext, Boolean.class);
    }

    private ProcessResult evaluateProcessSpelExpression(String expression, BaseExecutionContext context) {
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evalContext = createSecureContext();
        evalContext.setVariable("context", context);
        
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(evalContext, ProcessResult.class);
    }

    private boolean executeJavaDecision(String className, BaseExecutionContext context) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
                   NoSuchMethodException, InvocationTargetException {
        Class<?> decisionClass = Class.forName(className);
        DecisionCommand command = (DecisionCommand) decisionClass.getDeclaredConstructor().newInstance();
        return command.evaluate(ExecutionContext.create(context.getFlowId(), context.getCustomerId(), context.getCustomerData()));
    }

    private ProcessResult executeJavaProcess(String className, BaseExecutionContext context) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException {
        Class<?> processClass = Class.forName(className);
        ProcessState state = (ProcessState) processClass.getDeclaredConstructor().newInstance();
        return state.execute(ExecutionContext.create(context.getFlowId(), context.getCustomerId(), context.getCustomerData()));
    }

    private StandardEvaluationContext createSecureContext() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setTypeLocator(new WhitelistTypeLocator(
            Arrays.asList(
                "java.lang.String",
                "java.lang.Integer",
                "java.lang.Long",
                "java.lang.Double",
                "java.lang.Boolean",
                "java.time.LocalDateTime",
                "java.time.LocalDate",
                "java.time.Duration"
            )
        ));
        return context;
    }
}