package com.ruoyi.flowable.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.flowable.domain.FlowableProcess;
import com.ruoyi.flowable.service.IFlowableProcessService;

/**
 * 流程定义Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class FlowableProcessServiceImpl implements IFlowableProcessService
{
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询流程定义列表
     * 
     * @return 流程定义集合
     */
    @Override
    public List<FlowableProcess> selectProcessList()
    {
        List<FlowableProcess> processList = new ArrayList<>();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        
        for (ProcessDefinition processDefinition : list)
        {
            FlowableProcess process = new FlowableProcess();
            process.setProcessId(processDefinition.getId());
            process.setProcessName(processDefinition.getName());
            process.setCategory(processDefinition.getCategory());
            process.setVersion(processDefinition.getVersion());
            process.setDeploymentId(processDefinition.getDeploymentId());
            process.setSuspendState(processDefinition.isSuspended() ? "1" : "0");
            processList.add(process);
        }
        
        return processList;
    }

    /**
     * 根据流程ID查询流程定义
     * 
     * @param processId 流程ID
     * @return 流程定义
     */
    @Override
    public FlowableProcess selectProcessById(String processId)
    {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processId)
                .singleResult();
        
        if (processDefinition != null)
        {
            FlowableProcess process = new FlowableProcess();
            process.setProcessId(processDefinition.getId());
            process.setProcessName(processDefinition.getName());
            process.setCategory(processDefinition.getCategory());
            process.setVersion(processDefinition.getVersion());
            process.setDeploymentId(processDefinition.getDeploymentId());
            process.setSuspendState(processDefinition.isSuspended() ? "1" : "0");
            return process;
        }
        
        return null;
    }

    /**
     * 部署流程定义
     * 
     * @param resourceName 资源名称
     * @param inputStream 输入流
     * @return 结果
     */
    @Override
    public String deployProcess(String resourceName, java.io.InputStream inputStream)
    {
        Deployment deployment = repositoryService.createDeployment()
                .addInputStream(resourceName, inputStream)
                .deploy();
        return deployment.getId();
    }

    /**
     * 删除流程定义
     * 
     * @param deploymentId 部署ID
     * @return 结果
     */
    @Override
    public void deleteProcess(String deploymentId)
    {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 激活或挂起流程定义
     * 
     * @param processId 流程ID
     * @param suspendState 状态
     * @return 结果
     */
    @Override
    public void updateProcessState(String processId, String suspendState)
    {
        if ("1".equals(suspendState))
        {
            repositoryService.suspendProcessDefinitionById(processId);
        }
        else
        {
            repositoryService.activateProcessDefinitionById(processId);
        }
    }
}
