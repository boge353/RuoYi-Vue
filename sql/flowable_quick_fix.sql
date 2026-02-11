-- =============================================
-- Flowable Quick Fix Script
-- 解决 "unknown version from database: '8.1.0'" 错误
-- =============================================

-- 此脚本会自动检查并修复Flowable数据库版本问题

-- 使用方法:
-- mysql -u root -p your_database_name < sql/flowable_quick_fix.sql

-- 或者在MySQL客户端中：
-- source /path/to/sql/flowable_quick_fix.sql

-- =============================================
-- Step 1: 诊断当前状态
-- =============================================

SELECT '=== 检查Flowable表是否存在 ===' AS 'Step 1';
SELECT COUNT(*) AS 'Flowable表数量' 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%');

SELECT '=== 检查当前版本号 ===' AS 'Step 2';

-- 尝试查询版本（如果表不存在会报错，这是正常的）
SELECT 'ACT_GE_PROPERTY 表:' AS '表名', NAME_, VALUE_ 
FROM ACT_GE_PROPERTY 
WHERE NAME_ = 'schema.version'
UNION ALL
SELECT 'ACT_ID_PROPERTY 表:', NAME_, VALUE_ 
FROM ACT_ID_PROPERTY 
WHERE NAME_ = 'schema.version';

-- =============================================
-- Step 2: 自动修复版本号
-- =============================================

SELECT '=== 开始修复版本号 ===' AS 'Step 3';

-- 如果表存在但版本不对，更新版本号
UPDATE ACT_GE_PROPERTY 
SET VALUE_ = '6.8.1.0' 
WHERE NAME_ = 'schema.version' 
AND VALUE_ != '6.8.1.0';

UPDATE ACT_ID_PROPERTY 
SET VALUE_ = '6.8.1.0' 
WHERE NAME_ = 'schema.version' 
AND VALUE_ != '6.8.1.0';

-- 显示更新结果
SELECT ROW_COUNT() AS '更新的记录数';

-- =============================================
-- Step 3: 验证修复结果
-- =============================================

SELECT '=== 验证修复后的版本号 ===' AS 'Step 4';

SELECT 'ACT_GE_PROPERTY 表:' AS '表名', NAME_, VALUE_ 
FROM ACT_GE_PROPERTY 
WHERE NAME_ = 'schema.version'
UNION ALL
SELECT 'ACT_ID_PROPERTY 表:', NAME_, VALUE_ 
FROM ACT_ID_PROPERTY 
WHERE NAME_ = 'schema.version';

-- =============================================
-- 完成
-- =============================================

SELECT '=== 修复完成！===' AS '状态',
       '现在可以重启应用了' AS '下一步',
       'mvn spring-boot:run' AS '启动命令';

-- =============================================
-- 注意事项:
-- 1. 如果上面显示"表不存在"的错误，这是正常的
--    这意味着这是首次安装，应用启动时会自动创建表
-- 2. 如果版本号已经是 6.8.1.0，说明不需要修复
-- 3. 执行此脚本后，重启应用即可
-- =============================================
