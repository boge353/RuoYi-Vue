package com.ruoyi.luckysheet.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.luckysheet.mapper.LuckysheetSheetMapper;
import com.ruoyi.luckysheet.domain.LuckysheetSheet;
import com.ruoyi.luckysheet.service.ILuckysheetSheetService;

/**
 * Luckysheet工作表Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class LuckysheetSheetServiceImpl implements ILuckysheetSheetService
{
    @Autowired
    private LuckysheetSheetMapper sheetMapper;

    /**
     * 查询工作表列表
     * 
     * @param sheet 工作表
     * @return 工作表
     */
    @Override
    public List<LuckysheetSheet> selectSheetList(LuckysheetSheet sheet)
    {
        return sheetMapper.selectSheetList(sheet);
    }

    /**
     * 根据工作簿ID查询工作表列表
     * 
     * @param workbookId 工作簿ID
     * @return 工作表集合
     */
    @Override
    public List<LuckysheetSheet> selectSheetListByWorkbookId(Long workbookId)
    {
        return sheetMapper.selectSheetListByWorkbookId(workbookId);
    }

    /**
     * 查询工作表详细
     * 
     * @param id 工作表主键
     * @return 工作表
     */
    @Override
    public LuckysheetSheet selectSheetById(Long id)
    {
        return sheetMapper.selectSheetById(id);
    }

    /**
     * 根据工作簿ID和工作表索引查询工作表
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @return 工作表
     */
    @Override
    public LuckysheetSheet selectSheetByIndex(Long workbookId, String sheetIndex)
    {
        return sheetMapper.selectSheetByIndex(workbookId, sheetIndex);
    }

    /**
     * 新增工作表
     * 
     * @param sheet 工作表
     * @return 结果
     */
    @Override
    public int insertSheet(LuckysheetSheet sheet)
    {
        return sheetMapper.insertSheet(sheet);
    }

    /**
     * 修改工作表
     * 
     * @param sheet 工作表
     * @return 结果
     */
    @Override
    public int updateSheet(LuckysheetSheet sheet)
    {
        return sheetMapper.updateSheet(sheet);
    }

    /**
     * 批量更新单元格数据
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param cellData 单元格数据JSON
     * @return 结果
     */
    @Override
    public int batchUpdateCellData(Long workbookId, String sheetIndex, String cellData)
    {
        LuckysheetSheet sheet = selectSheetByIndex(workbookId, sheetIndex);
        if (sheet != null)
        {
            sheet.setCellData(cellData);
            return updateSheet(sheet);
        }
        return 0;
    }

    /**
     * 批量删除工作表
     * 
     * @param ids 需要删除的工作表主键
     * @return 结果
     */
    @Override
    public int deleteSheetByIds(Long[] ids)
    {
        return sheetMapper.deleteSheetByIds(ids);
    }

    /**
     * 删除工作表信息
     * 
     * @param id 工作表主键
     * @return 结果
     */
    @Override
    public int deleteSheetById(Long id)
    {
        return sheetMapper.deleteSheetById(id);
    }
}
