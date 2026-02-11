package com.ruoyi.flowable.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程任务对象 flowable_task
 * 
 * @author ruoyi
 */
public class FlowableTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private String taskId;

    /** 任务名称 */
    private String taskName;

    /** 流程实例ID */
    private String processInstanceId;

    /** 流程定义ID */
    private String processDefinitionId;

    /** 任务节点Key */
    private String taskDefinitionKey;

    /** 任务执行人 */
    private String assignee;

    /** 任务创建时间 */
    private String taskCreateTime;

    /** 任务完成时间 */
    private String taskEndTime;

    /** 任务持续时间 */
    private Long duration;

    /** 任务状态 */
    private String taskStatus;

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getProcessInstanceId()
    {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId)
    {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId()
    {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId)
    {
        this.processDefinitionId = processDefinitionId;
    }

    public String getTaskDefinitionKey()
    {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey)
    {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getAssignee()
    {
        return assignee;
    }

    public void setAssignee(String assignee)
    {
        this.assignee = assignee;
    }

    public String getTaskCreateTime()
    {
        return taskCreateTime;
    }

    public void setTaskCreateTime(String taskCreateTime)
    {
        this.taskCreateTime = taskCreateTime;
    }

    public String getTaskEndTime()
    {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime)
    {
        this.taskEndTime = taskEndTime;
    }

    public Long getDuration()
    {
        return duration;
    }

    public void setDuration(Long duration)
    {
        this.duration = duration;
    }

    public String getTaskStatus()
    {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus)
    {
        this.taskStatus = taskStatus;
    }
}
