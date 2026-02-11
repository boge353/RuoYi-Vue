package com.ruoyi.flowable.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Flowable配置 - 解决MySQL 8.2.0兼容性问题
 * 
 * 可以通过设置 flowable.enabled=false 来禁用Flowable功能
 * 
 * @author ruoyi
 */
@Configuration
@ConditionalOnProperty(prefix = "flowable", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration>
{
    private static final Logger log = LoggerFactory.getLogger(FlowableConfig.class);
    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration)
    {
        log.info("正在初始化Flowable工作流引擎...");
        
        // 设置字体，避免中文乱码
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
        
        // 配置数据库类型为MySQL
        engineConfiguration.setDatabaseType("mysql");
        
        // 设置数据库schema更新策略为create-drop，这会在每次启动时重新创建表
        // 如果需要保留数据，改为 "true" 即可
        // 选项说明:
        // - "false": 不进行任何检查，假设表已存在
        // - "true": 自动检查并更新表结构（推荐用于生产环境）
        // - "create-drop": 启动时创建表，关闭时删除表（仅用于开发测试）
        // - "drop-create": 先删除后创建（清空所有数据）
        engineConfiguration.setDatabaseSchemaUpdate(SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        
        log.info("Flowable工作流引擎配置完成");
    }
}
