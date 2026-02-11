<template>
  <div class="luckysheet-container">
    <div 
      id="luckysheet" 
      :style="{ width: width, height: height }"
    ></div>
  </div>
</template>

<script>
import luckysheet from 'luckysheet'
import 'luckysheet/dist/plugins/css/pluginsCss.css'
import 'luckysheet/dist/plugins/plugins.css'
import 'luckysheet/dist/css/luckysheet.css'
import 'luckysheet/dist/assets/iconfont/iconfont.css'

export default {
  name: 'Luckysheet',
  props: {
    // 工作簿ID
    workbookId: {
      type: [String, Number],
      required: false
    },
    // 工作簿数据
    data: {
      type: Array,
      default: () => []
    },
    // 容器宽度
    width: {
      type: String,
      default: '100%'
    },
    // 容器高度
    height: {
      type: String,
      default: '600px'
    },
    // 是否只读
    readOnly: {
      type: Boolean,
      default: false
    },
    // 配置选项
    options: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      luckysheetInstance: null,
      autoSaveTimer: null
    }
  },
  mounted() {
    this.initLuckysheet()
  },
  beforeDestroy() {
    this.destroyLuckysheet()
  },
  methods: {
    /**
     * 初始化Luckysheet
     */
    initLuckysheet() {
      const that = this
      
      // 默认配置
      const defaultOptions = {
        container: 'luckysheet',
        lang: 'zh',
        showinfobar: false,
        showsheetbar: true,
        showstatisticBar: true,
        sheetFormulaBar: true,
        enableAddRow: true,
        enableAddBackTop: true,
        userInfo: false,
        myFolderUrl: '',
        devicePixelRatio: 1,
        allowCopy: true,
        showtoolbar: true,
        showtoolbarConfig: {
          undoRedo: true, // 撤销重做
          paintFormat: true, // 格式刷
          currencyFormat: true, // 货币格式
          percentageFormat: true, // 百分比格式
          numberDecrease: true, // 减少小数位
          numberIncrease: true, // 增加小数位
          moreFormats: true, // 更多格式
          font: true, // 字体
          fontSize: true, // 字号
          bold: true, // 粗体
          italic: true, // 斜体
          strikethrough: true, // 删除线
          underline: true, // 下划线
          textColor: true, // 文本颜色
          fillColor: true, // 背景颜色
          border: true, // 边框
          mergeCell: true, // 合并单元格
          horizontalAlignMode: true, // 水平对齐
          verticalAlignMode: true, // 垂直对齐
          textWrapMode: true, // 文本换行
          textRotateMode: true, // 文本旋转
          image: true, // 插入图片
          link: true, // 插入链接
          chart: true, // 图表
          postil: true, // 批注
          pivotTable: true, // 数据透视表
          function: true, // 公式
          frozenMode: true, // 冻结
          sortAndFilter: true, // 排序和筛选
          conditionalFormat: true, // 条件格式
          dataVerification: true, // 数据验证
          splitColumn: true, // 分列
          screenshot: true, // 截图
          findAndReplace: true, // 查找替换
          protection: true, // 工作表保护
          print: true // 打印
        },
        // 自定义单元格右键菜单
        cellRightClickConfig: {
          copy: true, // 复制
          copyAs: true, // 复制为
          paste: true, // 粘贴
          insertRow: true, // 插入行
          insertColumn: true, // 插入列
          deleteRow: true, // 删除行
          deleteColumn: true, // 删除列
          deleteCell: true, // 删除单元格
          hideRow: true, // 隐藏行
          hideColumn: true, // 隐藏列
          rowHeight: true, // 行高
          columnWidth: true, // 列宽
          clear: true, // 清除内容
          matrix: true, // 矩阵操作
          sort: true, // 排序
          filter: true, // 筛选
          chart: true, // 图表
          image: true, // 图片
          link: true, // 链接
          data: true, // 数据验证
          cellFormat: true // 设置单元格格式
        },
        // 数据
        data: this.data && this.data.length > 0 ? this.data : this.getDefaultData(),
        // 钩子函数
        hook: {
          // 单元格更新之后
          cellUpdated: function(r, c, oldValue, newValue, isRefresh) {
            that.onCellUpdated(r, c, oldValue, newValue, isRefresh)
          },
          // 范围更新之后
          rangeSelect: function(sheet, range) {
            that.onRangeSelect(sheet, range)
          },
          // 工作表激活之前
          sheetActivate: function(index, isPivotInitial, isNewSheet) {
            that.onSheetActivate(index, isPivotInitial, isNewSheet)
          },
          // 工作表创建之后
          sheetCreateAfter: function(sheetIndex, sheetObject) {
            that.onSheetCreateAfter(sheetIndex, sheetObject)
          }
        }
      }

      // 合并用户配置
      const finalOptions = Object.assign({}, defaultOptions, this.options)

      // 创建实例
      luckysheet.create(finalOptions)
      
      this.luckysheetInstance = luckysheet

      // 如果是只读模式
      if (this.readOnly) {
        this.setReadOnly(true)
      }

      // 启动自动保存
      this.startAutoSave()
    },

    /**
     * 获取默认数据
     */
    getDefaultData() {
      return [{
        name: 'Sheet1',
        color: '',
        status: '1',
        order: '0',
        data: [],
        config: {},
        index: 'sheet_1',
        celldata: []
      }]
    },

    /**
     * 设置只读模式
     */
    setReadOnly(readOnly) {
      if (this.luckysheetInstance) {
        // Luckysheet的只读模式需要通过CSS或配置实现
        const container = document.getElementById('luckysheet')
        if (readOnly) {
          container.style.pointerEvents = 'none'
        } else {
          container.style.pointerEvents = 'auto'
        }
      }
    },

    /**
     * 单元格更新回调
     */
    onCellUpdated(r, c, oldValue, newValue, isRefresh) {
      this.$emit('cell-updated', { row: r, col: c, oldValue, newValue, isRefresh })
      
      // 触发自动保存
      if (!isRefresh) {
        this.triggerAutoSave()
      }
    },

    /**
     * 范围选择回调
     */
    onRangeSelect(sheet, range) {
      this.$emit('range-select', { sheet, range })
    },

    /**
     * 工作表激活回调
     */
    onSheetActivate(index, isPivotInitial, isNewSheet) {
      this.$emit('sheet-activate', { index, isPivotInitial, isNewSheet })
    },

    /**
     * 工作表创建回调
     */
    onSheetCreateAfter(sheetIndex, sheetObject) {
      this.$emit('sheet-create', { sheetIndex, sheetObject })
    },

    /**
     * 启动自动保存
     */
    startAutoSave() {
      // 每30秒自动保存一次
      if (this.workbookId && !this.readOnly) {
        this.autoSaveTimer = setInterval(() => {
          this.saveData()
        }, 30000)
      }
    },

    /**
     * 触发自动保存（防抖）
     */
    triggerAutoSave() {
      if (this.saveDebounceTimer) {
        clearTimeout(this.saveDebounceTimer)
      }
      this.saveDebounceTimer = setTimeout(() => {
        this.saveData()
      }, 3000)
    },

    /**
     * 保存数据
     */
    saveData() {
      if (!this.workbookId || this.readOnly) {
        return
      }

      const allSheetData = this.getAllSheetData()
      this.$emit('save', allSheetData)
    },

    /**
     * 获取所有工作表数据
     */
    getAllSheetData() {
      if (!this.luckysheetInstance) {
        return []
      }
      return luckysheet.getAllSheets()
    },

    /**
     * 获取当前工作表数据
     */
    getCurrentSheetData() {
      if (!this.luckysheetInstance) {
        return null
      }
      return luckysheet.getSheet()
    },

    /**
     * 获取指定单元格的值
     */
    getCellValue(row, col, sheetIndex) {
      if (!this.luckysheetInstance) {
        return null
      }
      return luckysheet.getCellValue(row, col, { order: sheetIndex })
    },

    /**
     * 设置指定单元格的值
     */
    setCellValue(row, col, value, sheetIndex) {
      if (!this.luckysheetInstance) {
        return
      }
      luckysheet.setCellValue(row, col, value, { order: sheetIndex })
    },

    /**
     * 刷新表格
     */
    refresh() {
      if (this.luckysheetInstance) {
        luckysheet.refresh()
      }
    },

    /**
     * 销毁实例
     */
    destroyLuckysheet() {
      if (this.autoSaveTimer) {
        clearInterval(this.autoSaveTimer)
      }
      if (this.saveDebounceTimer) {
        clearTimeout(this.saveDebounceTimer)
      }
      if (this.luckysheetInstance) {
        luckysheet.destroy()
        this.luckysheetInstance = null
      }
    }
  }
}
</script>

<style scoped>
.luckysheet-container {
  width: 100%;
  height: 100%;
}
</style>
