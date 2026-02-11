-- ======================================================================
-- Flowable 表验证脚本
-- 用于检查 Flowable 表是否已正确创建
-- ======================================================================

USE `ry-vue`;

-- 1. 检查数据库是否存在
SELECT DATABASE() AS current_database;

-- 2. 统计 Flowable 表数量（应该有约70个表）
SELECT COUNT(*) AS flowable_table_count
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'ry-vue' 
  AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%');

-- 3. 列出所有 Flowable 表
SELECT TABLE_NAME, TABLE_ROWS, CREATE_TIME
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'ry-vue' 
  AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%')
ORDER BY TABLE_NAME;

-- 4. 检查关键的版本表
SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
SELECT * FROM ACT_ID_PROPERTY WHERE NAME_ = 'schema.version';

-- 5. 检查通用属性表的其他配置
SELECT * FROM ACT_GE_PROPERTY ORDER BY NAME_;

-- 6. 验证结果说明
-- ======================================================================
-- 如果上述查询返回结果：
-- - flowable_table_count 约为 70: ✅ 表已创建
-- - schema.version = '6.8.1.0': ✅ 版本正确
-- 
-- 如果表不存在：
-- - flowable_table_count = 0: ❌ 表未创建
-- - 解决方案：执行 init_flowable_database.sql 后重启应用
-- ======================================================================
