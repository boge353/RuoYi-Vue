# Flowable 故障排除完整指南

## 快速导航

根据您遇到的错误信息，选择对应的解决方案：

### 错误 1: NullPointerException: PropertyEntity.getValue() 为 null

**症状**:
```
java.lang.NullPointerException: Cannot invoke "PropertyEntity.getValue()" 
because "dbVersionProperty" is null
```

**原因**: Flowable 表不存在  
**解决方案**: 📖 [FLOWABLE_NPE_FIX.md](FLOWABLE_NPE_FIX.md)  
**快速修复**: 
```bash
mysql -u root -p < sql/init_flowable_database.sql
mvn spring-boot:run
```

---

### 错误 2: 未知数据库版本 '8.1.0'

**症状**:
```
FlowableException: Could not update Flowable database schema: 
unknown version from database: '8.1.0'
```

**原因**: MySQL 版本号冲突  
**解决方案**: 📖 [FLOWABLE_MYSQL_FIX.md](FLOWABLE_MYSQL_FIX.md)  
**快速修复**:
```sql
UPDATE ACT_GE_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
UPDATE ACT_ID_PROPERTY SET VALUE_ = '6.8.1.0' WHERE NAME_ = 'schema.version';
```

---

### 错误 3: 依赖冲突检查

**需求**: 扫描项目依赖冲突  
**结果**: ✅ 无冲突（已验证）  
**详情**: 
- Spring Boot 2.7.18
- Flowable 6.8.1
- MySQL Connector 8.2.0
- 所有版本兼容

**验证命令**:
```bash
mvn dependency:tree -Dverbose | grep conflict
```

---

## 完整文档结构

### 一级文档（快速入门）

#### 1. [QUICK_FIX.md](QUICK_FIX.md) ⚡
- **用途**: 超快速解决方案（< 2 分钟）
- **适合**: 需要立即启动应用
- **特点**: 最简单，直接命令

#### 2. [FLOWABLE_NPE_FIX.md](FLOWABLE_NPE_FIX.md) 🎯 NEW!
- **用途**: 解决 NullPointerException
- **适合**: 遇到 "dbVersionProperty is null" 错误
- **特点**: 专门针对表缺失问题

### 二级文档（详细说明）

#### 3. [FLOWABLE_ERROR_SOLUTION.md](FLOWABLE_ERROR_SOLUTION.md) 📋
- **用途**: 通用错误解决方案
- **适合**: 需要理解错误原因
- **特点**: 3种方法，详细步骤

#### 4. [README_FLOWABLE_FIX.md](README_FLOWABLE_FIX.md) 👥
- **用途**: 用户友好指南
- **适合**: 新手用户
- **特点**: 简单易懂，有环境要求说明

### 三级文档（技术深入）

#### 5. [FLOWABLE_MYSQL_FIX.md](FLOWABLE_MYSQL_FIX.md) 🔧
- **用途**: MySQL 兼容性技术文档
- **适合**: 需要深入理解
- **特点**: 完整技术分析

#### 6. [FLOWABLE_DOCS_INDEX.md](FLOWABLE_DOCS_INDEX.md) 📚
- **用途**: 文档导航索引
- **适合**: 浏览所有资源
- **特点**: 文档总览

### SQL 脚本工具

#### 7. [sql/init_flowable_database.sql](sql/init_flowable_database.sql) 🆕 NEW!
- **用途**: 数据库初始化
- **功能**: 创建数据库
- **使用**: `mysql -u root -p < sql/init_flowable_database.sql`

#### 8. [sql/verify_flowable_tables.sql](sql/verify_flowable_tables.sql) 🆕 NEW!
- **用途**: 验证表是否创建
- **功能**: 检查约70个Flowable表
- **使用**: `mysql -u root -p < sql/verify_flowable_tables.sql`

#### 9. [sql/flowable_quick_fix.sql](sql/flowable_quick_fix.sql)
- **用途**: 自动修复版本问题
- **功能**: 更新版本号到6.8.1.0
- **使用**: `mysql -u root -p ry-vue < sql/flowable_quick_fix.sql`

#### 10. [sql/flowable_fix_version.sql](sql/flowable_fix_version.sql)
- **用途**: 手动修复选项
- **功能**: 多种修复方案
- **使用**: 选择性执行

---

## 问题诊断流程图

```
启动应用失败
    ↓
查看错误信息
    ↓
┌─────────────────┬─────────────────┬──────────────────┐
│                 │                 │                  │
│  NullPointer    │  unknown        │  其他错误        │
│  "dbVersion     │  version        │                  │
│   Property"     │  '8.1.0'        │                  │
│                 │                 │                  │
└────────┬────────┴────────┬────────┴────────┬─────────┘
         │                 │                 │
         ↓                 ↓                 ↓
   FLOWABLE_NPE_FIX   FLOWABLE_MYSQL_FIX   检查日志
         │                 │                 │
         ↓                 ↓                 ↓
   创建数据库          更新版本号          查看文档索引
         │                 │                 │
         └─────────────────┴─────────────────┘
                          ↓
                    重启应用成功
```

---

## 按场景选择文档

### 场景 A: 首次启动应用

**问题**: 应用无法启动  
**可能错误**: NullPointerException

**解决路径**:
1. 📖 打开: [FLOWABLE_NPE_FIX.md](FLOWABLE_NPE_FIX.md)
2. 🛠️ 执行: `sql/init_flowable_database.sql`
3. ▶️ 启动: `mvn spring-boot:run`
4. ✅ 验证: `sql/verify_flowable_tables.sql`

**时间**: 2 分钟

---

### 场景 B: 数据库已存在但有问题

**问题**: 应用启动报版本错误  
**可能错误**: unknown version '8.1.0'

**解决路径**:
1. 📖 打开: [FLOWABLE_MYSQL_FIX.md](FLOWABLE_MYSQL_FIX.md)
2. 🛠️ 执行: `sql/flowable_quick_fix.sql`
3. ▶️ 启动: `mvn spring-boot:run`
4. ✅ 成功!

**时间**: 1 分钟

---

### 场景 C: 检查依赖冲突

**问题**: 怀疑有依赖冲突  
**已验证**: ✅ 无冲突

**验证结果**:
```bash
mvn dependency:tree -Dverbose | grep -E "(conflict|omitted)"
```

**结论**: 
- 所有依赖版本一致
- Spring Boot 2.7.18
- Flowable 6.8.1  
- MySQL Connector 8.2.0
- ✅ 无需任何修改

---

### 场景 D: 开发环境快速重建

**需求**: 快速清空重建  
**适用**: 开发测试环境

**解决路径**:
1. 📖 打开: [QUICK_FIX.md](QUICK_FIX.md)
2. 🗑️ 删除: `DROP DATABASE ry-vue;`
3. 🆕 创建: `CREATE DATABASE ry-vue;`
4. ▶️ 启动: 应用自动创建表

**时间**: 30 秒

---

### 场景 E: 生产环境安全升级

**需求**: 保留数据，安全升级  
**适用**: 生产环境

**解决路径**:
1. 📖 打开: [README_FLOWABLE_FIX.md](README_FLOWABLE_FIX.md)
2. 💾 备份: 数据库
3. 🔄 更新: 版本号（不删除数据）
4. ▶️ 启动: 验证功能
5. ✅ 完成: 回滚备份（如需要）

**时间**: 5 分钟（含备份）

---

## 快速参考命令

### 数据库操作

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS \`ry-vue\` DEFAULT CHARACTER SET utf8mb4;"

# 初始化（完整脚本）
mysql -u root -p < sql/init_flowable_database.sql

# 验证表
mysql -u root -p < sql/verify_flowable_tables.sql

# 修复版本
mysql -u root -p ry-vue < sql/flowable_quick_fix.sql

# 删除重建（小心！）
mysql -u root -p -e "DROP DATABASE IF EXISTS \`ry-vue\`; CREATE DATABASE \`ry-vue\` DEFAULT CHARACTER SET utf8mb4;"
```

### 应用操作

```bash
# 启动应用
cd /home/runner/work/RuoYi-Vue/RuoYi-Vue
mvn spring-boot:run

# 编译
mvn clean package -DskipTests

# 检查依赖
mvn dependency:tree

# 检查冲突
mvn dependency:tree -Dverbose | grep conflict
```

### 验证操作

```bash
# 测试API
curl http://localhost:8080/flowable/process/list

# 查看日志
tail -f logs/ruoyi.log

# 检查Java版本
java -version  # 应该是 21.x.x
```

---

## 故障排除检查清单

### ✅ 启动前检查

- [ ] JDK 21 已安装
- [ ] MySQL 服务正在运行
- [ ] 数据库 `ry-vue` 已创建
- [ ] 数据库密码正确（application-druid.yml）
- [ ] 网络连接正常（localhost:3306）

### ✅ 启动后检查

- [ ] 应用启动无错误
- [ ] 约70个Flowable表已创建
- [ ] 版本号为 6.8.1.0
- [ ] API可以访问
- [ ] 日志无异常

### ✅ 如果失败

1. 查看具体错误信息
2. 对照本文档找到对应错误
3. 按照对应文档操作
4. 验证修复
5. 如仍有问题，检查：
   - 日志详细信息
   - 数据库连接
   - 防火墙设置
   - 权限配置

---

## 技术支持资源

### 文档资源（本地）

- 8个markdown文档
- 4个SQL脚本
- 完整配置示例
- 故障排除指南

### 技术规格

- **Spring Boot**: 2.7.18
- **Flowable**: 6.8.1
- **MySQL**: 8.2.0
- **JDK**: 21
- **编码**: UTF-8
- **时区**: GMT+8

### 常用链接

- Flowable官方文档: https://www.flowable.com/open-source/docs
- Spring Boot文档: https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/
- MySQL文档: https://dev.mysql.com/doc/

---

## 更新日志

### 2026-02-11 (最新)

- ✅ 新增 `FLOWABLE_NPE_FIX.md` - NPE专项修复指南
- ✅ 新增 `sql/init_flowable_database.sql` - 数据库初始化脚本
- ✅ 新增 `sql/verify_flowable_tables.sql` - 验证脚本
- ✅ 完成依赖冲突扫描 - 确认无冲突
- ✅ 更新本索引文档

### 之前版本

- ✅ 创建 `FLOWABLE_MYSQL_FIX.md` - MySQL兼容性修复
- ✅ 创建 `QUICK_FIX.md` - 快速修复指南
- ✅ 创建 `FLOWABLE_ERROR_SOLUTION.md` - 错误解决方案
- ✅ 创建各种SQL修复脚本

---

## 总结

**所有Flowable相关问题的完整解决方案已提供**

- ✅ 配置正确
- ✅ 无依赖冲突
- ✅ 文档齐全
- ✅ 脚本ready

**用户只需要**:
1. 选择对应错误的文档
2. 执行简单命令
3. 重启应用
4. 验证成功

**总耗时**: 1-5分钟（取决于场景）

---

**开始**: 根据您的错误选择上述对应文档，按步骤操作即可！
