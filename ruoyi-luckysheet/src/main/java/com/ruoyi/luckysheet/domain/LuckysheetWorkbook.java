package com.ruoyi.luckysheet.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.annotation.Excel;

/**
 * Luckysheet电子表格对象 luckysheet_workbook
 * 
 * @author ruoyi
 */
public class LuckysheetWorkbook extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工作簿ID */
    private Long id;

    /** 工作簿名称 */
    @Excel(name = "工作簿名称")
    private String workbookName;

    /** 工作簿描述 */
    private String description;

    /** 创建者名称 */
    @Excel(name = "创建者")
    private String createByName;

    /** 更新者名称 */
    private String updateByName;

    /** 状态（0正常 1停用） */
    private String status;

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

    public String getWorkbookName()
    {
        return workbookName;
    }

    public void setWorkbookName(String workbookName)
    {
        this.workbookName = workbookName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCreateByName()
    {
        return createByName;
    }

    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }

    public String getUpdateByName()
    {
        return updateByName;
    }

    public void setUpdateByName(String updateByName)
    {
        this.updateByName = updateByName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
