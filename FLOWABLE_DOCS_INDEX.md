# Flowable 文档索引

## 🚨 遇到启动错误？

如果你看到这个错误：
```
FlowableException: Could not update Flowable database schema: unknown version from database: '8.1.0'
```

**👉 立即查看**: [QUICK_FIX.md](QUICK_FIX.md) - 2分钟解决！

---

## �� 完整文档列表

### 🔥 快速解决方案（按推荐顺序）

1. **[QUICK_FIX.md](QUICK_FIX.md)** ⚡ 最快
   - 3步解决方案
   - < 2分钟修复
   - 适合：遇到错误需要立即解决

2. **[FLOWABLE_ERROR_SOLUTION.md](FLOWABLE_ERROR_SOLUTION.md)** 🎯 详细
   - 多种解决方法
   - 详细步骤说明
   - 适合：需要了解不同方案

3. **[README_FLOWABLE_FIX.md](README_FLOWABLE_FIX.md)** 👥 友好
   - 用户友好指南
   - JDK要求说明
   - 适合：想要全面了解修复过程

4. **[FLOWABLE_MYSQL_FIX.md](FLOWABLE_MYSQL_FIX.md)** 🔧 技术
   - 技术深度分析
   - 根本原因解释
   - 适合：想了解技术细节

### 🛠️ SQL修复脚本

1. **[sql/flowable_quick_fix.sql](sql/flowable_quick_fix.sql)** 🤖 自动
   - 一键自动修复
   - 诊断+修复+验证
   - 使用: `mysql -u root -p database < sql/flowable_quick_fix.sql`

2. **[sql/flowable_fix_version.sql](sql/flowable_fix_version.sql)** 📝 手动
   - 多种手动方案
   - 删除表或更新版本
   - 适合：需要精确控制

### 📖 集成和使用指南

1. **[BPMN_INTEGRATION_GUIDE.md](BPMN_INTEGRATION_GUIDE.md)** 📚
   - BPMN工作流集成
   - bpmn.js使用指南
   - API文档

2. **[WORKFLOW_INTEGRATION.md](WORKFLOW_INTEGRATION.md)** 🔄
   - 工作流系统说明
   - 功能介绍

3. **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** 📊
   - 完整实施总结
   - 技术栈说明

### 📦 其他相关文档

1. **[LUCKYSHEET_INTEGRATION_GUIDE.md](LUCKYSHEET_INTEGRATION_GUIDE.md)** 📊
   - Luckysheet表格集成

2. **[CELL_PERMISSION_GUIDE.md](CELL_PERMISSION_GUIDE.md)** 🔐
   - 单元格权限控制

---

## 🎯 按场景选择文档

### 场景1: 应用启动失败，报数据库版本错误
➡️ [QUICK_FIX.md](QUICK_FIX.md)

### 场景2: 想了解如何修复数据库问题
➡️ [FLOWABLE_ERROR_SOLUTION.md](FLOWABLE_ERROR_SOLUTION.md)

### 场景3: 需要技术细节和原理
➡️ [FLOWABLE_MYSQL_FIX.md](FLOWABLE_MYSQL_FIX.md)

### 场景4: 想使用自动化脚本修复
➡️ [sql/flowable_quick_fix.sql](sql/flowable_quick_fix.sql)

### 场景5: 学习工作流功能使用
➡️ [BPMN_INTEGRATION_GUIDE.md](BPMN_INTEGRATION_GUIDE.md)

---

## 💡 快速命令参考

### 开发环境快速修复（最快）
```bash
mysql -u root -p -e "DROP DATABASE IF EXISTS ry; CREATE DATABASE ry;"
mvn spring-boot:run
```

### 生产环境安全修复
```sql
UPDATE ACT_GE_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
UPDATE ACT_ID_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
```

### 自动化修复
```bash
mysql -u root -p ry < sql/flowable_quick_fix.sql
mvn spring-boot:run
```

---

## ⚠️ 重要提醒

- **JDK版本**: 项目使用 JDK 21
- **MySQL版本**: 支持 MySQL 8.2.0
- **Flowable版本**: 6.8.1
- **Spring Boot版本**: 2.7.18

---

## 📞 需要帮助？

1. 先查看 [QUICK_FIX.md](QUICK_FIX.md)
2. 如果还有问题，查看 [FLOWABLE_ERROR_SOLUTION.md](FLOWABLE_ERROR_SOLUTION.md)
3. 需要深入了解，查看 [FLOWABLE_MYSQL_FIX.md](FLOWABLE_MYSQL_FIX.md)

## 🎉 祝你成功！

按照文档操作，问题很快就能解决！
