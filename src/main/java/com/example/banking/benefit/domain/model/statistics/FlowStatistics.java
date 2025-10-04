package com.example.banking.benefit.domain.model.statistics;

import java.time.Duration;

/**
 * 流程統計資訊
 */
public class FlowStatistics {
    private long totalExecutions;
    private long successfulExecutions;
    private long failedExecutions;
    private Duration averageExecutionTime;
    private Duration maxExecutionTime;
    private Duration minExecutionTime;
    private double successRate;
    
    public FlowStatistics(
            long totalExecutions,
            long successfulExecutions,
            long failedExecutions,
            Duration averageExecutionTime,
            Duration maxExecutionTime,
            Duration minExecutionTime
    ) {
        this.totalExecutions = totalExecutions;
        this.successfulExecutions = successfulExecutions;
        this.failedExecutions = failedExecutions;
        this.averageExecutionTime = averageExecutionTime;
        this.maxExecutionTime = maxExecutionTime;
        this.minExecutionTime = minExecutionTime;
        this.successRate = totalExecutions > 0 
            ? (double) successfulExecutions / totalExecutions * 100 
            : 0.0;
    }
    
    public long getTotalExecutions() {
        return totalExecutions;
    }
    
    public long getSuccessfulExecutions() {
        return successfulExecutions;
    }
    
    public long getFailedExecutions() {
        return failedExecutions;
    }
    
    public Duration getAverageExecutionTime() {
        return averageExecutionTime;
    }
    
    public Duration getMaxExecutionTime() {
        return maxExecutionTime;
    }
    
    public Duration getMinExecutionTime() {
        return minExecutionTime;
    }
    
    public double getSuccessRate() {
        return successRate;
    }
}