package com.ruoyi.flowable.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.flowable.domain.FlowableTask;
import com.ruoyi.flowable.service.IFlowableTaskService;

/**
 * 流程任务Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class FlowableTaskServiceImpl implements IFlowableTaskService
{
    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 查询待办任务列表
     * 
     * @param userId 用户ID
     * @return 待办任务集合
     */
    @Override
    public List<FlowableTask> selectTodoTaskList(String userId)
    {
        List<FlowableTask> taskList = new ArrayList<>();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime().desc()
                .list();
        
        for (Task task : tasks)
        {
            FlowableTask flowableTask = new FlowableTask();
            flowableTask.setTaskId(task.getId());
            flowableTask.setTaskName(task.getName());
            flowableTask.setProcessInstanceId(task.getProcessInstanceId());
            flowableTask.setProcessDefinitionId(task.getProcessDefinitionId());
            flowableTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
            flowableTask.setAssignee(task.getAssignee());
            if (task.getCreateTime() != null)
            {
                flowableTask.setTaskCreateTime(sdf.format(task.getCreateTime()));
            }
            flowableTask.setTaskStatus("待办");
            taskList.add(flowableTask);
        }
        
        return taskList;
    }

    /**
     * 查询已办任务列表
     * 
     * @param userId 用户ID
     * @return 已办任务集合
     */
    @Override
    public List<FlowableTask> selectFinishedTaskList(String userId)
    {
        List<FlowableTask> taskList = new ArrayList<>();
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();
        
        for (HistoricTaskInstance task : tasks)
        {
            FlowableTask flowableTask = new FlowableTask();
            flowableTask.setTaskId(task.getId());
            flowableTask.setTaskName(task.getName());
            flowableTask.setProcessInstanceId(task.getProcessInstanceId());
            flowableTask.setProcessDefinitionId(task.getProcessDefinitionId());
            flowableTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
            flowableTask.setAssignee(task.getAssignee());
            if (task.getCreateTime() != null)
            {
                flowableTask.setTaskCreateTime(sdf.format(task.getCreateTime()));
            }
            if (task.getEndTime() != null)
            {
                flowableTask.setTaskEndTime(sdf.format(task.getEndTime()));
            }
            flowableTask.setDuration(task.getDurationInMillis());
            flowableTask.setTaskStatus("已完成");
            taskList.add(flowableTask);
        }
        
        return taskList;
    }

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 流程变量
     * @param comment 审批意见
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(String taskId, Map<String, Object> variables, String comment)
    {
        // 添加审批意见
        if (comment != null && !comment.isEmpty())
        {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        }
        
        // 完成任务
        if (variables != null && !variables.isEmpty())
        {
            taskService.complete(taskId, variables);
        }
        else
        {
            taskService.complete(taskId);
        }
    }

    /**
     * 拒绝任务（流程结束）
     * 
     * @param taskId 任务ID
     * @param comment 拒绝原因
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(String taskId, String comment)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        
        // 添加拒绝意见
        if (comment != null && !comment.isEmpty())
        {
            taskService.addComment(taskId, task.getProcessInstanceId(), "拒绝：" + comment);
        }
        
        // 终止流程实例
        runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "审批拒绝");
    }

    /**
     * 回退任务到指定节点
     * 
     * @param taskId 当前任务ID
     * @param targetTaskKey 目标任务节点Key
     * @param comment 回退原因
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollbackTask(String taskId, String targetTaskKey, String comment)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        
        // 添加回退意见
        if (comment != null && !comment.isEmpty())
        {
            taskService.addComment(taskId, task.getProcessInstanceId(), "回退：" + comment);
        }
        
        // 获取当前执行实例
        Execution execution = runtimeService.createExecutionQuery()
                .executionId(task.getExecutionId())
                .singleResult();
        
        // 使用Flowable的moveActivityIdTo方法实现回退
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdTo(task.getTaskDefinitionKey(), targetTaskKey)
                .changeState();
    }

    /**
     * 转办任务
     * 
     * @param taskId 任务ID
     * @param userId 转办目标用户ID
     * @param comment 转办说明
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferTask(String taskId, String userId, String comment)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        
        // 添加转办说明
        if (comment != null && !comment.isEmpty())
        {
            taskService.addComment(taskId, task.getProcessInstanceId(), "转办：" + comment);
        }
        
        // 设置新的任务执行人
        taskService.setAssignee(taskId, userId);
    }

    /**
     * 委派任务
     * 
     * @param taskId 任务ID
     * @param userId 委派目标用户ID
     * @param comment 委派说明
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(String taskId, String userId, String comment)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        
        // 添加委派说明
        if (comment != null && !comment.isEmpty())
        {
            taskService.addComment(taskId, task.getProcessInstanceId(), "委派：" + comment);
        }
        
        // 委派任务
        taskService.delegateTask(taskId, userId);
    }

    /**
     * 认领任务
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    @Override
    public void claimTask(String taskId, String userId)
    {
        taskService.claim(taskId, userId);
    }

    /**
     * 取消认领任务
     * 
     * @param taskId 任务ID
     */
    @Override
    public void unclaimTask(String taskId)
    {
        taskService.unclaim(taskId);
    }
}
