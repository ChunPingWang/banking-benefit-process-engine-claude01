package com.example.banking.benefit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.example.banking.benefit.domain.service.common.SpelExpressionExecutor;

/**
 * Spring 應用程式設定
 */
@Configuration
@EnableAspectJAutoProxy
public class ApplicationConfig {
    
    @Bean
    public SpelExpressionExecutor spelExpressionExecutor() {
        return new SpelExpressionExecutor();
    }
}