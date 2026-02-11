# Flowable NullPointerException 修复指南

## 错误信息

```
java.lang.NullPointerException: Cannot invoke "PropertyEntity.getValue()" 
because "dbVersionProperty" is null
```

## 根本原因

**Flowable 表不存在**

Flowable 尝试从 `ACT_GE_PROPERTY` 表中读取版本信息，但该表不存在，导致返回 null，进而引发 NullPointerException。

### 为什么会发生？

1. **数据库不存在** - `ry-vue` 数据库未创建
2. **表未初始化** - 数据库存在但 Flowable 表未创建
3. **权限问题** - 数据库用户没有创建表的权限
4. **配置错误** - 数据库连接配置有误

## 依赖冲突检查结果 ✅

已完成全项目依赖扫描，**未发现任何冲突**：

- Spring Boot: 2.7.18 ✅
- Flowable: 6.8.1 ✅  
- MySQL Connector: 8.2.0 ✅
- Spring Framework: 5.3.39 ✅
- 所有传递依赖版本一致 ✅

**结论**: 不是依赖冲突问题，是数据库表缺失问题。

## 快速解决方案

### 方案 1: 创建数据库并自动初始化表（推荐）⚡

这是最简单的方法，让 Flowable 自动创建所有表。

#### 步骤：

1. **创建数据库**
```bash
mysql -u root -p < sql/init_flowable_database.sql
```

或手动执行：
```sql
CREATE DATABASE IF NOT EXISTS `ry-vue` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. **确认数据库密码**

编辑 `ruoyi-admin/src/main/resources/application-druid.yml`:
```yaml
master:
  url: jdbc:mysql://localhost:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
  username: root
  password: 你的实际密码  # ⚠️ 修改这里
```

3. **启动应用**
```bash
cd /home/runner/work/RuoYi-Vue/RuoYi-Vue
mvn spring-boot:run
```

4. **验证**
```bash
mysql -u root -p < sql/verify_flowable_tables.sql
```

应该看到约 70 个 Flowable 表被创建。

### 方案 2: 检查现有数据库

如果数据库已存在但应用仍报错：

1. **验证数据库存在**
```sql
SHOW DATABASES LIKE 'ry-vue';
```

2. **检查表是否存在**
```sql
USE `ry-vue`;
SELECT COUNT(*) FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'ry-vue' 
  AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%');
```

3. **如果表不存在（计数为0）**
   - Flowable 自动创建失败
   - 可能是权限问题

4. **授予权限**
```sql
GRANT ALL PRIVILEGES ON `ry-vue`.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

5. **重启应用**

### 方案 3: 清空重建

如果之前有残留的错误表：

```sql
-- 小心！这会删除所有数据
DROP DATABASE IF EXISTS `ry-vue`;
CREATE DATABASE `ry-vue` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

然后启动应用，Flowable 会自动创建所有表。

## 配置验证

确保以下配置正确：

### 1. FlowableConfig.java ✅
```java
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {
    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setDatabaseType("mysql");
        engineConfiguration.setDatabaseSchemaUpdate(
            SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE
        );
    }
}
```

### 2. application.yml ✅
```yaml
flowable:
  database-schema-update: true
  database-type: mysql
```

### 3. application-druid.yml ✅
```yaml
spring:
  datasource:
    driverClassName: com.mysql.cj.jdbc.Driver
    druid:
      master:
        url: jdbc:mysql://localhost:3306/ry-vue?...
        username: root
        password: your_password  # 确保正确
```

## 验证成功标志

应用启动成功后，检查：

1. **日志中应该看到**
```
INFO  o.f.engine.impl.ProcessEngineImpl - ProcessEngine default created
INFO  org.flowable.engine.impl.bpmn.deployer.BpmnDeployer - Processing resource ...
```

2. **数据库中应该有约 70 个表**
```sql
SELECT COUNT(*) FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'ry-vue' 
AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%');
-- 返回: 约 70
```

3. **版本表有正确记录**
```sql
SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
-- 返回: VALUE_ = '6.8.1.0'
```

4. **API 可以访问**
```bash
curl http://localhost:8080/flowable/process/list
```

## 常见问题

### Q1: 数据库连接被拒绝
```
Communications link failure
```

**解决**:
- 检查 MySQL 是否运行: `systemctl status mysql` 或 `ps aux | grep mysql`
- 检查端口: `netstat -an | grep 3306`
- 检查防火墙设置

### Q2: 权限被拒绝
```
Access denied for user 'root'@'localhost'
```

**解决**:
```sql
-- 重置密码
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;
```

### Q3: 表已存在但版本错误
```
unknown version from database: '8.1.0'
```

**解决**: 参考之前的 `FLOWABLE_MYSQL_FIX.md` 文档更新版本号。

### Q4: 仍然是 NullPointerException

**检查清单**:
1. ✅ 数据库 `ry-vue` 存在
2. ✅ 用户有 CREATE TABLE 权限
3. ✅ 密码正确
4. ✅ MySQL 服务运行中
5. ✅ 配置中的数据库名正确

如果全部正确，尝试：
```bash
# 启用详细日志
export JAVA_OPTS="-Dlogging.level.org.flowable=DEBUG"
mvn spring-boot:run
```

查看详细错误信息。

## 总结

**问题**: Flowable 表不存在  
**原因**: 数据库未初始化  
**解决**: 创建数据库 → 启动应用 → 自动创建表  
**时间**: < 2 分钟  

### 最快解决路径

```bash
# 1. 创建数据库 (10 秒)
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS \`ry-vue\` DEFAULT CHARACTER SET utf8mb4;"

# 2. 确认密码正确 (10 秒)
# 编辑 application-druid.yml

# 3. 启动应用 (30 秒)
mvn spring-boot:run

# 4. 验证 (10 秒)
mysql -u root -p < sql/verify_flowable_tables.sql
```

**总计**: 1 分钟搞定！

## 相关文档

- `sql/init_flowable_database.sql` - 数据库初始化脚本
- `sql/verify_flowable_tables.sql` - 验证脚本
- `FLOWABLE_MYSQL_FIX.md` - MySQL 版本兼容性修复
- `QUICK_FIX.md` - 快速修复指南
- `FLOWABLE_DOCS_INDEX.md` - 文档索引
