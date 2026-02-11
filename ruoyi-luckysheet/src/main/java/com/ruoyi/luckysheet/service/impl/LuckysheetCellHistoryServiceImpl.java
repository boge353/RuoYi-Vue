package com.ruoyi.luckysheet.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.luckysheet.mapper.LuckysheetCellHistoryMapper;
import com.ruoyi.luckysheet.domain.LuckysheetCellHistory;
import com.ruoyi.luckysheet.service.ILuckysheetCellHistoryService;

/**
 * Luckysheet單元格歷史Service業務層處理
 * 
 * @author ruoyi
 */
@Service
public class LuckysheetCellHistoryServiceImpl implements ILuckysheetCellHistoryService
{
    @Autowired
    private LuckysheetCellHistoryMapper historyMapper;

    /**
     * 查詢單元格操作歷史列表
     * 
     * @param history 單元格歷史
     * @return 單元格歷史
     */
    @Override
    public List<LuckysheetCellHistory> selectHistoryList(LuckysheetCellHistory history)
    {
        return historyMapper.selectHistoryList(history);
    }

    /**
     * 查詢指定單元格的操作歷史
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 歷史記錄集合
     */
    @Override
    public List<LuckysheetCellHistory> selectCellHistory(Long workbookId, String sheetIndex, Integer rowIndex, Integer colIndex)
    {
        return historyMapper.selectCellHistory(workbookId, sheetIndex, rowIndex, colIndex);
    }

    /**
     * 記錄單元格操作
     * 
     * @param history 單元格歷史
     * @return 結果
     */
    @Override
    public int insertHistory(LuckysheetCellHistory history)
    {
        return historyMapper.insertHistory(history);
    }

    /**
     * 批量記錄單元格操作
     * 
     * @param historyList 歷史記錄列表
     * @return 結果
     */
    @Override
    public int batchInsertHistory(List<LuckysheetCellHistory> historyList)
    {
        return historyMapper.batchInsertHistory(historyList);
    }

    /**
     * 歸檔指定單元格的歷史記錄
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 結果
     */
    @Override
    public int archiveHistory(Long workbookId, String sheetIndex, Integer rowIndex, Integer colIndex)
    {
        return historyMapper.archiveHistory(workbookId, sheetIndex, rowIndex, colIndex);
    }

    /**
     * 獲取單元格的部門編輯統計（用於數據匯總）
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 統計結果
     */
    @Override
    public List<Map<String, Object>> getDeptEditStatistics(Long workbookId, String sheetIndex, Integer rowIndex, Integer colIndex)
    {
        return historyMapper.selectDeptEditStatistics(workbookId, sheetIndex, rowIndex, colIndex);
    }

    /**
     * 批量刪除單元格歷史
     * 
     * @param ids 需要刪除的單元格歷史主鍵
     * @return 結果
     */
    @Override
    public int deleteHistoryByIds(Long[] ids)
    {
        return historyMapper.deleteHistoryByIds(ids);
    }

    /**
     * 刪除單元格歷史信息
     * 
     * @param id 單元格歷史主鍵
     * @return 結果
     */
    @Override
    public int deleteHistoryById(Long id)
    {
        return historyMapper.deleteHistoryById(id);
    }
}
