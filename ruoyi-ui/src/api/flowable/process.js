import request from '@/utils/request'

// 查询流程定义列表
export function listProcess(query) {
  return request({
    url: '/flowable/process/list',
    method: 'get',
    params: query
  })
}

// 查询流程定义详细
export function getProcess(processId) {
  return request({
    url: '/flowable/process/' + processId,
    method: 'get'
  })
}

// 部署流程定义
export function deployProcess(data) {
  return request({
    url: '/flowable/process/deploy',
    method: 'post',
    data: data
  })
}

// 删除流程定义
export function delProcess(deploymentId) {
  return request({
    url: '/flowable/process/' + deploymentId,
    method: 'delete'
  })
}

// 激活/挂起流程定义
export function changeProcessState(processId, suspendState) {
  return request({
    url: '/flowable/process/changeState/' + processId + '/' + suspendState,
    method: 'put'
  })
}
