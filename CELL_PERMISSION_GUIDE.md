# Luckysheet 單元格權限與操作歷史系統

## 概述

本文檔描述了 Luckysheet 在線表格系統的單元格級權限控制和操作歷史跟蹤功能，實現了企業級的數據安全和審計需求。

## 功能需求

### 1. 單元格權限控制
- **部門級權限**：不同部門可以訪問不同的單元格
- **用戶級權限**：特定用戶可以有特殊權限
- **權限類型**：禁止訪問、只讀、可編輯
- **多部門編輯**：同一單元格可被多個部門編輯

### 2. 操作歷史記錄
- **全量記錄**：每次單元格操作都記錄到數據庫
- **完整信息**：記錄操作者、部門、時間、IP、新舊值
- **歸檔功能**：可以歸檔歷史記錄
- **數據匯總**：統計各部門的編輯次數和時間

## 數據庫設計

### luckysheet_cell_permission 表

單元格權限表，存儲每個單元格的訪問權限配置。

```sql
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Luckysheet單元格權限表';
```

#### 權限類型說明

| 值 | 類型 | 說明 |
|----|------|------|
| 0 | 禁止 | 用戶完全無法訪問此單元格 |
| 1 | 只讀 | 用戶可以查看但不能編輯 |
| 2 | 可編輯 | 用戶可以查看和編輯 |

#### 權限優先級

- 用戶級權限 > 部門級權限
- 如果同時存在用戶權限和部門權限，取最高權限
- 如果沒有設置任何權限，默認為可編輯（權限類型2）

### luckysheet_cell_history 表

單元格操作歷史表，記錄所有單元格的變更歷史。

```sql
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Luckysheet單元格操作歷史表';
```

#### 操作類型說明

| 操作類型 | 說明 |
|---------|------|
| INSERT | 新增單元格內容 |
| UPDATE | 更新單元格內容 |
| DELETE | 刪除單元格內容 |
| MERGE | 合併單元格 |
| SPLIT | 拆分單元格 |

## API 接口

### 權限管理接口

#### 1. 查詢單元格權限列表

```
GET /luckysheet/permission/list
```

參數：
- `workbookId`: 工作簿ID（可選）
- `sheetIndex`: 工作表索引（可選）
- `rowIndex`: 行索引（可選）
- `colIndex`: 列索引（可選）
- `deptId`: 部門ID（可選）
- `userId`: 用戶ID（可選）

#### 2. 檢查單元格權限

```
GET /luckysheet/permission/check
```

參數：
- `workbookId`: 工作簿ID（必填）
- `sheetIndex`: 工作表索引（必填）
- `rowIndex`: 行索引（必填）
- `colIndex`: 列索引（必填）
- `userId`: 用戶ID（必填）
- `deptId`: 部門ID（必填）

返回：權限類型（0/1/2）

#### 3. 獲取工作表的所有單元格權限

```
GET /luckysheet/permission/sheet/{workbookId}/{sheetIndex}
```

參數：
- `userId`: 用戶ID（必填）
- `deptId`: 部門ID（必填）

返回：用戶可訪問的所有單元格權限列表

#### 4. 新增單元格權限

```
POST /luckysheet/permission
```

請求體：
```json
{
  "workbookId": 1,
  "sheetIndex": "sheet1",
  "rowIndex": 0,
  "colIndex": 0,
  "deptId": 100,
  "userId": null,
  "permissionType": "2",
  "remark": "財務部可編輯"
}
```

#### 5. 修改單元格權限

```
PUT /luckysheet/permission
```

#### 6. 刪除單元格權限

```
DELETE /luckysheet/permission/{ids}
```

### 歷史記錄接口

#### 1. 查詢單元格操作歷史列表

```
GET /luckysheet/history/list
```

參數：
- `workbookId`: 工作簿ID（可選）
- `sheetIndex`: 工作表索引（可選）
- `rowIndex`: 行索引（可選）
- `colIndex`: 列索引（可選）
- `operationType`: 操作類型（可選）
- `operatorId`: 操作者ID（可選）
- `deptId`: 部門ID（可選）
- `isArchived`: 是否已歸檔（可選）

#### 2. 查詢指定單元格的操作歷史

```
GET /luckysheet/history/cell
```

參數：
- `workbookId`: 工作簿ID（必填）
- `sheetIndex`: 工作表索引（必填）
- `rowIndex`: 行索引（必填）
- `colIndex`: 列索引（必填）

返回：該單元格的所有歷史記錄，按時間倒序

#### 3. 記錄單元格操作

```
POST /luckysheet/history
```

請求體：
```json
{
  "workbookId": 1,
  "sheetIndex": "sheet1",
  "rowIndex": 1,
  "colIndex": 1,
  "operationType": "UPDATE",
  "oldValue": "{\"v\":\"舊值\"}",
  "newValue": "{\"v\":\"新值\"}",
  "operatorId": 1,
  "operatorName": "admin",
  "deptId": 100,
  "deptName": "財務部",
  "operationTime": "2026-02-11 10:30:00",
  "clientIp": "192.168.1.100",
  "isArchived": "0"
}
```

#### 4. 歸檔指定單元格的歷史記錄

```
POST /luckysheet/history/archive
```

參數：
- `workbookId`: 工作簿ID（必填）
- `sheetIndex`: 工作表索引（必填）
- `rowIndex`: 行索引（必填）
- `colIndex`: 列索引（必填）

功能：將該單元格所有未歸檔的歷史記錄標記為已歸檔

#### 5. 獲取單元格的部門編輯統計

```
GET /luckysheet/history/statistics
```

參數：
- `workbookId`: 工作簿ID（必填）
- `sheetIndex`: 工作表索引（必填）
- `rowIndex`: 行索引（必填）
- `colIndex`: 列索引（必填）

返回：
```json
[
  {
    "deptId": 100,
    "deptName": "財務部",
    "editCount": 5,
    "firstEditTime": "2026-02-10 09:00:00",
    "lastEditTime": "2026-02-11 10:30:00"
  },
  {
    "deptId": 101,
    "deptName": "人事部",
    "editCount": 3,
    "firstEditTime": "2026-02-10 14:00:00",
    "lastEditTime": "2026-02-11 09:15:00"
  }
]
```

### 增強的工作表接口

#### 1. 更新單個單元格（帶權限檢查）

```
POST /luckysheet/sheet/updateCell
```

請求體：
```json
{
  "workbookId": 1,
  "sheetIndex": "sheet1",
  "rowIndex": 1,
  "colIndex": 1,
  "oldValue": "{\"v\":\"舊值\"}",
  "newValue": "{\"v\":\"新值\"}"
}
```

功能：
1. 自動檢查用戶權限
2. 如果無權限或只讀，返回錯誤
3. 自動記錄操作歷史
4. 包含操作者信息和客戶端IP

#### 2. 加載工作簿數據（帶權限過濾）

```
GET /luckysheet/sheet/load/{workbookId}
```

參數：
- `userId`: 用戶ID（可選，默認當前用戶）
- `deptId`: 部門ID（可選，默認當前用戶部門）

功能：根據用戶權限過濾單元格數據

## 使用場景

### 場景1：多部門協同編輯

**需求**：財務部和人事部共同編輯員工薪資表，其中：
- A列（員工編號）：所有部門只讀
- B列（基本工資）：財務部可編輯，人事部只讀
- C列（績效獎金）：人事部可編輯，財務部只讀
- D列（總工資）：兩個部門都可編輯

**配置**：
```sql
-- A列：所有部門只讀
INSERT INTO luckysheet_cell_permission (workbook_id, sheet_index, row_index, col_index, dept_id, permission_type) 
VALUES (1, 'sheet1', 0, 0, 100, '1'), (1, 'sheet1', 0, 0, 101, '1');

-- B列：財務部可編輯，人事部只讀
INSERT INTO luckysheet_cell_permission (workbook_id, sheet_index, row_index, col_index, dept_id, permission_type) 
VALUES (1, 'sheet1', 0, 1, 100, '2'), (1, 'sheet1', 0, 1, 101, '1');

-- C列：人事部可編輯，財務部只讀
INSERT INTO luckysheet_cell_permission (workbook_id, sheet_index, row_index, col_index, dept_id, permission_type) 
VALUES (1, 'sheet1', 0, 2, 100, '1'), (1, 'sheet1', 0, 2, 101, '2');

-- D列：兩個部門都可編輯
INSERT INTO luckysheet_cell_permission (workbook_id, sheet_index, row_index, col_index, dept_id, permission_type) 
VALUES (1, 'sheet1', 0, 3, 100, '2'), (1, 'sheet1', 0, 3, 101, '2');
```

### 場景2：操作歷史追蹤與審計

**需求**：追蹤單元格的所有變更歷史，包括誰在什麼時間做了什麼修改。

**查詢示例**：
```javascript
// 查看單元格 (1, 1) 的所有歷史
fetch('/luckysheet/history/cell?workbookId=1&sheetIndex=sheet1&rowIndex=1&colIndex=1')
  .then(res => res.json())
  .then(data => {
    console.log('歷史記錄：', data);
    // 顯示：
    // 2026-02-11 10:30 - admin（財務部）修改：100 -> 120
    // 2026-02-10 14:15 - user1（人事部）修改：80 -> 100
    // 2026-02-10 09:00 - admin（財務部）新增：80
  });
```

### 場景3：多部門數據匯總

**需求**：統計一個單元格被哪些部門編輯過，編輯了多少次。

**查詢示例**：
```javascript
// 獲取單元格 (1, 1) 的部門編輯統計
fetch('/luckysheet/history/statistics?workbookId=1&sheetIndex=sheet1&rowIndex=1&colIndex=1')
  .then(res => res.json())
  .then(data => {
    console.log('部門編輯統計：', data);
    // 結果：
    // 財務部：編輯5次，首次2026-02-10 09:00，最後2026-02-11 10:30
    // 人事部：編輯3次，首次2026-02-10 14:00，最後2026-02-11 09:15
  });
```

### 場景4：數據歸檔

**需求**：定期歸檔歷史數據，標記為已處理。

**操作示例**：
```javascript
// 歸檔單元格 (1, 1) 的所有歷史記錄
fetch('/luckysheet/history/archive', {
  method: 'POST',
  body: JSON.stringify({
    workbookId: 1,
    sheetIndex: 'sheet1',
    rowIndex: 1,
    colIndex: 1
  })
})
  .then(res => res.json())
  .then(data => {
    console.log('歸檔完成，共歸檔', data, '條記錄');
  });
```

## 前端集成

### 權限檢查示例

```javascript
// 在用戶點擊單元格時檢查權限
function onCellClick(row, col) {
  const userId = getCurrentUserId();
  const deptId = getCurrentDeptId();
  
  fetch(`/luckysheet/permission/check?workbookId=1&sheetIndex=sheet1&rowIndex=${row}&colIndex=${col}&userId=${userId}&deptId=${deptId}`)
    .then(res => res.json())
    .then(permission => {
      if (permission === '0') {
        alert('您無權訪問此單元格');
        return;
      } else if (permission === '1') {
        // 設置為只讀模式
        setCellReadOnly(row, col);
      } else {
        // 可編輯
        enableCellEdit(row, col);
      }
    });
}
```

### 操作記錄示例

```javascript
// 在用戶修改單元格時記錄歷史
function onCellUpdate(row, col, oldValue, newValue) {
  fetch('/luckysheet/sheet/updateCell', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      workbookId: 1,
      sheetIndex: 'sheet1',
      rowIndex: row,
      colIndex: col,
      oldValue: JSON.stringify(oldValue),
      newValue: JSON.stringify(newValue)
    })
  })
    .then(res => res.json())
    .then(data => {
      if (data.code === 200) {
        console.log('更新成功，已記錄歷史');
      } else {
        alert(data.msg);
        // 恢復舊值
        setCellValue(row, col, oldValue);
      }
    });
}
```

## 性能優化建議

### 1. 批量權限查詢

不要逐個單元格查詢權限，而是一次性獲取整個工作表的權限：

```javascript
// 加載工作表時一次性獲取所有權限
fetch(`/luckysheet/permission/sheet/1/sheet1?userId=${userId}&deptId=${deptId}`)
  .then(res => res.json())
  .then(permissions => {
    // 緩存到前端
    cachePermissions(permissions);
  });
```

### 2. 異步記錄歷史

操作歷史記錄可以異步處理，不阻塞用戶操作：

```javascript
// 使用隊列異步提交歷史記錄
historyQueue.push({
  workbookId: 1,
  sheetIndex: 'sheet1',
  rowIndex: row,
  colIndex: col,
  oldValue: oldValue,
  newValue: newValue
});

// 每秒批量提交一次
setInterval(() => {
  if (historyQueue.length > 0) {
    batchSubmitHistory(historyQueue);
    historyQueue = [];
  }
}, 1000);
```

### 3. 索引優化

確保數據庫有正確的索引：

```sql
-- 權限表索引
CREATE INDEX idx_cell_location ON luckysheet_cell_permission (workbook_id, sheet_index, row_index, col_index);
CREATE INDEX idx_dept_id ON luckysheet_cell_permission (dept_id);

-- 歷史表索引
CREATE INDEX idx_cell_location ON luckysheet_cell_history (workbook_id, sheet_index, row_index, col_index);
CREATE INDEX idx_operation_time ON luckysheet_cell_history (operation_time);
CREATE INDEX idx_is_archived ON luckysheet_cell_history (is_archived);
```

## 安全注意事項

1. **權限驗證**：每次修改操作都必須在後端驗證權限
2. **SQL 注入防護**：使用參數化查詢
3. **敏感數據**：舊值和新值可能包含敏感信息，注意訪問控制
4. **審計日志**：操作歷史本身不能被隨意刪除或修改

## 總結

本系統實現了完整的單元格級權限控制和操作歷史追蹤功能，滿足企業級應用的需求：

- ✅ 單元格級權限控制
- ✅ 部門和用戶兩級權限
- ✅ 多部門協同編輯
- ✅ 完整的操作歷史記錄
- ✅ 數據歸檔功能
- ✅ 部門編輯統計與匯總

---

**版本**: v1.0.0  
**更新日期**: 2026-02-11  
**維護團隊**: RuoYi Luckysheet 權限系統團隊
