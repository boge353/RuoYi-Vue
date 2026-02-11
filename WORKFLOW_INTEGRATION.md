# 工作流模块集成说明

## 概述

本次更新为 RuoYi-Vue 系统集成了基于 Flowable 6.8.1 的工作流引擎模块，并将项目升级到 JDK 21。

## 主要变更

### 1. JDK 版本升级

- **原版本**: JDK 1.8
- **新版本**: JDK 21
- **变更文件**: `/pom.xml`

### 2. Spring Boot 版本升级

- **原版本**: 2.5.15
- **新版本**: 2.7.18
- **原因**: Spring Boot 2.7.18 支持 JDK 21，并且兼容 Flowable 6.8.1

### 3. MySQL 驱动升级

由于 Spring Boot 2.7.8+ 不再管理旧的 MySQL 驱动依赖，进行了以下更改：

- **原坐标**: `mysql:mysql-connector-java` (未指定版本)
- **新坐标**: `com.mysql:mysql-connector-j:8.0.33`
- **变更文件**: 
  - `/pom.xml` (添加依赖管理)
  - `/ruoyi-admin/pom.xml` (更新依赖坐标)

### 4. 新增 ruoyi-flowable 工作流模块

创建了完整的工作流模块，包括：

#### 模块结构
```
ruoyi-flowable/
├── pom.xml                                         # Maven 配置
├── README.md                                       # 模块说明文档
└── src/main/java/com/ruoyi/flowable/
    ├── config/
    │   └── FlowableConfig.java                    # Flowable 配置类
    ├── controller/
    │   └── FlowableProcessController.java         # 流程定义控制器
    ├── domain/
    │   └── FlowableProcess.java                   # 流程定义实体
    ├── service/
    │   ├── IFlowableProcessService.java           # 服务接口
    │   └── impl/
    │       └── FlowableProcessServiceImpl.java    # 服务实现
    └── mapper/                                     # 数据访问层（预留）
```

#### 核心功能

1. **FlowableConfig.java** - 配置类
   - 设置中文字体，避免流程图中文乱码
   - 配置流程引擎

2. **FlowableProcessController.java** - REST API 控制器
   - `GET /flowable/process/list` - 查询流程定义列表
   - `GET /flowable/process/{processId}` - 获取流程详情
   - `DELETE /flowable/process/{deploymentId}` - 删除流程
   - `PUT /flowable/process/changeState/{processId}/{suspendState}` - 激活/挂起流程

3. **FlowableProcessServiceImpl.java** - 服务实现
   - 流程定义查询
   - 流程部署
   - 流程删除
   - 流程状态管理

### 5. 配置文件更新

#### `/ruoyi-admin/src/main/resources/application.yml`

添加了 Flowable 配置：

```yaml
flowable:
  async-executor-activate: false      # 关闭定时任务JOB
  check-process-definitions: false    # 关闭自动部署
  database-schema-update: true        # 自动创建/更新数据库表
  db-history-used: true              # 启用历史数据
```

### 6. 依赖管理更新

在 `/pom.xml` 中添加了以下依赖管理：

```xml
<flowable.version>6.8.1</flowable.version>
<mysql.version>8.0.33</mysql.version>

<!-- Flowable工作流引擎 -->
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-spring-boot-starter</artifactId>
    <version>${flowable.version}</version>
</dependency>

<!-- 工作流模块 -->
<dependency>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi-flowable</artifactId>
    <version>${ruoyi.version}</version>
</dependency>
```

## 构建验证

项目已通过以下构建测试：

1. ✅ `mvn clean compile -DskipTests` - 编译成功
2. ✅ `mvn clean package -DskipTests` - 打包成功

使用的 Java 版本：
```
openjdk version "21.0.10" 2026-01-20 LTS
OpenJDK Runtime Environment Temurin-21.0.10+7 (build 21.0.10+7-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.10+7 (build 21.0.10+7-LTS, mixed mode, sharing)
```

## 部署说明

### 前置条件

1. **JDK 21**: 确保服务器已安装 JDK 21
2. **MySQL 数据库**: 需要 MySQL 5.7+ 或 MySQL 8.x
3. **Redis**: 系统需要 Redis 支持

### 数据库初始化

1. Flowable 会在首次启动时自动创建以下数据表：
   - `ACT_RE_*` - 流程定义和部署相关表
   - `ACT_RU_*` - 运行时数据表
   - `ACT_HI_*` - 历史数据表
   - `ACT_GE_*` - 通用数据表
   - `ACT_ID_*` - 身份信息表

2. 确保数据库用户有足够的权限创建表

### 配置调整

在生产环境中，建议调整以下配置：

1. **application.yml**
   ```yaml
   flowable:
     database-schema-update: false  # 生产环境关闭自动建表
   ```

2. **application-druid.yml**
   - 修改数据库连接信息
   - 调整连接池参数

### 启动应用

```bash
# 使用 Java 21 启动
export JAVA_HOME=/path/to/jdk-21
java -jar ruoyi-admin.jar
```

## API 测试示例

### 查询流程定义列表

```bash
curl -X GET http://localhost:8080/flowable/process/list
```

### 获取流程详情

```bash
curl -X GET http://localhost:8080/flowable/process/{processId}
```

### 删除流程

```bash
curl -X DELETE http://localhost:8080/flowable/process/{deploymentId}
```

### 激活/挂起流程

```bash
# 挂起流程 (suspendState=1)
curl -X PUT http://localhost:8080/flowable/process/changeState/{processId}/1

# 激活流程 (suspendState=0)
curl -X PUT http://localhost:8080/flowable/process/changeState/{processId}/0
```

## 注意事项

1. **JDK 版本要求**: 必须使用 JDK 21
2. **数据库兼容性**: 使用新的 MySQL 驱动 `com.mysql:mysql-connector-j`
3. **Spring Boot 版本**: 2.7.18 是支持 JDK 21 和 Flowable 6.8.1 的稳定版本
4. **Flowable 版本限制**: Flowable 6.8.1 不支持 Spring Boot 3.x，如需使用 Spring Boot 3.x，需升级到 Flowable 7.x

## 后续开发建议

可以基于此工作流模块继续开发以下功能：

1. **流程实例管理**
   - 启动流程实例
   - 查询流程实例
   - 删除流程实例

2. **任务管理**
   - 待办任务查询
   - 任务认领
   - 任务完成
   - 任务委派

3. **流程图展示**
   - 流程定义图展示
   - 流程实例运行轨迹

4. **表单集成**
   - 动态表单
   - 外部表单集成

5. **流程监控**
   - 流程实例监控
   - 任务执行监控

## 技术支持

- RuoYi 官方文档: http://doc.ruoyi.vip/
- Flowable 官方文档: https://www.flowable.com/open-source/docs/
- Spring Boot 2.7.x 文档: https://docs.spring.io/spring-boot/docs/2.7.x/reference/html/

## 版本兼容性总结

| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 21 | LTS 版本 |
| Spring Boot | 2.7.18 | 支持 JDK 21 |
| Flowable | 6.8.1 | 兼容 Spring Boot 2.7.x |
| MySQL Driver | 8.0.33 | 新坐标 com.mysql:mysql-connector-j |
| MySQL | 5.7+ / 8.x | 数据库版本 |

---

**变更完成时间**: 2026-02-11  
**构建状态**: ✅ 成功  
**测试状态**: ⏳ 待数据库环境验证
