-- ----------------------------
-- Table structure for luckysheet_workbook
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_workbook`;
CREATE TABLE `luckysheet_workbook` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作簿ID',
  `workbook_name` varchar(200) NOT NULL COMMENT '工作簿名称',
  `description` varchar(500) DEFAULT NULL COMMENT '工作簿描述',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_by_name` varchar(64) DEFAULT NULL COMMENT '创建者名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者ID',
  `update_by_name` varchar(64) DEFAULT NULL COMMENT '更新者名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_create_by` (`create_by`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Luckysheet工作簿表';

-- ----------------------------
-- Table structure for luckysheet_sheet
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_sheet`;
CREATE TABLE `luckysheet_sheet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作表ID',
  `workbook_id` bigint(20) NOT NULL COMMENT '工作簿ID',
  `sheet_index` varchar(50) NOT NULL COMMENT '工作表索引',
  `sheet_name` varchar(100) NOT NULL COMMENT '工作表名称',
  `sheet_order` int(11) DEFAULT '0' COMMENT '工作表顺序',
  `is_hidden` char(1) DEFAULT '0' COMMENT '是否隐藏（0否 1是）',
  `is_active` char(1) DEFAULT '0' COMMENT '是否激活（0否 1是）',
  `config` longtext COMMENT '工作表配置JSON',
  `cell_data` longtext COMMENT '单元格数据JSON',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_workbook_id` (`workbook_id`),
  KEY `idx_sheet_index` (`sheet_index`),
  KEY `idx_workbook_sheet` (`workbook_id`,`sheet_index`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Luckysheet工作表表';

-- ----------------------------
-- Records of luckysheet_workbook (示例数据)
-- ----------------------------
INSERT INTO `luckysheet_workbook` VALUES (1, '示例工作簿', '这是一个示例工作簿，展示Luckysheet的基本功能', 1, 'admin', NOW(), NULL, NULL, NULL, '0', '0', NULL);

-- ----------------------------
-- Records of luckysheet_sheet (示例数据)
-- ----------------------------
INSERT INTO `luckysheet_sheet` VALUES (1, 1, 'sheet1', 'Sheet1', 0, '0', '1', 
'{"name":"Sheet1","color":"","status":"1","order":"0","row":84,"column":60,"index":"sheet1","chart":[],"pivotTable":null,"filter_select":null,"filter":null,"zoomRatio":1,"showGridLines":"1","defaultColWidth":73,"defaultRowHeight":19,"celldata":[]}', 
'[{"r":0,"c":0,"v":{"v":"示例标题","ct":{"fa":"General","t":"g"},"m":"示例标题","bg":"#FFC000","bl":1,"it":0,"ff":"Arial","fs":"14"}},{"r":1,"c":0,"v":{"v":"A1","m":"A1"}},{"r":1,"c":1,"v":{"v":"B1","m":"B1"}},{"r":2,"c":0,"v":{"v":"A2","m":"A2"}},{"r":2,"c":1,"v":{"v":"=A1+B1","f":"=A1+B1","ct":{"fa":"General","t":"g"}}}]', 
NOW(), NULL, '0', '示例工作表，包含基本的单元格和公式');
