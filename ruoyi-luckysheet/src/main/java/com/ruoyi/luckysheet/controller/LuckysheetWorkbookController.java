package com.ruoyi.luckysheet.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.luckysheet.domain.LuckysheetWorkbook;
import com.ruoyi.luckysheet.service.ILuckysheetWorkbookService;

/**
 * Luckysheet工作簿Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/luckysheet/workbook")
public class LuckysheetWorkbookController extends BaseController
{
    @Autowired
    private ILuckysheetWorkbookService workbookService;

    /**
     * 查询工作簿列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LuckysheetWorkbook workbook)
    {
        startPage();
        List<LuckysheetWorkbook> list = workbookService.selectWorkbookList(workbook);
        return getDataTable(list);
    }

    /**
     * 导出工作簿列表
     */
    @Log(title = "工作簿", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LuckysheetWorkbook workbook)
    {
        List<LuckysheetWorkbook> list = workbookService.selectWorkbookList(workbook);
        ExcelUtil<LuckysheetWorkbook> util = new ExcelUtil<LuckysheetWorkbook>(LuckysheetWorkbook.class);
        util.exportExcel(response, list, "工作簿数据");
    }

    /**
     * 获取工作簿详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workbookService.selectWorkbookById(id));
    }

    /**
     * 新增工作簿
     */
    @Log(title = "工作簿", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LuckysheetWorkbook workbook)
    {
        return toAjax(workbookService.insertWorkbook(workbook));
    }

    /**
     * 修改工作簿
     */
    @Log(title = "工作簿", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LuckysheetWorkbook workbook)
    {
        return toAjax(workbookService.updateWorkbook(workbook));
    }

    /**
     * 删除工作簿
     */
    @Log(title = "工作簿", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(workbookService.deleteWorkbookByIds(ids));
    }
}
