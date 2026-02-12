package com.ruoyi.flowable.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

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
 *
 * @author ruoyi
 */
@Configuration
@ConditionalOnProperty(prefix = "flowable", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration>
{
    private static final Logger log = LoggerFactory.getLogger(FlowableConfig.class);
    private static final String FLOWABLE_VERSION = "6.8.1.0";

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

        // 根据数据库实际状态动态决策，避免“缺少版本记录”与“重复升级DDL”两类启动失败
        String schemaUpdateStrategy = resolveSchemaUpdateStrategy(engineConfiguration.getDataSource());
        engineConfiguration.setDatabaseSchemaUpdate(schemaUpdateStrategy);

        log.info("Flowable工作流引擎配置完成，databaseSchemaUpdate={}", engineConfiguration.getDatabaseSchemaUpdate());
    }

    private String resolveSchemaUpdateStrategy(DataSource dataSource)
    {
        try (Connection connection = dataSource.getConnection())
        {
            if (!tableExists(connection, "ACT_GE_PROPERTY"))
            {
                log.warn("未检测到ACT_GE_PROPERTY，启用自动建表/升级(databaseSchemaUpdate=true)");
                return SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;
            }

            String schemaVersion = queryProperty(connection, "schema.version");
            boolean variable620ColumnsReady = columnExists(connection, "ACT_RU_VARIABLE", "SCOPE_ID_")
                    && columnExists(connection, "ACT_RU_VARIABLE", "SUB_SCOPE_ID_")
                    && columnExists(connection, "ACT_RU_VARIABLE", "SCOPE_TYPE_");

            // 库内结构已是6.2+，但版本号缺失/滞后，会触发重复升级；自动修复版本标记后关闭升级
            if ((schemaVersion == null || !FLOWABLE_VERSION.equals(schemaVersion)) && variable620ColumnsReady)
            {
                upsertProperty(connection, "schema.version", FLOWABLE_VERSION);
                upsertProperty(connection, "schema.history", "create(6.8.1.0)");
                log.warn("检测到Flowable表结构已包含6.2+字段，已修复schema.version为{}，关闭自动升级", FLOWABLE_VERSION);
                return SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE;
            }

            // 版本未知且结构不完整，交给Flowable自动初始化
            if (schemaVersion == null)
            {
                log.warn("schema.version缺失且表结构不完整，启用自动建表/升级(databaseSchemaUpdate=true)");
                return SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;
            }

            // 版本号正常时，默认不重复升级
            return SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE;
        }
        catch (Exception e)
        {
            log.error("检测Flowable数据库状态失败，回退到自动升级策略(databaseSchemaUpdate=true)", e);
            return SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException
    {
        String sql = "SELECT COUNT(1) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean columnExists(Connection connection, String tableName, String columnName) throws SQLException
    {
        String sql = "SELECT COUNT(1) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private String queryProperty(Connection connection, String name) throws SQLException
    {
        String sql = "SELECT VALUE_ FROM ACT_GE_PROPERTY WHERE NAME_ = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    private void upsertProperty(Connection connection, String name, String value) throws SQLException
    {
        String updateSql = "UPDATE ACT_GE_PROPERTY SET VALUE_ = ?, REV_ = IFNULL(REV_, 1) + 1 WHERE NAME_ = ?";
        try (PreparedStatement update = connection.prepareStatement(updateSql))
        {
            update.setString(1, value);
            update.setString(2, name);
            int affected = update.executeUpdate();
            if (affected > 0)
            {
                return;
            }
        }

        String insertSql = "INSERT INTO ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES (?, ?, 1)";
        try (PreparedStatement insert = connection.prepareStatement(insertSql))
        {
            insert.setString(1, name);
            insert.setString(2, value);
            insert.executeUpdate();
        }
    }
}
