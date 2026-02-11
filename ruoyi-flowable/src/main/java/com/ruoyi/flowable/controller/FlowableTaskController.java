package com.ruoyi.flowable.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.flowable.domain.FlowableTask;
import com.ruoyi.flowable.service.IFlowableTaskService;

/**
 * 流程任务Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/flowable/task")
public class FlowableTaskController extends BaseController
{
    @Autowired
    private IFlowableTaskService flowableTaskService;

    /**
     * 查询待办任务列表
     */
    @GetMapping("/todo")
    public TableDataInfo todoList()
    {
        String userId = getUserId();
        startPage();
        List<FlowableTask> list = flowableTaskService.selectTodoTaskList(userId);
        return getDataTable(list);
    }

    /**
     * 查询已办任务列表
     */
    @GetMapping("/finished")
    public TableDataInfo finishedList()
    {
        String userId = getUserId();
        startPage();
        List<FlowableTask> list = flowableTaskService.selectFinishedTaskList(userId);
        return getDataTable(list);
    }

    /**
     * 完成任务
     */
    @PostMapping("/complete/{taskId}")
    public AjaxResult complete(@PathVariable String taskId, 
                               @RequestBody(required = false) Map<String, Object> params)
    {
        String comment = params != null && params.containsKey("comment") ? 
                        params.get("comment").toString() : "";
        Map<String, Object> variables = params != null && params.containsKey("variables") ? 
                                       (Map<String, Object>) params.get("variables") : new HashMap<>();
        
        flowableTaskService.completeTask(taskId, variables, comment);
        return success();
    }

    /**
     * 拒绝任务
     */
    @PostMapping("/reject/{taskId}")
    public AjaxResult reject(@PathVariable String taskId, @RequestParam String comment)
    {
        flowableTaskService.rejectTask(taskId, comment);
        return success();
    }

    /**
     * 回退任务
     */
    @PostMapping("/rollback/{taskId}")
    public AjaxResult rollback(@PathVariable String taskId, 
                              @RequestParam String targetTaskKey,
                              @RequestParam String comment)
    {
        flowableTaskService.rollbackTask(taskId, targetTaskKey, comment);
        return success();
    }

    /**
     * 转办任务
     */
    @PostMapping("/transfer/{taskId}")
    public AjaxResult transfer(@PathVariable String taskId,
                              @RequestParam String userId,
                              @RequestParam String comment)
    {
        flowableTaskService.transferTask(taskId, userId, comment);
        return success();
    }

    /**
     * 委派任务
     */
    @PostMapping("/delegate/{taskId}")
    public AjaxResult delegate(@PathVariable String taskId,
                              @RequestParam String userId,
                              @RequestParam String comment)
    {
        flowableTaskService.delegateTask(taskId, userId, comment);
        return success();
    }

    /**
     * 认领任务
     */
    @PostMapping("/claim/{taskId}")
    public AjaxResult claim(@PathVariable String taskId)
    {
        String userId = getUserId();
        flowableTaskService.claimTask(taskId, userId);
        return success();
    }

    /**
     * 取消认领任务
     */
    @PostMapping("/unclaim/{taskId}")
    public AjaxResult unclaim(@PathVariable String taskId)
    {
        flowableTaskService.unclaimTask(taskId);
        return success();
    }

    /**
     * 获取当前用户ID
     */
    private String getUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null)
        {
            return authentication.getName();
        }
        return "admin"; // 默认返回admin
    }
}
