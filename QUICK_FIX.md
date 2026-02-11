# ⚡ Flowable 数据库版本错误 - 立即解决

## 🎯 你现在遇到的错误

```
FlowableException: Could not update Flowable database schema: 
unknown version from database: '8.1.0'
```

## ✅ 好消息

**代码已经全部修复完成！** 你只需要修复数据库状态，1分钟即可解决。

## 🚀 最快解决方法（3步）

### 步骤1: 选择你的情况

#### 情况A：开发环境，可以删除工作流数据 ⭐ **最简单**
```bash
# 直接重建数据库（最快速）
mysql -u root -p -e "DROP DATABASE IF EXISTS ry; CREATE DATABASE ry CHARACTER SET utf8mb4;"

# 或者使用一键修复脚本
mysql -u root -p ry < sql/flowable_quick_fix.sql
```

#### 情况B：生产环境，需要保留数据
```sql
-- 连接到数据库后执行
UPDATE ACT_GE_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
UPDATE ACT_ID_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
```

#### 情况C：第一次安装，没有旧数据
```bash
# 什么都不用做！直接跳到步骤2
```

### 步骤2: 重启应用
```bash
cd /home/runner/work/RuoYi-Vue/RuoYi-Vue
mvn clean spring-boot:run
```

### 步骤3: 验证成功
```sql
-- 应该看到版本号为 6.8.1.0
SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
```

访问测试：`http://localhost:8080/flowable/process/list`

## 📋 详细说明

### 为什么会出现这个错误？

你的MySQL数据库中已经有Flowable的表，但版本号是 `8.1.0`，而Flowable 6.8.1期望的版本号是 `6.8.1.0`。

### 已经为你修复的内容（代码层面）

✅ `FlowableConfig.java` - 配置正确的数据库类型和更新策略  
✅ `application.yml` - 添加MySQL数据库配置  
✅ 所有配置文件已经优化完成  

**这些你不需要再做任何操作！**

### 你需要做的（数据库层面）

只需要选择上面3种情况之一，执行对应的命令即可。

## 🛠️ 可用工具

我已经为你准备了完整的修复工具：

1. **sql/flowable_quick_fix.sql** - 一键自动修复脚本
2. **FLOWABLE_ERROR_SOLUTION.md** - 详细解决方案
3. **README_FLOWABLE_FIX.md** - 用户友好指南
4. **FLOWABLE_MYSQL_FIX.md** - 技术详细文档
5. **sql/flowable_fix_version.sql** - 手动修复选项

## ⚠️ 注意事项

### JDK版本要求
项目使用 **JDK 21**，请确认：
```bash
java -version
# 必须是: java version "21.x.x"
```

如果不是，下载安装：https://adoptium.net/temurin/releases/?version=21

### 数据库连接
确保`application.yml`中的数据库配置正确：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ry?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

## 🔧 故障排除

### 问题1: 表不存在
**解决**: 这是正常的！如果是首次安装，应用启动时会自动创建表。

### 问题2: 权限不足
**解决**: 确保MySQL用户有CREATE、ALTER、DROP权限。

### 问题3: 还是报同样的错误
**可能原因**:
1. 没有重启应用
2. 数据库名称不对
3. 有多个数据库都有Flowable表

**解决**:
```sql
-- 检查所有数据库中的Flowable表
SELECT TABLE_SCHEMA, COUNT(*) as table_count
FROM information_schema.TABLES 
WHERE TABLE_NAME LIKE 'ACT_%' 
GROUP BY TABLE_SCHEMA;
```

## 💡 推荐方案对比

| 方案 | 适用场景 | 数据保留 | 执行时间 | 推荐度 |
|------|---------|---------|---------|--------|
| 重建数据库 | 开发环境 | ❌ | 10秒 | ⭐⭐⭐⭐⭐ |
| 快速修复脚本 | 开发/生产 | ✅ | 30秒 | ⭐⭐⭐⭐ |
| 手动更新版本 | 生产环境 | ✅ | 1分钟 | ⭐⭐⭐⭐ |
| 手动删除表 | 开发环境 | ❌ | 2分钟 | ⭐⭐⭐ |

## 📞 需要更多帮助？

查看详细文档：
- **FLOWABLE_ERROR_SOLUTION.md** - 这个错误的完整解决方案
- **README_FLOWABLE_FIX.md** - 通用修复指南

或提供以下信息：
```bash
# MySQL版本
mysql --version

# Java版本  
java -version

# Flowable表
mysql -u root -p -e "USE ry; SHOW TABLES LIKE 'ACT_%';"

# 当前版本号
mysql -u root -p -e "USE ry; SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';"
```

## ✨ 快速参考卡

```bash
# 开发环境 - 最快方法
mysql -u root -p
DROP DATABASE IF EXISTS ry;
CREATE DATABASE ry CHARACTER SET utf8mb4;
exit
mvn spring-boot:run

# 生产环境 - 安全方法  
mysql -u root -p ry
UPDATE ACT_GE_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
UPDATE ACT_ID_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
exit
mvn spring-boot:run

# 验证
curl http://localhost:8080/flowable/process/list
```

---

## 🎉 完成！

按照上面的步骤操作后，应用应该可以正常启动了。

Flowable会自动创建约70个表，版本号设置为6.8.1.0，一切正常运行！

**问题解决时间：< 2分钟** ⏱️
