package com.ruoyi.flowable.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Flowable配置
 *
 * 可以通过设置 flowable.enabled=false 来禁用Flowable功能
 * 数据库 schema 更新策略请通过 application.yml 中 flowable.database-schema-update 控制
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

        // 注意：不要在代码里强制覆写 databaseSchemaUpdate，避免与配置文件冲突
        log.info("Flowable工作流引擎配置完成，databaseSchemaUpdate={}", engineConfiguration.getDatabaseSchemaUpdate());
    }
}
