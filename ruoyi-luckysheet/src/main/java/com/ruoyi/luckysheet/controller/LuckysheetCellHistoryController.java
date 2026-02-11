package com.ruoyi.luckysheet.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.luckysheet.domain.LuckysheetCellHistory;
import com.ruoyi.luckysheet.service.ILuckysheetCellHistoryService;

/**
 * Luckysheet單元格歷史Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/luckysheet/history")
public class LuckysheetCellHistoryController extends BaseController
{
    @Autowired
    private ILuckysheetCellHistoryService historyService;

    /**
     * 查詢單元格操作歷史列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LuckysheetCellHistory history)
    {
        startPage();
        List<LuckysheetCellHistory> list = historyService.selectHistoryList(history);
        return getDataTable(list);
    }

    /**
     * 查詢指定單元格的操作歷史
     */
    @GetMapping("/cell")
    public AjaxResult getCellHistory(@RequestParam("workbookId") Long workbookId,
                                     @RequestParam("sheetIndex") String sheetIndex,
                                     @RequestParam("rowIndex") Integer rowIndex,
                                     @RequestParam("colIndex") Integer colIndex)
    {
        List<LuckysheetCellHistory> list = historyService.selectCellHistory(workbookId, sheetIndex, rowIndex, colIndex);
        return success(list);
    }

    /**
     * 記錄單元格操作（通常由系統內部調用）
     */
    @Log(title = "單元格操作", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LuckysheetCellHistory history)
    {
        return toAjax(historyService.insertHistory(history));
    }

    /**
     * 歸檔指定單元格的歷史記錄
     */
    @Log(title = "歸檔單元格歷史", businessType = BusinessType.UPDATE)
    @PostMapping("/archive")
    public AjaxResult archive(@RequestParam("workbookId") Long workbookId,
                             @RequestParam("sheetIndex") String sheetIndex,
                             @RequestParam("rowIndex") Integer rowIndex,
                             @RequestParam("colIndex") Integer colIndex)
    {
        int result = historyService.archiveHistory(workbookId, sheetIndex, rowIndex, colIndex);
        return toAjax(result);
    }

    /**
     * 獲取單元格的部門編輯統計（用於數據匯總）
     */
    @GetMapping("/statistics")
    public AjaxResult getDeptStatistics(@RequestParam("workbookId") Long workbookId,
                                       @RequestParam("sheetIndex") String sheetIndex,
                                       @RequestParam("rowIndex") Integer rowIndex,
                                       @RequestParam("colIndex") Integer colIndex)
    {
        List<Map<String, Object>> statistics = historyService.getDeptEditStatistics(workbookId, sheetIndex, rowIndex, colIndex);
        return success(statistics);
    }

    /**
     * 刪除單元格歷史
     */
    @Log(title = "單元格歷史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(historyService.deleteHistoryByIds(ids));
    }
}
