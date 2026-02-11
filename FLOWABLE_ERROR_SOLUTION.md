# Flowable 启动错误解决方案

## 🚨 错误信息

```
org.flowable.common.engine.api.FlowableException: 
Could not update Flowable database schema: unknown version from database: '8.1.0'
```

## ✅ 快速解决方法

### 方法一：一键修复（推荐）

在命令行执行以下命令：

```bash
cd /home/runner/work/RuoYi-Vue/RuoYi-Vue

# 替换 your_database_name 为你的数据库名
# 替换 your_username 为你的MySQL用户名
mysql -u your_username -p your_database_name < sql/flowable_quick_fix.sql
```

然后重启应用：
```bash
mvn spring-boot:run
```

### 方法二：手动修复

1. **连接到MySQL数据库**

2. **检查当前版本**：
```sql
SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
SELECT * FROM ACT_ID_PROPERTY WHERE NAME_ = 'schema.version';
```

3. **如果看到版本号不是 6.8.1.0，执行以下更新**：
```sql
UPDATE ACT_GE_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
UPDATE ACT_ID_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
```

4. **重启应用**

### 方法三：全新安装（清空所有工作流数据）

如果你是在开发环境且不需要保留数据：

```sql
-- 使用提供的脚本删除所有Flowable表
source sql/flowable_fix_version.sql
-- 然后取消注释脚本中的 DROP TABLE 语句并执行

-- 或者手动删除所有 ACT_ 和 FLW_ 开头的表
```

重启应用后，Flowable会自动创建所有表。

## 🔍 问题原因

你的数据库中存在Flowable表，但版本号（'8.1.0'）与Flowable 6.8.1期望的版本号（'6.8.1.0'）不匹配。

可能的原因：
- 之前安装过不同版本的Flowable
- 数据库被其他Flowable应用使用过
- 版本号被意外修改

## ✅ 已经为你实施的代码修复

好消息！代码配置已经全部修复完成：

1. ✅ **FlowableConfig.java** - 已配置正确的数据库类型和更新策略
2. ✅ **application.yml** - 已添加MySQL数据库类型配置
3. ✅ **修复脚本** - 已创建多个SQL修复脚本

你只需要修复数据库状态即可！

## 📋 验证修复是否成功

执行以下SQL确认版本正确：

```sql
SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
-- 应该返回: 6.8.1.0

SELECT COUNT(*) FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%');
-- 应该返回: 大约70个表
```

应用启动后，测试API：
```
GET http://localhost:8080/flowable/process/list
```

## ⚠️ 重要提醒

### JDK 版本要求

本项目使用 **JDK 21**，请确保已安装：

```bash
java -version
# 应该显示: java version "21.x.x"
```

如果不是JDK 21，请下载安装：
- [Eclipse Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21)

### 配置说明

当前配置（已在代码中设置）：
```yaml
flowable:
  database-schema-update: true  # 自动创建/更新表
  database-type: mysql          # 明确指定MySQL
```

这意味着：
- ✅ 首次启动会自动创建所有表
- ✅ 版本升级时会自动更新表结构
- ✅ 不会删除已有数据
- ✅ 适合开发和生产环境

## 📚 相关文档

- **README_FLOWABLE_FIX.md** - 用户友好的修复指南
- **FLOWABLE_MYSQL_FIX.md** - 技术详细文档
- **sql/flowable_quick_fix.sql** - 一键修复脚本
- **sql/flowable_fix_version.sql** - 详细修复选项

## 🆘 仍然遇到问题？

如果以上方法都不能解决，请提供：

1. 完整的错误日志
2. MySQL版本：`SELECT VERSION();`
3. Java版本：`java -version`
4. 数据库中是否有Flowable表：
   ```sql
   SHOW TABLES LIKE 'ACT_%';
   ```
5. 当前版本号（如果表存在）：
   ```sql
   SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
   ```

## 💡 最简单的解决方法

**如果你是在开发环境，且不需要保留工作流数据：**

```bash
# 1. 删除数据库
mysql -u root -p -e "DROP DATABASE IF EXISTS your_database_name; CREATE DATABASE your_database_name CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 重新启动应用
mvn spring-boot:run
```

应用会自动创建所有表，版本号正确，一切正常！

---

**总结**：你只需要选择一个方法修复数据库版本号，然后重启应用即可。代码配置已经全部OK！
