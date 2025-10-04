package com.example.banking.benefit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BankingBenefitProcessEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingBenefitProcessEngineApplication.class, args);
    }
}