# RuoYi Flowable 工作流模块

## 模块介绍

ruoyi-flowable 是基于 Flowable 6.8.1 工作流引擎的集成模块，为 RuoYi 系统提供了强大的流程管理功能。

## 版本要求

- **JDK 版本**: 21
- **Spring Boot 版本**: 2.7.18
- **Flowable 版本**: 6.8.1

## 主要功能

1. **流程定义管理**
   - 流程定义查询
   - 流程部署
   - 流程删除
   - 流程激活/挂起

2. **流程引擎配置**
   - 自动字体配置（解决中文乱码问题）
   - 数据库自动建表
   - 流程引擎初始化

## 模块结构

```
ruoyi-flowable
├── src/main/java/com/ruoyi/flowable
│   ├── config          # Flowable配置类
│   ├── controller      # 流程控制器
│   ├── domain          # 流程实体类
│   ├── service         # 流程服务接口
│   │   └── impl        # 流程服务实现
│   └── mapper          # 流程数据访问层
└── pom.xml             # Maven配置文件
```

## API接口

### 流程定义管理

- `GET /flowable/process/list` - 查询流程定义列表
- `GET /flowable/process/{processId}` - 获取流程定义详情
- `DELETE /flowable/process/{deploymentId}` - 删除流程定义
- `PUT /flowable/process/changeState/{processId}/{suspendState}` - 激活/挂起流程

## 配置说明

在 `application.yml` 中添加了以下 Flowable 配置：

```yaml
flowable:
  # 关闭定时任务JOB
  async-executor-activate: false
  # 关闭自动部署
  check-process-definitions: false
  # 数据库策略-自动检查、创建表
  database-schema-update: true
  # 将databaseSchemaUpdate设置为true
  db-history-used: true
```

## 集成说明

1. 本模块已集成到 `ruoyi-admin` 主模块中
2. 启动应用时，Flowable 引擎会自动初始化
3. 相关数据表会自动创建在配置的数据库中

## 数据表说明

Flowable 会自动创建以下数据表：

- `ACT_RE_*` - 流程定义和部署相关表
- `ACT_RU_*` - 运行时数据表
- `ACT_HI_*` - 历史数据表
- `ACT_GE_*` - 通用数据表
- `ACT_ID_*` - 身份信息表

## 使用示例

### 查询所有流程定义

```java
@Autowired
private IFlowableProcessService flowableProcessService;

public void listProcesses() {
    List<FlowableProcess> processes = flowableProcessService.selectProcessList();
    // 处理流程列表...
}
```

### 部署流程定义

```java
String deploymentId = flowableProcessService.deployProcess(
    "process.bpmn20.xml", 
    inputStream
);
```

## 注意事项

1. 确保数据库已正确配置
2. 首次启动时，Flowable 会创建相关数据表
3. 流程定义文件需要符合 BPMN 2.0 规范
4. 建议在生产环境中关闭 `database-schema-update` 自动建表功能

## 后续扩展

可以基于此模块继续开发：

1. 流程实例管理
2. 任务管理
3. 流程变量管理
4. 流程图展示
5. 流程监控
6. 流程表单集成

## 参考文档

- [Flowable 官方文档](https://www.flowable.com/open-source/docs/)
- [RuoYi 官方文档](http://doc.ruoyi.vip/)
