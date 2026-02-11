# Flowable + MySQL 8.2.0 兼容性问题解决方案

## 问题描述

启动应用时出现以下错误：

```
org.flowable.common.engine.api.FlowableException: 
Could not update Flowable database schema: unknown version from database: '8.1.0'
```

## 问题原因

1. **数据库版本冲突**: Flowable 6.8.1在检查数据库时，发现已存在的Flowable表中的版本信息与期望的版本不匹配
2. **MySQL版本新**: MySQL 8.2.0是较新的版本，Flowable 6.8.1的版本检测逻辑可能不完全支持
3. **多数据库冲突**: 同一MySQL实例中如果有多个数据库都包含Flowable表，可能导致版本检查混乱

## 解决方案

### 方案1: 清空数据库重新初始化（开发环境推荐）

如果是开发环境或测试环境，可以直接删除所有Flowable表，让应用重新创建：

```sql
-- 执行 sql/flowable_fix_version.sql 中的DROP TABLE语句
-- 或者手动删除所有ACT_*和FLW_*开头的表
```

**步骤:**
1. 备份数据库（如果有重要数据）
2. 执行 `sql/flowable_fix_version.sql` 中的删除表SQL（取消注释）
3. 重启应用，Flowable会自动创建所有表

### 方案2: 更新数据库版本号（生产环境推荐）

如果已有数据需要保留，可以手动更新版本号：

```sql
-- 检查当前版本
SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
SELECT * FROM ACT_ID_PROPERTY WHERE NAME_ = 'schema.version';

-- 更新为Flowable 6.8.1期望的版本
UPDATE ACT_GE_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
UPDATE ACT_ID_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
```

**步骤:**
1. 备份数据库
2. 执行上述SQL更新版本号
3. 重启应用

### 方案3: 修改配置文件

已经在代码中做了以下修改：

**FlowableConfig.java** - 设置数据库类型和更新策略：
```java
// 配置数据库类型为MySQL
engineConfiguration.setDatabaseType("mysql");

// 设置数据库schema更新策略
engineConfiguration.setDatabaseSchemaUpdate(
    SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE
);
```

**application.yml** - 明确指定数据库配置：
```yaml
flowable:
  database-schema-update: true
  database-type: mysql
  database-table-prefix: ""
```

## 配置说明

### database-schema-update 选项

- `false`: 不进行任何检查，假设表已存在（需要手动创建表）
- `true`: 自动检查并更新表结构（**推荐，已配置**）
- `create-drop`: 启动时创建表，关闭时删除表（仅用于开发测试）
- `drop-create`: 先删除后创建（会清空所有数据）

当前配置为 `true`，这意味着：
- 如果表不存在，会自动创建
- 如果表存在但结构不同，会自动更新
- 如果版本号不匹配，会尝试升级

## 快速解决步骤

### 开发环境（无重要数据）

1. **删除所有Flowable表**:
   ```bash
   # 连接到MySQL
   mysql -u root -p
   
   # 使用你的数据库
   use your_database_name;
   
   # 执行删除脚本
   source sql/flowable_fix_version.sql
   # （取消注释DROP TABLE语句后执行）
   ```

2. **重启应用**:
   ```bash
   mvn clean spring-boot:run
   ```

3. Flowable会自动创建所有必需的表

### 生产环境（需要保留数据）

1. **备份数据库**:
   ```bash
   mysqldump -u root -p database_name > backup_$(date +%Y%m%d).sql
   ```

2. **更新版本号**:
   ```sql
   UPDATE ACT_GE_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
   UPDATE ACT_ID_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
   ```

3. **重启应用**

## 验证

启动成功后，检查以下内容：

1. **检查Flowable表是否创建**:
   ```sql
   SELECT TABLE_NAME 
   FROM information_schema.TABLES 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%')
   ORDER BY TABLE_NAME;
   ```
   应该看到约70-80个表

2. **检查版本号**:
   ```sql
   SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
   SELECT * FROM ACT_ID_PROPERTY WHERE NAME_ = 'schema.version';
   ```
   版本号应该是 `6.8.1.0`

3. **测试工作流功能**:
   - 访问: `http://localhost:8080/flowable/process/list`
   - 创建一个简单的流程定义
   - 启动一个流程实例

## 其他注意事项

### 多数据库冲突

如果同一MySQL实例中有多个数据库都包含Flowable表，可能导致版本检查混乱。

**解决方法**: 在 `application.yml` 中明确指定数据库：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_specific_database?useSSL=false&serverTimezone=Asia/Shanghai
```

### MySQL 8.x 认证问题

如果遇到认证错误，可能是因为MySQL 8.0+默认使用 `caching_sha2_password`，而不是旧的 `mysql_native_password`。

**解决方法**:
```sql
ALTER USER 'username'@'localhost' 
IDENTIFIED WITH mysql_native_password BY 'password';
FLUSH PRIVILEGES;
```

或者更新MySQL Connector/J到最新版本（已配置8.2.0）

## 相关文件

- `/sql/flowable_fix_version.sql` - 版本修复SQL脚本
- `/ruoyi-flowable/src/main/java/com/ruoyi/flowable/config/FlowableConfig.java` - Flowable配置
- `/ruoyi-admin/src/main/resources/application.yml` - 应用配置

## 参考资料

- [Flowable官方文档 - 数据库配置](https://www.flowable.com/open-source/docs/bpmn/ch05a-Spring-Boot)
- [MySQL 8.0 升级指南](https://dev.mysql.com/doc/refman/8.0/en/upgrading.html)
- [Flowable GitHub Issues](https://github.com/flowable/flowable-engine/issues)

## 总结

本次修改通过以下方式解决了MySQL 8.2.0兼容性问题：

1. ✅ 在 `FlowableConfig.java` 中明确设置数据库类型为MySQL
2. ✅ 配置数据库schema自动更新策略为 `true`
3. ✅ 在 `application.yml` 中添加明确的数据库配置
4. ✅ 提供SQL脚本用于清理或更新版本号
5. ✅ 提供详细的故障排除文档

现在应用应该可以正常启动，Flowable会自动处理数据库表的创建和更新。
