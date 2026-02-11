package com.ruoyi.luckysheet.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * Luckysheet單元格操作歷史對象 luckysheet_cell_history
 * 
 * @author ruoyi
 */
public class LuckysheetCellHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 歷史ID */
    private Long id;

    /** 工作簿ID */
    private Long workbookId;

    /** 工作表索引 */
    private String sheetIndex;

    /** 行索引 */
    private Integer rowIndex;

    /** 列索引 */
    private Integer colIndex;

    /** 操作類型（INSERT,UPDATE,DELETE,MERGE,SPLIT） */
    private String operationType;

    /** 舊值JSON */
    private String oldValue;

    /** 新值JSON */
    private String newValue;

    /** 操作者用戶ID */
    private Long operatorId;

    /** 操作者名稱 */
    private String operatorName;

    /** 操作者部門ID */
    private Long deptId;

    /** 操作者部門名稱 */
    private String deptName;

    /** 操作時間 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

    /** 客戶端IP */
    private String clientIp;

    /** 是否已歸檔（0否 1是） */
    private String isArchived;

    /** 歸檔時間 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date archiveTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getWorkbookId()
    {
        return workbookId;
    }

    public void setWorkbookId(Long workbookId)
    {
        this.workbookId = workbookId;
    }

    public String getSheetIndex()
    {
        return sheetIndex;
    }

    public void setSheetIndex(String sheetIndex)
    {
        this.sheetIndex = sheetIndex;
    }

    public Integer getRowIndex()
    {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex)
    {
        this.rowIndex = rowIndex;
    }

    public Integer getColIndex()
    {
        return colIndex;
    }

    public void setColIndex(Integer colIndex)
    {
        this.colIndex = colIndex;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType(String operationType)
    {
        this.operationType = operationType;
    }

    public String getOldValue()
    {
        return oldValue;
    }

    public void setOldValue(String oldValue)
    {
        this.oldValue = oldValue;
    }

    public String getNewValue()
    {
        return newValue;
    }

    public void setNewValue(String newValue)
    {
        this.newValue = newValue;
    }

    public Long getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(Long operatorId)
    {
        this.operatorId = operatorId;
    }

    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public Date getOperationTime()
    {
        return operationTime;
    }

    public void setOperationTime(Date operationTime)
    {
        this.operationTime = operationTime;
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp;
    }

    public String getIsArchived()
    {
        return isArchived;
    }

    public void setIsArchived(String isArchived)
    {
        this.isArchived = isArchived;
    }

    public Date getArchiveTime()
    {
        return archiveTime;
    }

    public void setArchiveTime(Date archiveTime)
    {
        this.archiveTime = archiveTime;
    }
}
