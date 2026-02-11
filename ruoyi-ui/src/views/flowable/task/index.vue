<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="待办任务" name="todo">
        <el-table v-loading="loading" :data="todoList">
          <el-table-column label="任务名称" align="center" prop="taskName" />
          <el-table-column label="流程实例ID" align="center" prop="processInstanceId" width="200" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
          <el-table-column label="状态" align="center" prop="taskStatus">
            <template slot-scope="scope">
              <el-tag type="warning">{{ scope.row.taskStatus }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-check"
                @click="handleApprove(scope.row)"
              >审批</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-close"
                @click="handleReject(scope.row)"
              >拒绝</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-back"
                @click="handleRollback(scope.row)"
              >回退</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-s-promotion"
                @click="handleTransfer(scope.row)"
              >转办</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="todoTotal>0"
          :total="todoTotal"
          :page.sync="todoQueryParams.pageNum"
          :limit.sync="todoQueryParams.pageSize"
          @pagination="getTodoList"
        />
      </el-tab-pane>

      <el-tab-pane label="已办任务" name="finished">
        <el-table v-loading="loading" :data="finishedList">
          <el-table-column label="任务名称" align="center" prop="taskName" />
          <el-table-column label="流程实例ID" align="center" prop="processInstanceId" width="200" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
          <el-table-column label="完成时间" align="center" prop="endTime" width="180" />
          <el-table-column label="状态" align="center" prop="taskStatus">
            <template slot-scope="scope">
              <el-tag type="success">{{ scope.row.taskStatus }}</el-tag>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="finishedTotal>0"
          :total="finishedTotal"
          :page.sync="finishedQueryParams.pageNum"
          :limit.sync="finishedQueryParams.pageSize"
          @pagination="getFinishedList"
        />
      </el-tab-pane>
    </el-tabs>

    <!-- 审批对话框 -->
    <el-dialog title="审批" :visible.sync="approveOpen" width="500px" append-to-body>
      <el-form ref="approveForm" :model="approveForm" label-width="80px">
        <el-form-item label="审批意见">
          <el-input v-model="approveForm.comment" type="textarea" :rows="4" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitApprove">确 定</el-button>
        <el-button @click="approveOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 拒绝对话框 -->
    <el-dialog title="拒绝" :visible.sync="rejectOpen" width="500px" append-to-body>
      <el-form ref="rejectForm" :model="rejectForm" label-width="80px">
        <el-form-item label="拒绝原因" required>
          <el-input v-model="rejectForm.comment" type="textarea" :rows="4" placeholder="请输入拒绝原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="danger" @click="submitReject">确 定</el-button>
        <el-button @click="rejectOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 回退对话框 -->
    <el-dialog title="回退" :visible.sync="rollbackOpen" width="500px" append-to-body">
      <el-form ref="rollbackForm" :model="rollbackForm" label-width="100px">
        <el-form-item label="回退节点" required>
          <el-input v-model="rollbackForm.targetTaskKey" placeholder="请输入目标任务节点Key" />
        </el-form-item>
        <el-form-item label="回退原因" required>
          <el-input v-model="rollbackForm.comment" type="textarea" :rows="4" placeholder="请输入回退原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" @click="submitRollback">确 定</el-button>
        <el-button @click="rollbackOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 转办对话框 -->
    <el-dialog title="转办" :visible.sync="transferOpen" width="500px" append-to-body">
      <el-form ref="transferForm" :model="transferForm" label-width="80px">
        <el-form-item label="转办人" required>
          <el-input v-model="transferForm.userId" placeholder="请输入转办人用户ID" />
        </el-form-item>
        <el-form-item label="转办说明">
          <el-input v-model="transferForm.comment" type="textarea" :rows="4" placeholder="请输入转办说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTransfer">确 定</el-button>
        <el-button @click="transferOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { todoList, finishedList, completeTask, rejectTask, rollbackTask, transferTask } from "@/api/flowable/task";

export default {
  name: "Task",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 当前标签页
      activeTab: 'todo',
      // 待办任务列表
      todoList: [],
      // 待办任务总数
      todoTotal: 0,
      // 待办查询参数
      todoQueryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 已办任务列表
      finishedList: [],
      // 已办任务总数
      finishedTotal: 0,
      // 已办查询参数
      finishedQueryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 当前任务
      currentTask: null,
      // 审批对话框
      approveOpen: false,
      approveForm: {
        comment: ''
      },
      // 拒绝对话框
      rejectOpen: false,
      rejectForm: {
        comment: ''
      },
      // 回退对话框
      rollbackOpen: false,
      rollbackForm: {
        targetTaskKey: '',
        comment: ''
      },
      // 转办对话框
      transferOpen: false,
      transferForm: {
        userId: '',
        comment: ''
      }
    };
  },
  created() {
    this.getTodoList();
  },
  methods: {
    /** 查询待办任务列表 */
    getTodoList() {
      this.loading = true;
      todoList(this.todoQueryParams).then(response => {
        this.todoList = response.rows;
        this.todoTotal = response.total;
        this.loading = false;
      });
    },
    /** 查询已办任务列表 */
    getFinishedList() {
      this.loading = true;
      finishedList(this.finishedQueryParams).then(response => {
        this.finishedList = response.rows;
        this.finishedTotal = response.total;
        this.loading = false;
      });
    },
    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'todo') {
        this.getTodoList();
      } else {
        this.getFinishedList();
      }
    },
    /** 审批按钮操作 */
    handleApprove(row) {
      this.currentTask = row;
      this.approveForm = {
        comment: ''
      };
      this.approveOpen = true;
    },
    /** 提交审批 */
    submitApprove() {
      const data = {
        comment: this.approveForm.comment,
        variables: {}
      };
      completeTask(this.currentTask.taskId, data).then(response => {
        this.$modal.msgSuccess("审批成功");
        this.approveOpen = false;
        this.getTodoList();
      });
    },
    /** 拒绝按钮操作 */
    handleReject(row) {
      this.currentTask = row;
      this.rejectForm = {
        comment: ''
      };
      this.rejectOpen = true;
    },
    /** 提交拒绝 */
    submitReject() {
      if (!this.rejectForm.comment) {
        this.$modal.msgError("请输入拒绝原因");
        return;
      }
      rejectTask(this.currentTask.taskId, this.rejectForm.comment).then(response => {
        this.$modal.msgSuccess("拒绝成功");
        this.rejectOpen = false;
        this.getTodoList();
      });
    },
    /** 回退按钮操作 */
    handleRollback(row) {
      this.currentTask = row;
      this.rollbackForm = {
        targetTaskKey: '',
        comment: ''
      };
      this.rollbackOpen = true;
    },
    /** 提交回退 */
    submitRollback() {
      if (!this.rollbackForm.targetTaskKey || !this.rollbackForm.comment) {
        this.$modal.msgError("请填写完整信息");
        return;
      }
      rollbackTask(this.currentTask.taskId, this.rollbackForm.targetTaskKey, this.rollbackForm.comment).then(response => {
        this.$modal.msgSuccess("回退成功");
        this.rollbackOpen = false;
        this.getTodoList();
      });
    },
    /** 转办按钮操作 */
    handleTransfer(row) {
      this.currentTask = row;
      this.transferForm = {
        userId: '',
        comment: ''
      };
      this.transferOpen = true;
    },
    /** 提交转办 */
    submitTransfer() {
      if (!this.transferForm.userId) {
        this.$modal.msgError("请输入转办人用户ID");
        return;
      }
      transferTask(this.currentTask.taskId, this.transferForm.userId, this.transferForm.comment).then(response => {
        this.$modal.msgSuccess("转办成功");
        this.transferOpen = false;
        this.getTodoList();
      });
    }
  }
};
</script>
