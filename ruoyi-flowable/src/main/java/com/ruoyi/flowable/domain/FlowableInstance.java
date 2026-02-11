package com.ruoyi.flowable.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程实例对象 flowable_instance
 * 
 * @author ruoyi
 */
public class FlowableInstance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流程实例ID */
    private String instanceId;

    /** 流程定义ID */
    private String processDefinitionId;

    /** 流程定义名称 */
    private String processDefinitionName;

    /** 流程定义Key */
    private String processDefinitionKey;

    /** 业务Key */
    private String businessKey;

    /** 流程发起人 */
    private String startUserId;

    /** 流程启动时间 */
    private String startTime;

    /** 流程结束时间 */
    private String endTime;

    /** 流程状态 */
    private String instanceStatus;

    /** 持续时间 */
    private Long duration;

    public String getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    public String getProcessDefinitionId()
    {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId)
    {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName()
    {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName)
    {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionKey()
    {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey)
    {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey()
    {
        return businessKey;
    }

    public void setBusinessKey(String businessKey)
    {
        this.businessKey = businessKey;
    }

    public String getStartUserId()
    {
        return startUserId;
    }

    public void setStartUserId(String startUserId)
    {
        this.startUserId = startUserId;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getInstanceStatus()
    {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus)
    {
        this.instanceStatus = instanceStatus;
    }

    public Long getDuration()
    {
        return duration;
    }

    public void setDuration(Long duration)
    {
        this.duration = duration;
    }
}
