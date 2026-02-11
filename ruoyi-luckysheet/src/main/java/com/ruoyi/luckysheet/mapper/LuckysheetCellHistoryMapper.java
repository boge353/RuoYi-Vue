package com.ruoyi.luckysheet.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.luckysheet.domain.LuckysheetCellHistory;

/**
 * Luckysheet單元格歷史Mapper接口
 * 
 * @author ruoyi
 */
public interface LuckysheetCellHistoryMapper
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
    public List<LuckysheetCellHistory> selectCellHistory(@Param("workbookId") Long workbookId,
                                                         @Param("sheetIndex") String sheetIndex,
                                                         @Param("rowIndex") Integer rowIndex,
                                                         @Param("colIndex") Integer colIndex);

    /**
     * 新增單元格操作歷史
     * 
     * @param history 單元格歷史
     * @return 結果
     */
    public int insertHistory(LuckysheetCellHistory history);

    /**
     * 批量新增單元格操作歷史
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
    public int archiveHistory(@Param("workbookId") Long workbookId,
                             @Param("sheetIndex") String sheetIndex,
                             @Param("rowIndex") Integer rowIndex,
                             @Param("colIndex") Integer colIndex);

    /**
     * 統計各部門對單元格的編輯次數（用於數據匯總）
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 統計結果
     */
    public List<Map<String, Object>> selectDeptEditStatistics(@Param("workbookId") Long workbookId,
                                                              @Param("sheetIndex") String sheetIndex,
                                                              @Param("rowIndex") Integer rowIndex,
                                                              @Param("colIndex") Integer colIndex);

    /**
     * 刪除單元格歷史
     * 
     * @param id 單元格歷史主鍵
     * @return 結果
     */
    public int deleteHistoryById(Long id);

    /**
     * 批量刪除單元格歷史
     * 
     * @param ids 需要刪除的數據主鍵集合
     * @return 結果
     */
    public int deleteHistoryByIds(Long[] ids);
}
