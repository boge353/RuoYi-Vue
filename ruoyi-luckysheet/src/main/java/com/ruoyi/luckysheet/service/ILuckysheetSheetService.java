package com.ruoyi.luckysheet.service;

import java.util.List;
import com.ruoyi.luckysheet.domain.LuckysheetSheet;

/**
 * Luckysheet工作表Service接口
 * 
 * @author ruoyi
 */
public interface ILuckysheetSheetService
{
    /**
     * 查询工作表列表
     * 
     * @param sheet 工作表
     * @return 工作表集合
     */
    public List<LuckysheetSheet> selectSheetList(LuckysheetSheet sheet);

    /**
     * 根据工作簿ID查询工作表列表
     * 
     * @param workbookId 工作簿ID
     * @return 工作表集合
     */
    public List<LuckysheetSheet> selectSheetListByWorkbookId(Long workbookId);

    /**
     * 查询工作表详细
     * 
     * @param id 工作表主键
     * @return 工作表
     */
    public LuckysheetSheet selectSheetById(Long id);

    /**
     * 根据工作簿ID和工作表索引查询工作表
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @return 工作表
     */
    public LuckysheetSheet selectSheetByIndex(Long workbookId, String sheetIndex);

    /**
     * 新增工作表
     * 
     * @param sheet 工作表
     * @return 结果
     */
    public int insertSheet(LuckysheetSheet sheet);

    /**
     * 修改工作表
     * 
     * @param sheet 工作表
     * @return 结果
     */
    public int updateSheet(LuckysheetSheet sheet);

    /**
     * 批量更新单元格数据
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param cellData 单元格数据JSON
     * @return 结果
     */
    public int batchUpdateCellData(Long workbookId, String sheetIndex, String cellData);

    /**
     * 批量删除工作表
     * 
     * @param ids 需要删除的工作表主键集合
     * @return 结果
     */
    public int deleteSheetByIds(Long[] ids);

    /**
     * 删除工作表信息
     * 
     * @param id 工作表主键
     * @return 结果
     */
    public int deleteSheetById(Long id);
}
