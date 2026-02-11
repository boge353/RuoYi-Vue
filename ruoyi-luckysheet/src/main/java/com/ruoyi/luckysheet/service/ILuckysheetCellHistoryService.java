package com.ruoyi.luckysheet.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.luckysheet.domain.LuckysheetCellHistory;

/**
 * Luckysheet單元格歷史Service接口
 * 
 * @author ruoyi
 */
public interface ILuckysheetCellHistoryService
{
    /**
     * 查詢單元格操作歷史列表
     * 
     * @param history 單元格歷史
     * @return 單元格歷史集合
     */
    public List<LuckysheetCellHistory> selectHistoryList(LuckysheetCellHistory history);

    /**
     * 查詢指定單元格的操作歷史
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 歷史記錄集合
     */
    public List<LuckysheetCellHistory> selectCellHistory(Long workbookId, String sheetIndex, Integer rowIndex, Integer colIndex);

    /**
     * 記錄單元格操作
     * 
     * @param history 單元格歷史
     * @return 結果
     */
    public int insertHistory(LuckysheetCellHistory history);

    /**
     * 批量記錄單元格操作
     * 
     * @param historyList 歷史記錄列表
     * @return 結果
     */
    public int batchInsertHistory(List<LuckysheetCellHistory> historyList);

    /**
     * 歸檔指定單元格的歷史記錄
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 結果
     */
    public int archiveHistory(Long workbookId, String sheetIndex, Integer rowIndex, Integer colIndex);

    /**
     * 獲取單元格的部門編輯統計（用於數據匯總）
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 統計結果
     */
    public List<Map<String, Object>> getDeptEditStatistics(Long workbookId, String sheetIndex, Integer rowIndex, Integer colIndex);

    /**
     * 批量刪除單元格歷史
     * 
     * @param ids 需要刪除的單元格歷史主鍵集合
     * @return 結果
     */
    public int deleteHistoryByIds(Long[] ids);

    /**
     * 刪除單元格歷史信息
     * 
     * @param id 單元格歷史主鍵
     * @return 結果
     */
    public int deleteHistoryById(Long id);
}
