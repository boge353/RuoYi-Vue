-- ======================================================================
-- Flowable 数据库初始化脚本
-- 用于解决: NullPointerException: Cannot invoke PropertyEntity.getValue()
-- ======================================================================
-- 说明：此脚本用于创建数据库和基本配置，Flowable表将在应用启动时自动创建
-- ======================================================================

-- 1. 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `ry-vue` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 2. 使用数据库
USE `ry-vue`;

-- 3. 验证数据库已创建
SELECT DATABASE() AS current_database;

-- ======================================================================
-- 说明：Flowable 表不需要手动创建
-- ======================================================================
-- 配置文件中已设置 database-schema-update: true
-- FlowableConfig 中已设置 DB_SCHEMA_UPDATE_TRUE
-- 
-- 当应用启动时，Flowable 会自动创建以下约70个表：
-- - ACT_GE_* (通用数据表)
-- - ACT_RE_* (流程定义表) 
-- - ACT_RU_* (运行时数据表)
-- - ACT_HI_* (历史数据表)
-- - ACT_ID_* (身份管理表)
-- - FLW_* (Form/Event 相关表)
-- ======================================================================

-- 4. 确认数据库连接参数正确
-- 请确保 application-druid.yml 中的配置正确：
-- url: jdbc:mysql://localhost:3306/ry-vue
-- username: root
-- password: password (改为您的实际密码)

-- ======================================================================
-- 执行说明
-- ======================================================================
-- 方法1: 命令行执行
--   mysql -u root -p < sql/init_flowable_database.sql
--
-- 方法2: MySQL客户端执行
--   1. 打开 MySQL Workbench 或其他客户端
--   2. 执行此脚本
--
-- 方法3: 直接执行创建命令
--   mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS \`ry-vue\` DEFAULT CHARACTER SET utf8mb4;"
--
-- ======================================================================

-- 5. 验证准备完成
SELECT 'Database ry-vue is ready. Start your application now!' AS message;
SELECT 'Flowable tables will be auto-created on first startup.' AS info;
