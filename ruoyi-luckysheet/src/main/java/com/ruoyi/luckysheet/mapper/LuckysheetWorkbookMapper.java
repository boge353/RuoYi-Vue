package com.ruoyi.luckysheet.mapper;

import java.util.List;
import com.ruoyi.luckysheet.domain.LuckysheetWorkbook;

/**
 * Luckysheet工作簿Mapper接口
 * 
 * @author ruoyi
 */
public interface LuckysheetWorkbookMapper
{
    /**
     * 查询工作簿列表
     * 
     * @param workbook 工作簿
     * @return 工作簿集合
     */
    public List<LuckysheetWorkbook> selectWorkbookList(LuckysheetWorkbook workbook);

    /**
     * 查询工作簿详细
     * 
     * @param id 工作簿主键
     * @return 工作簿
     */
    public LuckysheetWorkbook selectWorkbookById(Long id);

    /**
     * 新增工作簿
     * 
     * @param workbook 工作簿
     * @return 结果
     */
    public int insertWorkbook(LuckysheetWorkbook workbook);

    /**
     * 修改工作簿
     * 
     * @param workbook 工作簿
     * @return 结果
     */
    public int updateWorkbook(LuckysheetWorkbook workbook);

    /**
     * 删除工作簿
     * 
     * @param id 工作簿主键
     * @return 结果
     */
    public int deleteWorkbookById(Long id);

    /**
     * 批量删除工作簿
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkbookByIds(Long[] ids);
}
