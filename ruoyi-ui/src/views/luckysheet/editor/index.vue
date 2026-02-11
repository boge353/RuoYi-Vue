<template>
  <div class="luckysheet-editor">
    <el-page-header @back="goBack" :content="pageTitle">
      <template slot="content">
        <span class="title">{{ pageTitle }}</span>
        <el-button-group style="margin-left: 20px;">
          <el-button size="small" icon="el-icon-document" @click="handleSave">保存</el-button>
          <el-button size="small" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
        </el-button-group>
      </template>
    </el-page-header>

    <div class="editor-container">
      <Luckysheet
        ref="luckysheet"
        :workbook-id="workbookId"
        :data="sheetData"
        width="100%"
        height="calc(100vh - 120px)"
        @save="handleDataSave"
        @cell-updated="handleCellUpdated"
        @sheet-create="handleSheetCreate"
      />
    </div>
  </div>
</template>

<script>
import Luckysheet from '@/components/Luckysheet'
import { getWorkbook } from '@/api/luckysheet/workbook'
import { loadWorkbook, batchUpdateCellData, addSheet, updateSheet } from '@/api/luckysheet/sheet'

export default {
  name: 'LuckysheetEditor',
  components: {
    Luckysheet
  },
  data() {
    return {
      workbookId: null,
      workbookInfo: null,
      sheetData: [],
      pageTitle: '在线表格编辑器'
    }
  },
  created() {
    this.workbookId = this.$route.query.workbookId
    if (this.workbookId) {
      this.loadWorkbookData()
    } else {
      // 如果没有工作簿ID，初始化一个新的
      this.initNewWorkbook()
    }
  },
  methods: {
    /**
     * 加载工作簿数据
     */
    loadWorkbookData() {
      // 加载工作簿信息
      getWorkbook(this.workbookId).then(response => {
        this.workbookInfo = response.data
        this.pageTitle = this.workbookInfo.workbookName
        
        // 加载工作表数据
        this.loadSheets()
      })
    },

    /**
     * 加载工作表数据
     */
    loadSheets() {
      loadWorkbook(this.workbookId).then(response => {
        const sheets = response.data
        if (sheets && sheets.length > 0) {
          // 转换数据格式为Luckysheet需要的格式
          this.sheetData = sheets.map(sheet => {
            return {
              name: sheet.sheetName,
              index: sheet.sheetIndex,
              order: sheet.sheetOrder,
              status: sheet.isActive === '1' ? '1' : '0',
              hide: sheet.isHidden === '1' ? 1 : 0,
              config: sheet.config ? JSON.parse(sheet.config) : {},
              celldata: sheet.cellData ? JSON.parse(sheet.cellData) : [],
              row: 84,
              column: 60
            }
          })
        } else {
          // 如果没有工作表，创建默认的
          this.initNewWorkbook()
        }
      }).catch(() => {
        this.$modal.msgError('加载工作表数据失败')
        this.initNewWorkbook()
      })
    },

    /**
     * 初始化新工作簿
     */
    initNewWorkbook() {
      this.sheetData = [{
        name: 'Sheet1',
        color: '',
        status: '1',
        order: '0',
        index: 'sheet_1',
        celldata: [],
        config: {},
        row: 84,
        column: 60
      }]
    },

    /**
     * 处理数据保存
     */
    handleDataSave(allSheetData) {
      if (!this.workbookId) {
        this.$modal.msgWarning('请先保存工作簿')
        return
      }

      // 保存所有工作表数据
      const savePromises = allSheetData.map((sheet, index) => {
        const sheetData = {
          workbookId: this.workbookId,
          sheetIndex: sheet.index,
          sheetName: sheet.name,
          sheetOrder: index,
          isHidden: sheet.hide ? '1' : '0',
          isActive: sheet.status === '1' ? '1' : '0',
          config: JSON.stringify(sheet.config || {}),
          cellData: JSON.stringify(sheet.celldata || [])
        }

        // 检查工作表是否已存在
        if (sheet.id) {
          sheetData.id = sheet.id
          return updateSheet(sheetData)
        } else {
          return addSheet(sheetData)
        }
      })

      Promise.all(savePromises).then(() => {
        this.$modal.msgSuccess('保存成功')
      }).catch(error => {
        console.error('保存失败:', error)
        this.$modal.msgError('保存失败')
      })
    },

    /**
     * 手动保存
     */
    handleSave() {
      const luckysheetComponent = this.$refs.luckysheet
      if (luckysheetComponent) {
        luckysheetComponent.saveData()
      }
    },

    /**
     * 刷新
     */
    handleRefresh() {
      if (this.workbookId) {
        this.loadWorkbookData()
      }
    },

    /**
     * 单元格更新事件
     */
    handleCellUpdated(event) {
      // 可以在这里处理单元格更新的逻辑
      console.log('Cell updated:', event)
    },

    /**
     * 工作表创建事件
     */
    handleSheetCreate(event) {
      console.log('Sheet created:', event)
      // 自动保存新创建的工作表
      this.handleSave()
    },

    /**
     * 返回
     */
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped lang="scss">
.luckysheet-editor {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;

  .title {
    font-size: 18px;
    font-weight: 500;
  }

  .editor-container {
    flex: 1;
    overflow: hidden;
    padding: 10px;
  }
}
</style>
