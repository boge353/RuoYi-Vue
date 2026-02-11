package com.ruoyi.flowable.service;

import java.util.List;
import com.ruoyi.flowable.domain.FlowableProcess;

/**
 * 流程定义Service接口
 * 
 * @author ruoyi
 */
public interface IFlowableProcessService
{
    /**
     * 查询流程定义列表
     * 
     * @return 流程定义集合
     */
    public List<FlowableProcess> selectProcessList();

    /**
     * 根据流程ID查询流程定义
     * 
     * @param processId 流程ID
     * @return 流程定义
     */
    public FlowableProcess selectProcessById(String processId);

    /**
     * 部署流程定义
     * 
     * @param resourceName 资源名称
     * @param inputStream 输入流
     * @return 结果
     */
    public String deployProcess(String resourceName, java.io.InputStream inputStream);

    /**
     * 删除流程定义
     * 
     * @param deploymentId 部署ID
     * @return 结果
     */
    public void deleteProcess(String deploymentId);

    /**
     * 激活或挂起流程定义
     * 
     * @param processId 流程ID
     * @param suspendState 状态
     * @return 结果
     */
    public void updateProcessState(String processId, String suspendState);
}
