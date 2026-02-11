# 问题解决总结 - Flowable启动失败

## 您遇到的问题

启动应用时出现以下错误：
```
org.flowable.common.engine.api.FlowableException: 
Could not update Flowable database schema: unknown version from database: '8.1.0'
```

## 已经为您实施的修复

我已经对代码进行了以下修改来解决这个问题：

### 1. ✅ 更新了 Flowable 配置
**文件**: `ruoyi-flowable/src/main/java/com/ruoyi/flowable/config/FlowableConfig.java`

- 明确设置数据库类型为 MySQL
- 配置自动更新数据库表结构
- 添加了详细的配置说明注释

### 2. ✅ 增强了应用配置
**文件**: `ruoyi-admin/src/main/resources/application.yml`

- 添加了 `database-type: mysql` 配置
- 添加了 `database-table-prefix: ""` 配置
- 确保数据库自动更新功能开启

### 3. ✅ 创建了数据库修复脚本
**文件**: `sql/flowable_fix_version.sql`

提供了三种修复方案的SQL脚本：
- 删除所有Flowable表（适合开发环境）
- 更新版本号（适合生产环境）
- 诊断查询（检查当前状态）

### 4. ✅ 编写了详细的解决方案文档
**文件**: `FLOWABLE_MYSQL_FIX.md`

包含完整的：
- 问题描述和原因分析
- 三种解决方案的详细步骤
- 验证方法
- 常见问题解答

## 您现在需要做什么

根据您的情况选择以下方案之一：

### 方案A: 如果您是第一次启动（推荐）

**什么都不用做，直接重启应用！**

配置已经修改好了，Flowable会自动：
1. 检测数据库中没有表
2. 创建所有需要的表（约70个）
3. 设置正确的版本号 (6.8.1.0)
4. 成功启动

```bash
# 直接启动应用
cd /path/to/RuoYi-Vue
mvn spring-boot:run
```

### 方案B: 如果数据库中已经有Flowable表

#### B1. 开发环境（可以清空数据）

1. **连接到MySQL数据库**
2. **执行删除脚本**:
   ```bash
   mysql -u your_username -p your_database_name
   ```
   
3. **在MySQL中执行**:
   ```sql
   source sql/flowable_fix_version.sql
   ```
   
4. **取消注释文件中的 DROP TABLE 语句并执行**

5. **重启应用**

#### B2. 生产环境（需要保留数据）

1. **备份数据库**:
   ```bash
   mysqldump -u root -p your_database_name > backup_$(date +%Y%m%d).sql
   ```

2. **连接MySQL并执行**:
   ```sql
   -- 更新版本号
   UPDATE ACT_GE_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
   UPDATE ACT_ID_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
   ```

3. **重启应用**

## 验证是否成功

启动成功后，您应该看到：

1. **应用正常启动**，没有 Flowable 相关错误

2. **数据库中创建了约70个Flowable表**:
   ```sql
   SELECT COUNT(*) FROM information_schema.TABLES 
   WHERE TABLE_SCHEMA = DATABASE() 
   AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%');
   ```

3. **版本号正确**:
   ```sql
   SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
   -- 应该返回: 6.8.1.0
   ```

4. **可以访问工作流API**:
   ```
   http://localhost:8080/flowable/process/list
   ```

## 重要提醒

### ⚠️ JDK 版本要求

本项目已升级到 **JDK 21**，请确保您的环境中安装了正确版本：

```bash
java -version
# 应该显示: openjdk version "21.x.x" 或 java version "21.x.x"
```

如果不是 JDK 21，请下载安装：
- **推荐**: [Eclipse Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21)
- 下载后设置 JAVA_HOME 环境变量

### 📝 配置说明

当前配置使用 `database-schema-update: true`，这意味着：
- ✅ 如果表不存在，会自动创建
- ✅ 如果表结构需要更新，会自动更新
- ✅ 不会删除现有数据
- ✅ 适合开发和生产环境

## 如果仍然遇到问题

请查看详细文档：
1. **FLOWABLE_MYSQL_FIX.md** - Flowable数据库兼容性完整指南
2. **sql/flowable_fix_version.sql** - 数据库修复SQL脚本

或者提供以下信息：
- 完整的错误日志
- 数据库中是否已有Flowable表
- MySQL版本 (`SELECT VERSION();`)
- Java版本 (`java -version`)

## 技术细节（供参考）

### 问题根本原因
Flowable 6.8.1在启动时会检查数据库中的版本信息（存储在 `ACT_GE_PROPERTY` 和 `ACT_ID_PROPERTY` 表中）。如果版本字符串不匹配或无法识别，就会抛出错误。

### 修复原理
通过配置明确告诉Flowable：
1. 使用MySQL数据库
2. 启用自动schema管理
3. 允许自动创建和更新表结构

这样Flowable就能正确处理MySQL 8.2.0，并自动管理数据库表。

## 相关文档

- 📄 `FLOWABLE_MYSQL_FIX.md` - 详细的故障排除指南
- 📄 `sql/flowable_fix_version.sql` - 数据库修复脚本
- 📄 `BPMN_INTEGRATION_GUIDE.md` - BPMN工作流集成指南
- 📄 `WORKFLOW_INTEGRATION.md` - 工作流系统使用说明

---

**总结**: 代码已经修复完成，您只需要根据自己的情况选择方案A或B，然后重启应用即可！
