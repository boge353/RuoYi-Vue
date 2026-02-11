import request from '@/utils/request'

// 查询待办任务列表
export function todoList(query) {
  return request({
    url: '/flowable/task/todo',
    method: 'get',
    params: query
  })
}

// 查询已办任务列表
export function finishedList(query) {
  return request({
    url: '/flowable/task/finished',
    method: 'get',
    params: query
  })
}

// 完成任务
export function completeTask(taskId, data) {
  return request({
    url: '/flowable/task/complete/' + taskId,
    method: 'post',
    data: data
  })
}

// 拒绝任务
export function rejectTask(taskId, comment) {
  return request({
    url: '/flowable/task/reject/' + taskId,
    method: 'post',
    params: { comment }
  })
}

// 回退任务
export function rollbackTask(taskId, targetTaskKey, comment) {
  return request({
    url: '/flowable/task/rollback/' + taskId,
    method: 'post',
    params: { targetTaskKey, comment }
  })
}

// 转办任务
export function transferTask(taskId, userId, comment) {
  return request({
    url: '/flowable/task/transfer/' + taskId,
    method: 'post',
    params: { userId, comment }
  })
}

// 委派任务
export function delegateTask(taskId, userId, comment) {
  return request({
    url: '/flowable/task/delegate/' + taskId,
    method: 'post',
    params: { userId, comment }
  })
}

// 认领任务
export function claimTask(taskId) {
  return request({
    url: '/flowable/task/claim/' + taskId,
    method: 'post'
  })
}

// 取消认领任务
export function unclaimTask(taskId) {
  return request({
    url: '/flowable/task/unclaim/' + taskId,
    method: 'post'
  })
}
