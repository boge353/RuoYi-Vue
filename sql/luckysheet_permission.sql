-- ----------------------------
-- Table structure for luckysheet_cell_permission
-- 單元格權限表
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_cell_permission`;
CREATE TABLE `luckysheet_cell_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '權限ID',
  `workbook_id` bigint(20) NOT NULL COMMENT '工作簿ID',
  `sheet_index` varchar(50) NOT NULL COMMENT '工作表索引',
  `row_index` int(11) NOT NULL COMMENT '行索引',
  `col_index` int(11) NOT NULL COMMENT '列索引',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部門ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用戶ID',
  `permission_type` char(1) NOT NULL DEFAULT '1' COMMENT '權限類型（0禁止 1只讀 2可編輯）',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建者',
  `update_time` datetime DEFAULT NULL COMMENT '更新時間',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  KEY `idx_workbook_sheet` (`workbook_id`,`sheet_index`),
  KEY `idx_cell_location` (`workbook_id`,`sheet_index`,`row_index`,`col_index`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Luckysheet單元格權限表';

-- ----------------------------
-- Table structure for luckysheet_cell_history
-- 單元格操作歷史表
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_cell_history`;
CREATE TABLE `luckysheet_cell_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '歷史ID',
  `workbook_id` bigint(20) NOT NULL COMMENT '工作簿ID',
  `sheet_index` varchar(50) NOT NULL COMMENT '工作表索引',
  `row_index` int(11) NOT NULL COMMENT '行索引',
  `col_index` int(11) NOT NULL COMMENT '列索引',
  `operation_type` varchar(20) NOT NULL COMMENT '操作類型（INSERT,UPDATE,DELETE,MERGE,SPLIT）',
  `old_value` longtext COMMENT '舊值JSON',
  `new_value` longtext COMMENT '新值JSON',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作者用戶ID',
  `operator_name` varchar(64) DEFAULT NULL COMMENT '操作者名稱',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '操作者部門ID',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '操作者部門名稱',
  `operation_time` datetime NOT NULL COMMENT '操作時間',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '客戶端IP',
  `is_archived` char(1) DEFAULT '0' COMMENT '是否已歸檔（0否 1是）',
  `archive_time` datetime DEFAULT NULL COMMENT '歸檔時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  KEY `idx_workbook_sheet` (`workbook_id`,`sheet_index`),
  KEY `idx_cell_location` (`workbook_id`,`sheet_index`,`row_index`,`col_index`),
  KEY `idx_operator` (`operator_id`),
  KEY `idx_dept` (`dept_id`),
  KEY `idx_operation_time` (`operation_time`),
  KEY `idx_is_archived` (`is_archived`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Luckysheet單元格操作歷史表';

-- ----------------------------
-- Records of luckysheet_cell_permission (示例數據)
-- 假設部門ID: 100=財務部, 101=人事部, 102=IT部
-- ----------------------------
INSERT INTO `luckysheet_cell_permission` VALUES 
(1, 1, 'sheet1', 0, 0, 100, NULL, '2', NOW(), 'admin', NULL, NULL, '財務部可編輯標題單元格'),
(2, 1, 'sheet1', 1, 0, 100, NULL, '2', NOW(), 'admin', NULL, NULL, '財務部可編輯A1'),
(3, 1, 'sheet1', 1, 1, 100, NULL, '2', NOW(), 'admin', NULL, NULL, '財務部可編輯B1'),
(4, 1, 'sheet1', 2, 0, 101, NULL, '1', NOW(), 'admin', NULL, NULL, '人事部只讀A2'),
(5, 1, 'sheet1', 2, 1, 101, NULL, '2', NOW(), 'admin', NULL, NULL, '人事部可編輯B2'),
(6, 1, 'sheet1', 2, 1, 102, NULL, '2', NOW(), 'admin', NULL, NULL, 'IT部也可編輯B2（多部門編輯同一單元格）');

-- ----------------------------
-- Records of luckysheet_cell_history (示例數據)
-- ----------------------------
INSERT INTO `luckysheet_cell_history` VALUES 
(1, 1, 'sheet1', 1, 1, 'INSERT', NULL, '{"v":"初始值","m":"初始值"}', 1, 'admin', 100, '財務部', NOW(), '127.0.0.1', '0', NULL, '初始插入'),
(2, 1, 'sheet1', 1, 1, 'UPDATE', '{"v":"初始值","m":"初始值"}', '{"v":"更新值","m":"更新值"}', 1, 'admin', 100, '財務部', NOW(), '127.0.0.1', '0', NULL, '更新操作');
