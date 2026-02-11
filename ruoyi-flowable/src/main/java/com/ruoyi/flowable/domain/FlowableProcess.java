package com.ruoyi.flowable.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程定义对象 flowable_process
 * 
 * @author ruoyi
 */
public class FlowableProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流程ID */
    private String processId;

    /** 流程名称 */
    private String processName;

    /** 流程分类 */
    private String category;

    /** 流程版本 */
    private Integer version;

    /** 部署ID */
    private String deploymentId;

    /** 流程定义状态 */
    private String suspendState;

    public String getProcessId()
    {
        return processId;
    }

    public void setProcessId(String processId)
    {
        this.processId = processId;
    }

    public String getProcessName()
    {
        return processName;
    }

    public void setProcessName(String processName)
    {
        this.processName = processName;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public String getDeploymentId()
    {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId)
    {
        this.deploymentId = deploymentId;
    }

    public String getSuspendState()
    {
        return suspendState;
    }

    public void setSuspendState(String suspendState)
    {
        this.suspendState = suspendState;
    }
}
