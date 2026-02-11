package com.ruoyi.luckysheet.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Luckysheet單元格權限對象 luckysheet_cell_permission
 * 
 * @author ruoyi
 */
public class LuckysheetCellPermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 權限ID */
    private Long id;

    /** 工作簿ID */
    private Long workbookId;

    /** 工作表索引 */
    private String sheetIndex;

    /** 行索引 */
    private Integer rowIndex;

    /** 列索引 */
    private Integer colIndex;

    /** 部門ID */
    private Long deptId;

    /** 用戶ID */
    private Long userId;

    /** 權限類型（0禁止 1只讀 2可編輯） */
    private String permissionType;

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

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getPermissionType()
    {
        return permissionType;
    }

    public void setPermissionType(String permissionType)
    {
        this.permissionType = permissionType;
    }
}
