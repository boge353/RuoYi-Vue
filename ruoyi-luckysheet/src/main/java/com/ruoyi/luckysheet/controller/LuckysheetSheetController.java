package com.ruoyi.luckysheet.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.luckysheet.domain.LuckysheetSheet;
import com.ruoyi.luckysheet.service.ILuckysheetSheetService;

/**
 * Luckysheet工作表Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/luckysheet/sheet")
public class LuckysheetSheetController extends BaseController
{
    @Autowired
    private ILuckysheetSheetService sheetService;

    /**
     * 查询工作表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LuckysheetSheet sheet)
    {
        startPage();
        List<LuckysheetSheet> list = sheetService.selectSheetList(sheet);
        return getDataTable(list);
    }

    /**
     * 根据工作簿ID查询工作表列表
     */
    @GetMapping("/listByWorkbook/{workbookId}")
    public AjaxResult listByWorkbook(@PathVariable("workbookId") Long workbookId)
    {
        List<LuckysheetSheet> list = sheetService.selectSheetListByWorkbookId(workbookId);
        return success(list);
    }

    /**
     * 获取工作表详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sheetService.selectSheetById(id));
    }

    /**
     * 根据工作簿ID和工作表索引查询工作表
     */
    @GetMapping("/getByIndex")
    public AjaxResult getByIndex(@RequestParam("workbookId") Long workbookId, 
                                 @RequestParam("sheetIndex") String sheetIndex)
    {
        return success(sheetService.selectSheetByIndex(workbookId, sheetIndex));
    }

    /**
     * 新增工作表
     */
    @Log(title = "工作表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LuckysheetSheet sheet)
    {
        return toAjax(sheetService.insertSheet(sheet));
    }

    /**
     * 修改工作表
     */
    @Log(title = "工作表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LuckysheetSheet sheet)
    {
        return toAjax(sheetService.updateSheet(sheet));
    }

    /**
     * 批量更新单元格数据
     */
    @Log(title = "批量更新单元格", businessType = BusinessType.UPDATE)
    @PostMapping("/batchUpdateCellData")
    public AjaxResult batchUpdateCellData(@RequestBody Map<String, Object> params)
    {
        Long workbookId = Long.valueOf(params.get("workbookId").toString());
        String sheetIndex = params.get("sheetIndex").toString();
        String cellData = params.get("cellData").toString();
        return toAjax(sheetService.batchUpdateCellData(workbookId, sheetIndex, cellData));
    }

    /**
     * 删除工作表
     */
    @Log(title = "工作表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sheetService.deleteSheetByIds(ids));
    }

    /**
     * 加载工作簿的所有工作表数据（用于Luckysheet初始化）
     */
    @GetMapping("/load/{workbookId}")
    public AjaxResult loadWorkbook(@PathVariable("workbookId") Long workbookId)
    {
        List<LuckysheetSheet> sheets = sheetService.selectSheetListByWorkbookId(workbookId);
        return success(sheets);
    }
}
