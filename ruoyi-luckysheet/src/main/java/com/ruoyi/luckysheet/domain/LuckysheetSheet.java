package com.ruoyi.luckysheet.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Luckysheet工作表对象 luckysheet_sheet
 * 
 * @author ruoyi
 */
public class LuckysheetSheet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工作表ID */
    private Long id;

    /** 工作簿ID */
    private Long workbookId;

    /** 工作表索引 */
    private String sheetIndex;

    /** 工作表名称 */
    private String sheetName;

    /** 工作表顺序 */
    private Integer sheetOrder;

    /** 是否隐藏（0否 1是） */
    private String isHidden;

    /** 是否激活（0否 1是） */
    private String isActive;

    /** 工作表配置JSON */
    private String config;

    /** 单元格数据JSON */
    private String cellData;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public String getSheetName()
    {
        return sheetName;
    }

    public void setSheetName(String sheetName)
    {
        this.sheetName = sheetName;
    }

    public Integer getSheetOrder()
    {
        return sheetOrder;
    }

    public void setSheetOrder(Integer sheetOrder)
    {
        this.sheetOrder = sheetOrder;
    }

    public String getIsHidden()
    {
        return isHidden;
    }

    public void setIsHidden(String isHidden)
    {
        this.isHidden = isHidden;
    }

    public String getIsActive()
    {
        return isActive;
    }

    public void setIsActive(String isActive)
    {
        this.isActive = isActive;
    }

    public String getConfig()
    {
        return config;
    }

    public void setConfig(String config)
    {
        this.config = config;
    }

    public String getCellData()
    {
        return cellData;
    }

    public void setCellData(String cellData)
    {
        this.cellData = cellData;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }
}
