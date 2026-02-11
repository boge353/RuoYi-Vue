# Luckysheet 在线表格集成文档

## 概述

本文档介绍了 RuoYi-Vue 系统中集成的 Luckysheet 在线表格解决方案，提供完整的 Excel 操作功能，包括公式计算、数据验证等高级特性。

## Luckysheet 简介

Luckysheet 是一款纯前端类似 Excel 的在线表格，功能强大、配置简单、完全开源。支持表格设置，包括冻结行列、合并单元格、公式、筛选器、排序功能、条件格式、数据验证、批注等功能。

## 功能特性

### 1. 基础功能

#### 1.1 单元格操作
- ✅ 单元格编辑
- ✅ 复制、剪切、粘贴
- ✅ 撤销、重做
- ✅ 查找替换
- ✅ 拖拽填充
- ✅ 批量编辑

#### 1.2 格式设置
- ✅ 字体样式（字体、字号、粗体、斜体、下划线、删除线）
- ✅ 文本颜色、背景颜色
- ✅ 边框样式
- ✅ 对齐方式（水平、垂直）
- ✅ 文本换行
- ✅ 文本旋转
- ✅ 数字格式（货币、百分比、日期等）

#### 1.3 行列操作
- ✅ 插入/删除行列
- ✅ 隐藏/显示行列
- ✅ 设置行高列宽
- ✅ 冻结行列

#### 1.4 工作表操作
- ✅ 新增工作表
- ✅ 删除工作表
- ✅ 复制工作表
- ✅ 重命名工作表
- ✅ 显示/隐藏工作表
- ✅ 工作表排序

### 2. 公式支持（Formula Support）

Luckysheet 内置强大的公式引擎，支持数百种 Excel 公式：

#### 2.1 数学函数
```
SUM(A1:A10)           // 求和
AVERAGE(A1:A10)       // 平均值
COUNT(A1:A10)         // 计数
MAX(A1:A10)           // 最大值
MIN(A1:A10)           // 最小值
ROUND(A1, 2)          // 四舍五入
ABS(A1)               // 绝对值
SQRT(A1)              // 平方根
POWER(A1, 2)          // 幂运算
```

#### 2.2 逻辑函数
```
IF(A1>0, "正数", "负数")
AND(A1>0, B1>0)
OR(A1>0, B1>0)
NOT(A1>0)
```

#### 2.3 文本函数
```
CONCATENATE(A1, B1)   // 连接文本
LEFT(A1, 5)           // 左侧取字符
RIGHT(A1, 5)          // 右侧取字符
MID(A1, 2, 3)         // 中间取字符
LEN(A1)               // 文本长度
UPPER(A1)             // 转大写
LOWER(A1)             // 转小写
```

#### 2.4 日期时间函数
```
NOW()                 // 当前日期时间
TODAY()               // 当前日期
DATE(2024, 1, 1)      // 构造日期
YEAR(A1)              // 年份
MONTH(A1)             // 月份
DAY(A1)               // 日
```

#### 2.5 查找引用函数
```
VLOOKUP(A1, B1:D10, 2, FALSE)
HLOOKUP(A1, B1:D10, 2, FALSE)
INDEX(A1:A10, 5)
MATCH(A1, B1:B10, 0)
```

### 3. 数据验证（Data Validation）

支持多种数据验证规则，确保数据格式正确性：

#### 3.1 数字验证
```javascript
{
  type: 'number',
  value1: 0,      // 最小值
  value2: 100,    // 最大值
  checked: true
}
```

#### 3.2 列表验证
```javascript
{
  type: 'dropdown',
  value1: '男,女',  // 下拉选项
  checked: true
}
```

#### 3.3 日期验证
```javascript
{
  type: 'date',
  value1: '2024-01-01',
  value2: '2024-12-31',
  checked: true
}
```

#### 3.4 文本长度验证
```javascript
{
  type: 'textLength',
  value1: 1,     // 最小长度
  value2: 100,   // 最大长度
  checked: true
}
```

#### 3.5 自定义公式验证
```javascript
{
  type: 'validity',
  value1: '=A1>0',  // 自定义公式
  checked: true
}
```

### 4. 高级功能

#### 4.1 条件格式
- 根据单元格值设置格式
- 色阶
- 数据条
- 图标集

#### 4.2 图表
- 柱状图
- 折线图
- 饼图
- 散点图
- 面积图
- 雷达图

#### 4.3 数据透视表
- 行/列拖拽
- 值聚合
- 筛选

#### 4.4 筛选和排序
- 自动筛选
- 高级筛选
- 升序/降序排序
- 自定义排序

#### 4.5 批注
- 添加批注
- 编辑批注
- 删除批注
- 显示/隐藏批注

#### 4.6 插入对象
- 插入图片
- 插入链接
- 插入截图

#### 4.7 单元格保护
- 工作表保护
- 单元格锁定
- 范围保护

## 技术架构

### 前端技术栈

```json
{
  "luckysheet": "^2.1.13"  // Luckysheet 核心库
}
```

### 后端技术栈

- **Spring Boot**: 2.7.18
- **MyBatis**: 3.5.x
- **MySQL**: 8.2.0
- **JDK**: 21

## 数据模型

### 工作簿表 (luckysheet_workbook)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint(20) | 工作簿ID |
| workbook_name | varchar(200) | 工作簿名称 |
| description | varchar(500) | 工作簿描述 |
| create_by | varchar(64) | 创建者 |
| create_by_name | varchar(64) | 创建者名称 |
| create_time | datetime | 创建时间 |
| update_by | varchar(64) | 更新者 |
| update_by_name | varchar(64) | 更新者名称 |
| update_time | datetime | 更新时间 |
| status | char(1) | 状态（0正常 1停用） |
| del_flag | char(1) | 删除标志 |
| remark | varchar(500) | 备注 |

### 工作表表 (luckysheet_sheet)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint(20) | 工作表ID |
| workbook_id | bigint(20) | 工作簿ID |
| sheet_index | varchar(50) | 工作表索引 |
| sheet_name | varchar(100) | 工作表名称 |
| sheet_order | int(11) | 工作表顺序 |
| is_hidden | char(1) | 是否隐藏 |
| is_active | char(1) | 是否激活 |
| config | longtext | 工作表配置JSON |
| cell_data | longtext | 单元格数据JSON |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| del_flag | char(1) | 删除标志 |
| remark | varchar(500) | 备注 |

## API 接口

### 工作簿接口

#### 1. 查询工作簿列表
```
GET /luckysheet/workbook/list
```

参数：
- `workbookName`: 工作簿名称（可选）
- `status`: 状态（可选）
- `pageNum`: 页码
- `pageSize`: 每页数量

#### 2. 获取工作簿详情
```
GET /luckysheet/workbook/{id}
```

#### 3. 新增工作簿
```
POST /luckysheet/workbook
```

请求体：
```json
{
  "workbookName": "测试工作簿",
  "description": "这是一个测试工作簿",
  "status": "0",
  "remark": "备注信息"
}
```

#### 4. 修改工作簿
```
PUT /luckysheet/workbook
```

#### 5. 删除工作簿
```
DELETE /luckysheet/workbook/{ids}
```

### 工作表接口

#### 1. 查询工作表列表
```
GET /luckysheet/sheet/list
```

#### 2. 根据工作簿ID查询工作表
```
GET /luckysheet/sheet/listByWorkbook/{workbookId}
```

#### 3. 加载工作簿数据
```
GET /luckysheet/sheet/load/{workbookId}
```

返回格式：
```json
[
  {
    "id": 1,
    "workbookId": 1,
    "sheetIndex": "sheet1",
    "sheetName": "Sheet1",
    "sheetOrder": 0,
    "isHidden": "0",
    "isActive": "1",
    "config": "{}",
    "cellData": "[...]"
  }
]
```

#### 4. 新增工作表
```
POST /luckysheet/sheet
```

#### 5. 修改工作表
```
PUT /luckysheet/sheet
```

#### 6. 批量更新单元格数据
```
POST /luckysheet/sheet/batchUpdateCellData
```

请求体：
```json
{
  "workbookId": 1,
  "sheetIndex": "sheet1",
  "cellData": "[{\"r\":0,\"c\":0,\"v\":{\"v\":\"测试\"}}]"
}
```

#### 7. 删除工作表
```
DELETE /luckysheet/sheet/{ids}
```

## 前端集成

### 1. 安装依赖

```bash
cd ruoyi-ui
npm install
```

### 2. 使用 Luckysheet 组件

#### 基础用法

```vue
<template>
  <Luckysheet
    :workbook-id="workbookId"
    :data="sheetData"
    width="100%"
    height="600px"
    @save="handleSave"
  />
</template>

<script>
import Luckysheet from '@/components/Luckysheet'

export default {
  components: {
    Luckysheet
  },
  data() {
    return {
      workbookId: 1,
      sheetData: []
    }
  },
  methods: {
    handleSave(data) {
      console.log('保存数据:', data)
    }
  }
}
</script>
```

#### 高级配置

```vue
<Luckysheet
  :workbook-id="workbookId"
  :data="sheetData"
  :read-only="false"
  :options="customOptions"
  @cell-updated="onCellUpdated"
  @range-select="onRangeSelect"
  @sheet-activate="onSheetActivate"
  @sheet-create="onSheetCreate"
/>
```

### 3. 组件属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| workbookId | String/Number | - | 工作簿ID |
| data | Array | [] | 工作表数据 |
| width | String | '100%' | 容器宽度 |
| height | String | '600px' | 容器高度 |
| readOnly | Boolean | false | 是否只读 |
| options | Object | {} | 自定义配置 |

### 4. 组件事件

| 事件 | 参数 | 说明 |
|------|------|------|
| save | data | 保存数据 |
| cell-updated | event | 单元格更新 |
| range-select | event | 范围选择 |
| sheet-activate | event | 工作表激活 |
| sheet-create | event | 工作表创建 |

### 5. 组件方法

```javascript
// 获取组件实例
const luckysheet = this.$refs.luckysheet

// 获取所有工作表数据
const allSheets = luckysheet.getAllSheetData()

// 获取当前工作表数据
const currentSheet = luckysheet.getCurrentSheetData()

// 获取单元格值
const value = luckysheet.getCellValue(row, col, sheetIndex)

// 设置单元格值
luckysheet.setCellValue(row, col, value, sheetIndex)

// 刷新表格
luckysheet.refresh()
```

## 使用指南

### 1. 创建新工作簿

1. 访问工作簿管理页面
2. 点击"新增"按钮
3. 填写工作簿名称和描述
4. 点击"确定"创建工作簿

### 2. 编辑工作簿

1. 在工作簿列表中点击"打开"按钮
2. 进入在线表格编辑器
3. 使用工具栏进行各种操作
4. 数据自动保存（30秒一次）
5. 也可以手动点击"保存"按钮

### 3. 使用公式

1. 选中单元格
2. 输入 `=` 开始公式
3. 输入公式内容，例如 `=SUM(A1:A10)`
4. 按 Enter 完成输入
5. 公式自动计算结果

### 4. 设置数据验证

1. 选中要验证的单元格或范围
2. 点击工具栏"数据验证"按钮
3. 选择验证类型（数字、列表、日期等）
4. 设置验证条件
5. 点击"确定"应用验证规则

### 5. 创建图表

1. 选中数据范围
2. 点击工具栏"图表"按钮
3. 选择图表类型
4. 配置图表选项
5. 点击"确定"插入图表

## 性能优化

### 1. 数据存储优化

- 单元格数据采用 JSON 格式存储
- 使用 longtext 类型支持大量数据
- 仅保存有值的单元格，减少存储空间

### 2. 加载优化

- 按需加载工作表数据
- 懒加载大型工作簿
- 分页查询工作簿列表

### 3. 自动保存优化

- 防抖处理，避免频繁保存
- 仅保存变更的数据
- 后台异步保存

### 4. 渲染优化

- 虚拟滚动技术
- 按需渲染可见区域
- Canvas 渲染提升性能

## 常见问题

### Q1: 如何禁用某些功能？

A: 通过 `options.showtoolbarConfig` 配置：

```javascript
{
  showtoolbarConfig: {
    print: false,      // 禁用打印
    image: false,      // 禁用插入图片
    link: false        // 禁用插入链接
  }
}
```

### Q2: 如何自定义工具栏？

A: 使用 `cellRightClickConfig` 自定义右键菜单：

```javascript
{
  cellRightClickConfig: {
    copy: true,
    paste: true,
    insertRow: false,  // 禁用插入行
    insertColumn: false // 禁用插入列
  }
}
```

### Q3: 如何实现协同编辑？

A: Luckysheet 支持协同编辑，需要：
1. 实现 WebSocket 服务端
2. 配置协同编辑选项
3. 处理多用户操作冲突

### Q4: 数据验证不生效？

A: 确保：
1. 验证规则格式正确
2. checked 属性为 true
3. 单元格未被锁定

### Q5: 公式计算错误？

A: 检查：
1. 公式语法是否正确
2. 引用的单元格是否存在
3. 数据类型是否匹配

## 扩展开发

### 1. 自定义函数

```javascript
// 注册自定义函数
luckysheet.setCustomFunction({
  name: 'CUSTOM',
  fn: function(args) {
    // 自定义逻辑
    return result
  }
})
```

### 2. 自定义工具栏按钮

```javascript
{
  customToolbar: [
    {
      id: 'customBtn',
      title: '自定义按钮',
      icon: 'icon-class',
      click: function() {
        // 点击事件
      }
    }
  ]
}
```

### 3. 事件监听

```javascript
// 监听单元格更新
luckysheet.on('cellUpdated', function(r, c, oldValue, newValue) {
  console.log('单元格更新:', r, c, oldValue, newValue)
})
```

## 安全建议

1. **权限控制**：实施细粒度的工作簿访问控制
2. **数据验证**：对用户输入进行严格验证
3. **SQL 注入防护**：使用参数化查询
4. **XSS 防护**：对用户输入进行转义
5. **数据备份**：定期备份工作簿数据

## 参考资源

- [Luckysheet 官方文档](https://mengshukeji.gitee.io/LuckysheetDocs/zh/)
- [Luckysheet GitHub](https://github.com/mengshukeji/Luckysheet)
- [RuoYi 官方文档](http://doc.ruoyi.vip/)

---

**版本**: v1.0.0  
**更新日期**: 2026-02-11  
**维护团队**: RuoYi Luckysheet 集成团队
