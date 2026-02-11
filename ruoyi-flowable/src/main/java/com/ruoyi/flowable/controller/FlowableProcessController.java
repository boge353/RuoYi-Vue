package com.ruoyi.flowable.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.flowable.domain.FlowableProcess;
import com.ruoyi.flowable.service.IFlowableProcessService;

/**
 * 流程定义Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/flowable/process")
@ConditionalOnProperty(prefix = "flowable", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FlowableProcessController extends BaseController
{
    @Autowired
    private IFlowableProcessService flowableProcessService;

    /**
     * 查询流程定义列表
     */
    @GetMapping("/list")
    public TableDataInfo list()
    {
        startPage();
        List<FlowableProcess> list = flowableProcessService.selectProcessList();
        return getDataTable(list);
    }

    /**
     * 获取流程定义详细信息
     */
    @GetMapping(value = "/{processId}")
    public AjaxResult getInfo(@PathVariable("processId") String processId)
    {
        return success(flowableProcessService.selectProcessById(processId));
    }

    /**
     * 删除流程定义
     */
    @DeleteMapping("/{deploymentId}")
    public AjaxResult remove(@PathVariable String deploymentId)
    {
        flowableProcessService.deleteProcess(deploymentId);
        return success();
    }

    /**
     * 激活或挂起流程定义
     */
    @PutMapping("/changeState/{processId}/{suspendState}")
    public AjaxResult changeState(@PathVariable String processId, @PathVariable String suspendState)
    {
        flowableProcessService.updateProcessState(processId, suspendState);
        return success();
    }
}
