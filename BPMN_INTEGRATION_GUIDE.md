# 若依工作流系统 - bpmn.js 集成文档

## 概述

本文档描述了 RuoYi-Vue 系统中集成的完整工作流解决方案，包括前端 bpmn.js 流程设计器和后端 Flowable 6.8.1 工作流引擎。

## 功能特性

### 1. 流程设计

#### 前端功能
- **可视化流程设计器**：基于 bpmn.js 的拖拽式流程设计
- **流程建模**：支持 BPMN 2.0 标准
- **属性面板**：可配置节点属性
- **流程导入/导出**：支持 XML 格式

#### 后端功能
- 流程定义管理（CRUD）
- 流程部署
- 流程版本控制
- 流程激活/挂起

### 2. 任务管理

#### 人工任务流程

##### 2.1 审批操作
- **通过审批**：完成任务并流转到下一节点
- **拒绝审批**：终止当前流程实例
- **回退操作**：回退到指定的历史节点
- **转办操作**：将任务转交给其他用户或部门

##### 2.2 人员节点支持
- **部门选择**：支持按部门筛选人员
- **角色选择**：支持按角色筛选人员
- **部门+角色组合**：先选部门，再选该部门下的角色

##### 2.3 会签与或签
- **会签（Countersignature）**：所有指定人员都需要审批通过
- **或签（OR Signature）**：任意一人审批通过即可

##### 2.4 任务委派
- **委派**：暂时委派给其他人处理，完成后返回原处理人
- **认领/取消认领**：候选人认领任务后成为执行人

#### 系统任务流程
- **系统任务调度**：自动执行的系统任务
- **消息提醒**：各节点完成后的消息通知

### 3. 流程实例管理
- 启动流程实例
- 查询流程实例
- 监控流程执行状态
- 查看流程历史

## 技术架构

### 前端技术栈

```json
{
  "bpmn-js": "^11.5.0",                    // BPMN 流程设计器
  "bpmn-js-properties-panel": "^1.22.1",  // 属性面板
  "camunda-bpmn-moddle": "^7.0.1",        // Camunda 扩展
  "vue": "2.6.12",                         // Vue框架
  "element-ui": "2.15.14"                  // UI组件库
}
```

### 后端技术栈

- **Spring Boot**: 2.7.18
- **Flowable**: 6.8.1
- **JDK**: 21
- **MySQL**: 8.0+

## 目录结构

### 前端结构

```
ruoyi-ui/
├── src/
│   ├── api/flowable/
│   │   ├── process.js              # 流程定义API
│   │   └── task.js                 # 任务管理API
│   ├── components/
│   │   └── ProcessDesigner/
│   │       └── index.vue           # BPMN流程设计器组件
│   └── views/flowable/
│       ├── process/
│       │   └── index.vue           # 流程定义管理页面
│       └── task/
│           └── index.vue           # 任务管理页面
```

### 后端结构

```
ruoyi-flowable/
└── src/main/java/com/ruoyi/flowable/
    ├── config/
    │   └── FlowableConfig.java            # Flowable配置
    ├── controller/
    │   ├── FlowableProcessController.java # 流程定义控制器
    │   └── FlowableTaskController.java    # 任务控制器
    ├── domain/
    │   ├── FlowableProcess.java           # 流程定义实体
    │   ├── FlowableTask.java              # 任务实体
    │   └── FlowableInstance.java          # 流程实例实体
    └── service/
        ├── IFlowableProcessService.java   # 流程定义服务接口
        ├── IFlowableTaskService.java      # 任务服务接口
        └── impl/
            ├── FlowableProcessServiceImpl.java
            └── FlowableTaskServiceImpl.java
```

## API 接口

### 流程定义接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 查询流程定义列表 | GET | /flowable/process/list | 分页查询 |
| 获取流程定义详情 | GET | /flowable/process/{processId} | 根据ID获取 |
| 部署流程定义 | POST | /flowable/process/deploy | 上传BPMN文件 |
| 删除流程定义 | DELETE | /flowable/process/{deploymentId} | 删除部署 |
| 激活/挂起流程 | PUT | /flowable/process/changeState/{processId}/{state} | 改变状态 |

### 任务管理接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 查询待办任务 | GET | /flowable/task/todo | 当前用户待办 |
| 查询已办任务 | GET | /flowable/task/finished | 当前用户已办 |
| 完成任务 | POST | /flowable/task/complete/{taskId} | 审批通过 |
| 拒绝任务 | POST | /flowable/task/reject/{taskId} | 审批拒绝 |
| 回退任务 | POST | /flowable/task/rollback/{taskId} | 回退到指定节点 |
| 转办任务 | POST | /flowable/task/transfer/{taskId} | 转给其他用户 |
| 委派任务 | POST | /flowable/task/delegate/{taskId} | 委派给其他用户 |
| 认领任务 | POST | /flowable/task/claim/{taskId} | 认领候选任务 |
| 取消认领 | POST | /flowable/task/unclaim/{taskId} | 取消认领 |

## 使用指南

### 1. 安装依赖

#### 前端依赖安装

```bash
cd ruoyi-ui
npm install
```

新增的依赖会自动安装：
- bpmn-js
- bpmn-js-properties-panel
- camunda-bpmn-moddle

### 2. 流程设计

#### 使用流程设计器

1. 访问流程定义管理页面
2. 点击"新增流程"按钮
3. 在设计器中：
   - 从左侧工具栏拖拽元素到画布
   - 选中元素后在右侧属性面板配置
   - 配置人员节点时可设置部门和角色
4. 点击"保存"按钮保存流程定义

#### BPMN 元素说明

- **StartEvent**：开始事件
- **UserTask**：用户任务（需要人工审批）
- **ServiceTask**：服务任务（系统自动执行）
- **ExclusiveGateway**：排他网关（条件分支）
- **ParallelGateway**：并行网关（会签）
- **EndEvent**：结束事件

### 3. 流程配置

#### 会签配置

在并行网关（ParallelGateway）中配置多个输出流：
```xml
<parallelGateway id="Gateway_1" />
<sequenceFlow sourceRef="Gateway_1" targetRef="Task_Approve1" />
<sequenceFlow sourceRef="Gateway_1" targetRef="Task_Approve2" />
<sequenceFlow sourceRef="Gateway_1" targetRef="Task_Approve3" />
```

#### 或签配置

使用包容网关（InclusiveGateway）或在用户任务中设置候选人组：
```xml
<userTask id="Task_1" name="审批">
  <candidateUsers>user1,user2,user3</candidateUsers>
</userTask>
```

#### 部门+角色配置

在用户任务的扩展属性中配置：
```xml
<userTask id="Task_1" name="部门经理审批">
  <extensionElements>
    <flowable:taskListener 
      event="create" 
      delegateExpression="${deptRoleTaskListener}">
      <flowable:field name="deptId">
        <flowable:string>dept_001</flowable:string>
      </flowable:field>
      <flowable:field name="roleId">
        <flowable:string>role_manager</flowable:string>
      </flowable:field>
    </flowable:taskListener>
  </extensionElements>
</userTask>
```

### 4. 任务审批

#### 审批流程

1. 登录系统后访问"待办任务"
2. 查看任务列表，点击"审批"
3. 填写审批意见
4. 选择操作：
   - **通过**：任务完成，流程继续
   - **拒绝**：填写拒绝原因，流程终止
   - **回退**：选择回退节点，流程回退
   - **转办**：选择转办人，任务转交

#### 回退操作示例

```javascript
// 回退到指定节点
rollbackTask(taskId, 'Task_Submit', '需要重新提交资料')
```

#### 转办操作示例

```javascript
// 转办给其他用户
transferTask(taskId, 'user123', '您更熟悉此业务，请您处理')
```

### 5. 系统任务配置

#### 服务任务配置

```xml
<serviceTask id="Task_Notify" name="发送通知" 
  flowable:delegateExpression="${notificationDelegate}">
</serviceTask>
```

#### 定时任务配置

```xml
<startEvent id="StartEvent_Timer">
  <timerEventDefinition>
    <timeCycle>0 0 9 * * ?</timeCycle> <!-- 每天9点执行 -->
  </timerEventDefinition>
</startEvent>
```

## 数据库表说明

Flowable 会自动创建以下表：

### 流程定义相关
- `ACT_RE_DEPLOYMENT` - 部署信息
- `ACT_RE_PROCDEF` - 流程定义
- `ACT_RE_MODEL` - 流程模型

### 运行时相关
- `ACT_RU_EXECUTION` - 流程执行实例
- `ACT_RU_TASK` - 运行中的任务
- `ACT_RU_VARIABLE` - 流程变量

### 历史相关
- `ACT_HI_PROCINST` - 流程实例历史
- `ACT_HI_TASKINST` - 任务实例历史
- `ACT_HI_VARINST` - 变量历史
- `ACT_HI_COMMENT` - 审批意见

### 身份相关
- `ACT_ID_USER` - 用户信息
- `ACT_ID_GROUP` - 用户组信息
- `ACT_ID_MEMBERSHIP` - 用户组成员关系

## 常见问题

### Q1: 如何配置候选人？

A: 在 UserTask 节点的属性中配置 `candidateUsers` 或 `candidateGroups`：
```xml
<userTask id="task1" name="审批">
  <candidateUsers>user1,user2</candidateUsers>
</userTask>
```

### Q2: 如何实现会签？

A: 使用并行网关（Parallel Gateway）创建多个分支，每个分支设置不同的审批人。

### Q3: 回退功能如何实现？

A: 使用 Flowable 的 `createChangeActivityStateBuilder()` API 实现流程节点的跳转。

### Q4: 如何集成消息通知？

A: 实现 Flowable 的 TaskListener，在任务创建或完成时发送通知。

## 扩展开发

### 自定义任务监听器

```java
@Component("customTaskListener")
public class CustomTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        // 任务创建时的逻辑
        String assignee = delegateTask.getAssignee();
        // 发送通知
    }
}
```

### 自定义执行监听器

```java
@Component("customExecutionListener")
public class CustomExecutionListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        // 流程执行时的逻辑
    }
}
```

### 部门角色选择器实现

```java
@Component("deptRoleTaskListener")
public class DeptRoleTaskListener implements TaskListener {
    @Autowired
    private ISysUserService userService;
    
    @Override
    public void notify(DelegateTask delegateTask) {
        String deptId = getField(delegateTask, "deptId");
        String roleId = getField(delegateTask, "roleId");
        
        // 根据部门和角色查询用户
        List<SysUser> users = userService.selectUsersByDeptAndRole(deptId, roleId);
        
        // 设置候选人
        for (SysUser user : users) {
            delegateTask.addCandidateUser(String.valueOf(user.getUserId()));
        }
    }
}
```

## 性能优化建议

1. **索引优化**：为常用查询字段添加数据库索引
2. **历史数据清理**：定期清理历史流程数据
3. **缓存策略**：缓存流程定义信息
4. **异步执行**：使用 Flowable 的异步执行器处理耗时任务

## 安全注意事项

1. **权限控制**：确保用户只能看到和操作自己的任务
2. **数据验证**：对流程变量和用户输入进行验证
3. **审计日志**：记录所有流程操作的审计日志
4. **敏感数据**：避免在流程变量中存储敏感信息

## 升级说明

### 从基础版升级

如果从基础的 Flowable 集成升级到完整工作流系统：

1. 更新 `package.json` 添加新依赖
2. 运行 `npm install` 安装依赖
3. 复制新的前端组件和页面
4. 复制新的后端服务和控制器
5. 重启前后端服务

## 技术支持

- Flowable 官方文档: https://www.flowable.com/open-source/docs/
- bpmn.js 官方文档: https://bpmn.io/toolkit/bpmn-js/
- RuoYi 官方文档: http://doc.ruoyi.vip/

---

**版本**: v1.0.0  
**更新日期**: 2026-02-11  
**维护团队**: RuoYi 工作流团队
