import request from '@/utils/request'

// 查询工作表列表
export function listSheet(query) {
  return request({
    url: '/luckysheet/sheet/list',
    method: 'get',
    params: query
  })
}

// 根据工作簿ID查询工作表列表
export function listSheetByWorkbook(workbookId) {
  return request({
    url: '/luckysheet/sheet/listByWorkbook/' + workbookId,
    method: 'get'
  })
}

// 查询工作表详细
export function getSheet(id) {
  return request({
    url: '/luckysheet/sheet/' + id,
    method: 'get'
  })
}

// 根据工作簿ID和工作表索引查询
export function getSheetByIndex(workbookId, sheetIndex) {
  return request({
    url: '/luckysheet/sheet/getByIndex',
    method: 'get',
    params: {
      workbookId: workbookId,
      sheetIndex: sheetIndex
    }
  })
}

// 新增工作表
export function addSheet(data) {
  return request({
    url: '/luckysheet/sheet',
    method: 'post',
    data: data
  })
}

// 修改工作表
export function updateSheet(data) {
  return request({
    url: '/luckysheet/sheet',
    method: 'put',
    data: data
  })
}

// 批量更新单元格数据
export function batchUpdateCellData(data) {
  return request({
    url: '/luckysheet/sheet/batchUpdateCellData',
    method: 'post',
    data: data
  })
}

// 删除工作表
export function delSheet(id) {
  return request({
    url: '/luckysheet/sheet/' + id,
    method: 'delete'
  })
}

// 加载工作簿的所有工作表数据
export function loadWorkbook(workbookId) {
  return request({
    url: '/luckysheet/sheet/load/' + workbookId,
    method: 'get'
  })
}
