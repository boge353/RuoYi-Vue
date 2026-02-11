import request from '@/utils/request'

// 查询工作簿列表
export function listWorkbook(query) {
  return request({
    url: '/luckysheet/workbook/list',
    method: 'get',
    params: query
  })
}

// 查询工作簿详细
export function getWorkbook(id) {
  return request({
    url: '/luckysheet/workbook/' + id,
    method: 'get'
  })
}

// 新增工作簿
export function addWorkbook(data) {
  return request({
    url: '/luckysheet/workbook',
    method: 'post',
    data: data
  })
}

// 修改工作簿
export function updateWorkbook(data) {
  return request({
    url: '/luckysheet/workbook',
    method: 'put',
    data: data
  })
}

// 删除工作簿
export function delWorkbook(id) {
  return request({
    url: '/luckysheet/workbook/' + id,
    method: 'delete'
  })
}

// 导出工作簿
export function exportWorkbook(query) {
  return request({
    url: '/luckysheet/workbook/export',
    method: 'post',
    params: query
  })
}
