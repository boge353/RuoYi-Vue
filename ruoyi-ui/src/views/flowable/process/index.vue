<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="流程名称" prop="processName">
        <el-input
          v-model="queryParams.processName"
          placeholder="请输入流程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增流程</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="processList">
      <el-table-column label="流程ID" align="center" prop="processId" width="200" />
      <el-table-column label="流程名称" align="center" prop="processName" />
      <el-table-column label="流程分类" align="center" prop="category" />
      <el-table-column label="版本" align="center" prop="version" />
      <el-table-column label="状态" align="center" prop="suspendState">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.suspendState === '0'" type="success">激活</el-tag>
          <el-tag v-else type="danger">挂起</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDesign(scope.row)"
          >设计</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
          <el-button
            v-if="scope.row.suspendState === '0'"
            size="mini"
            type="text"
            @click="handleChangeState(scope.row, '1')"
          >挂起</el-button>
          <el-button
            v-else
            size="mini"
            type="text"
            @click="handleChangeState(scope.row, '0')"
          >激活</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 流程设计器对话框 -->
    <el-dialog title="流程设计" :visible.sync="designOpen" width="90%" append-to-body>
      <process-designer :xml="processXml" @save="handleSaveDesign" />
    </el-dialog>
  </div>
</template>

<script>
import { listProcess, getProcess, delProcess, changeProcessState } from "@/api/flowable/process";
import ProcessDesigner from "@/components/ProcessDesigner";

export default {
  name: "Process",
  components: {
    ProcessDesigner
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 流程定义表格数据
      processList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        processName: null
      },
      // 设计器对话框
      designOpen: false,
      // 流程XML
      processXml: '',
      // 当前流程
      currentProcess: null
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程定义列表 */
    getList() {
      this.loading = true;
      listProcess(this.queryParams).then(response => {
        this.processList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.currentProcess = null;
      this.processXml = '';
      this.designOpen = true;
    },
    /** 设计按钮操作 */
    handleDesign(row) {
      this.currentProcess = row;
      getProcess(row.processId).then(response => {
        this.processXml = response.data.xml || '';
        this.designOpen = true;
      });
    },
    /** 保存设计 */
    handleSaveDesign(xml) {
      // TODO: 实现保存流程定义的接口
      console.log('Save XML:', xml);
      this.designOpen = false;
      this.getList();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除流程定义？').then(function() {
        return delProcess(row.deploymentId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 改变流程状态 */
    handleChangeState(row, suspendState) {
      const text = suspendState === '0' ? '激活' : '挂起';
      this.$modal.confirm('确认要' + text + '该流程吗？').then(function() {
        return changeProcessState(row.processId, suspendState);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(text + "成功");
      }).catch(() => {});
    }
  }
};
</script>
