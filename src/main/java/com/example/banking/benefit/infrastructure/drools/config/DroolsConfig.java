package com.example.banking.benefit.infrastructure.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

/**
 * Drools 規則引擎配置
 * 負責初始化 KieServices、KieContainer 和 KieSession
 */
@Configuration
public class DroolsConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DroolsConfig.class);
    
    private final KieServices kieServices;
    private final KieContainer kieContainer;
    
    public DroolsConfig() {
        this.kieServices = KieServices.Factory.get();
        this.kieContainer = createKieContainer();
    }
    
    @PostConstruct
    public void init() {
        logger.info("Drools 規則引擎初始化完成");
        logger.info("可用的 KieBase: {}", kieContainer.getKieBaseNames());
        logger.info("可用的 KieSession: {}", kieContainer.getKieSessionNamesInKieBase("rules"));
    }
    
    @Bean
    public KieServices kieServices() {
        return kieServices;
    }
    
    @Bean
    public KieContainer kieContainer() {
        return kieContainer;
    }
    
    @Bean
    public KieSession defaultKieSession() {
        return kieContainer.newKieSession("ksession-rules");
    }
    
    @Bean("customerKieSession")
    public KieSession customerKieSession() {
        return kieContainer.newKieSession("ksession-customer");
    }
    
    @Bean("productKieSession")
    public KieSession productKieSession() {
        return kieContainer.newKieSession("ksession-product");
    }
    
    @Bean("flowKieSession")
    public KieSession flowKieSession() {
        return kieContainer.newKieSession("ksession-flow");
    }
    
    /**
     * 創建 KieContainer
     */
    private KieContainer createKieContainer() {
        try {
            // 創建 KieFileSystem
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
            
            // 加載規則文件
            loadRuleFiles(kieFileSystem);
            
            // 創建 KieBuilder
            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();
            
            // 檢查編譯錯誤
            if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
                logger.error("規則編譯錯誤: {}", kieBuilder.getResults().toString());
                throw new RuntimeException("規則編譯失敗: " + kieBuilder.getResults().toString());
            }
            
            // 獲取 ReleaseId 並創建 KieContainer
            ReleaseId releaseId = kieBuilder.getKieModule().getReleaseId();
            return kieServices.newKieContainer(releaseId);
            
        } catch (Exception e) {
            logger.error("創建 KieContainer 失敗", e);
            throw new RuntimeException("Drools 初始化失敗", e);
        }
    }
    
    /**
     * 加載規則文件
     */
    private void loadRuleFiles(KieFileSystem kieFileSystem) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        
        // 加載所有 .drl 文件
        Resource[] resources = resolver.getResources("classpath*:rules/**/*.drl");
        
        logger.info("找到 {} 個規則文件", resources.length);
        
        for (Resource resource : resources) {
            String path = "src/main/resources/" + resource.getURL().toString()
                    .substring(resource.getURL().toString().indexOf("rules/"));
            
            logger.debug("加載規則文件: {}", path);
            
            kieFileSystem.write(path, 
                    ResourceFactory.newInputStreamResource(resource.getInputStream()));
        }
        
        // 如果沒有找到規則文件，創建一個默認的空規則
        if (resources.length == 0) {
            logger.warn("未找到任何規則文件，創建默認空規則");
            createDefaultRules(kieFileSystem);
        }
    }
    
    /**
     * 創建默認規則（當沒有規則文件時）
     */
    private void createDefaultRules(KieFileSystem kieFileSystem) {
        String defaultRule = """
            package rules;
            
            rule "Default Rule"
            when
                // 默認規則，始終為真
            then
                // 無操作
            end
            """;
        
        kieFileSystem.write("src/main/resources/rules/default.drl", defaultRule);
    }
}