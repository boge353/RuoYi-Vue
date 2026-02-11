<template>
  <div class="process-designer">
    <div class="designer-container">
      <div class="canvas" ref="canvas"></div>
      <div class="properties-panel" ref="propertiesPanel"></div>
    </div>
    <div class="designer-toolbar">
      <el-button type="primary" size="small" @click="handleSave">保存</el-button>
      <el-button size="small" @click="handleExport">导出XML</el-button>
      <el-button size="small" @click="handleZoomIn">放大</el-button>
      <el-button size="small" @click="handleZoomOut">缩小</el-button>
      <el-button size="small" @click="handleZoomReset">还原</el-button>
    </div>
  </div>
</template>

<script>
import BpmnModeler from 'bpmn-js/lib/Modeler'
import {
  BpmnPropertiesPanelModule,
  BpmnPropertiesProviderModule,
} from 'bpmn-js-properties-panel'
import CamundaBpmnModdle from 'camunda-bpmn-moddle/resources/camunda'

export default {
  name: 'ProcessDesigner',
  props: {
    xml: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      modeler: null,
      defaultXml: `<?xml version="1.0" encoding="UTF-8"?>
<bpmn2:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL"
  xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
  xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"
  xmlns:di="http://www.omg.org/spec/DD/20100524/DI"
  xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd"
  id="sample-diagram"
  targetNamespace="http://bpmn.io/schema/bpmn">
  <bpmn2:process id="Process_1" isExecutable="true">
    <bpmn2:startEvent id="StartEvent_1"/>
  </bpmn2:process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">
      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">
        <dc:Bounds height="36.0" width="36.0" x="173.0" y="102.0"/>
      </bpmndi:BPMNShape>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</bpmn2:definitions>`
    }
  },
  mounted() {
    this.initModeler()
  },
  methods: {
    initModeler() {
      this.modeler = new BpmnModeler({
        container: this.$refs.canvas,
        propertiesPanel: {
          parent: this.$refs.propertiesPanel
        },
        additionalModules: [
          BpmnPropertiesPanelModule,
          BpmnPropertiesProviderModule
        ],
        moddleExtensions: {
          camunda: CamundaBpmnModdle
        }
      })

      this.createNewDiagram()
    },
    async createNewDiagram() {
      try {
        const xml = this.xml || this.defaultXml
        await this.modeler.importXML(xml)
        const canvas = this.modeler.get('canvas')
        canvas.zoom('fit-viewport')
      } catch (err) {
        console.error('导入流程图失败', err)
        this.$message.error('导入流程图失败: ' + err.message)
      }
    },
    async handleSave() {
      try {
        const { xml } = await this.modeler.saveXML({ format: true })
        this.$emit('save', xml)
        this.$message.success('保存成功')
      } catch (err) {
        console.error('保存失败', err)
        this.$message.error('保存失败: ' + err.message)
      }
    },
    async handleExport() {
      try {
        const { xml } = await this.modeler.saveXML({ format: true })
        const blob = new Blob([xml], { type: 'text/xml' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = 'process.bpmn'
        link.click()
        URL.revokeObjectURL(url)
      } catch (err) {
        console.error('导出失败', err)
        this.$message.error('导出失败: ' + err.message)
      }
    },
    handleZoomIn() {
      const canvas = this.modeler.get('canvas')
      const zoom = canvas.zoom()
      canvas.zoom(zoom + 0.1)
    },
    handleZoomOut() {
      const canvas = this.modeler.get('canvas')
      const zoom = canvas.zoom()
      canvas.zoom(zoom - 0.1)
    },
    handleZoomReset() {
      const canvas = this.modeler.get('canvas')
      canvas.zoom('fit-viewport')
    }
  },
  beforeDestroy() {
    if (this.modeler) {
      this.modeler.destroy()
    }
  }
}
</script>

<style scoped lang="scss">
.process-designer {
  height: calc(100vh - 200px);
  display: flex;
  flex-direction: column;
}

.designer-container {
  flex: 1;
  display: flex;
  position: relative;
  border: 1px solid #ddd;
}

.canvas {
  flex: 1;
  height: 100%;
}

.properties-panel {
  width: 300px;
  height: 100%;
  overflow: auto;
  border-left: 1px solid #ddd;
}

.designer-toolbar {
  padding: 10px;
  background: #f5f5f5;
  border: 1px solid #ddd;
  border-top: none;
}
</style>

<style>
/* Import bpmn-js styles */
@import '~bpmn-js/dist/assets/diagram-js.css';
@import '~bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
@import '~bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css';
@import '~bpmn-js-properties-panel/dist/assets/properties-panel.css';
</style>
