package com.ruoyi.flowable.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.flowable.domain.FlowableTask;

/**
 * 流程任务Service接口
 * 
 * @author ruoyi
 */
public interface IFlowableTaskService
{
    /**
     * 查询待办任务列表
     * 
     * @param userId 用户ID
     * @return 待办任务集合
     */
    public List<FlowableTask> selectTodoTaskList(String userId);

    /**
     * 查询已办任务列表
     * 
     * @param userId 用户ID
     * @return 已办任务集合
     */
    public List<FlowableTask> selectFinishedTaskList(String userId);

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 流程变量
     * @param comment 审批意见
     */
    public void completeTask(String taskId, Map<String, Object> variables, String comment);

    /**
     * 拒绝任务（流程结束）
     * 
     * @param taskId 任务ID
     * @param comment 拒绝原因
     */
    public void rejectTask(String taskId, String comment);

    /**
     * 回退任务到指定节点
     * 
     * @param taskId 当前任务ID
     * @param targetTaskKey 目标任务节点Key
     * @param comment 回退原因
     */
    public void rollbackTask(String taskId, String targetTaskKey, String comment);

    /**
     * 转办任务
     * 
     * @param taskId 任务ID
     * @param userId 转办目标用户ID
     * @param comment 转办说明
     */
    public void transferTask(String taskId, String userId, String comment);

    /**
     * 委派任务
     * 
     * @param taskId 任务ID
     * @param userId 委派目标用户ID
     * @param comment 委派说明
     */
    public void delegateTask(String taskId, String userId, String comment);

    /**
     * 认领任务
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    public void claimTask(String taskId, String userId);

    /**
     * 取消认领任务
     * 
     * @param taskId 任务ID
     */
    public void unclaimTask(String taskId);
}
