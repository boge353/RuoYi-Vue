package com.ruoyi.luckysheet.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.luckysheet.mapper.LuckysheetWorkbookMapper;
import com.ruoyi.luckysheet.domain.LuckysheetWorkbook;
import com.ruoyi.luckysheet.service.ILuckysheetWorkbookService;

/**
 * Luckysheet工作簿Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class LuckysheetWorkbookServiceImpl implements ILuckysheetWorkbookService
{
    @Autowired
    private LuckysheetWorkbookMapper workbookMapper;

    /**
     * 查询工作簿列表
     * 
     * @param workbook 工作簿
     * @return 工作簿
     */
    @Override
    public List<LuckysheetWorkbook> selectWorkbookList(LuckysheetWorkbook workbook)
    {
        return workbookMapper.selectWorkbookList(workbook);
    }

    /**
     * 查询工作簿详细
     * 
     * @param id 工作簿主键
     * @return 工作簿
     */
    @Override
    public LuckysheetWorkbook selectWorkbookById(Long id)
    {
        return workbookMapper.selectWorkbookById(id);
    }

    /**
     * 新增工作簿
     * 
     * @param workbook 工作簿
     * @return 结果
     */
    @Override
    public int insertWorkbook(LuckysheetWorkbook workbook)
    {
        return workbookMapper.insertWorkbook(workbook);
    }

    /**
     * 修改工作簿
     * 
     * @param workbook 工作簿
     * @return 结果
     */
    @Override
    public int updateWorkbook(LuckysheetWorkbook workbook)
    {
        return workbookMapper.updateWorkbook(workbook);
    }

    /**
     * 批量删除工作簿
     * 
     * @param ids 需要删除的工作簿主键
     * @return 结果
     */
    @Override
    public int deleteWorkbookByIds(Long[] ids)
    {
        return workbookMapper.deleteWorkbookByIds(ids);
    }

    /**
     * 删除工作簿信息
     * 
     * @param id 工作簿主键
     * @return 结果
     */
    @Override
    public int deleteWorkbookById(Long id)
    {
        return workbookMapper.deleteWorkbookById(id);
    }
}
