package com.ruoyi.luckysheet.controller;

import java.util.List;
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
import com.ruoyi.luckysheet.domain.LuckysheetCellPermission;
import com.ruoyi.luckysheet.service.ILuckysheetCellPermissionService;

/**
 * Luckysheet單元格權限Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/luckysheet/permission")
public class LuckysheetCellPermissionController extends BaseController
{
    @Autowired
    private ILuckysheetCellPermissionService permissionService;

    /**
     * 查詢單元格權限列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LuckysheetCellPermission permission)
    {
        startPage();
        List<LuckysheetCellPermission> list = permissionService.selectPermissionList(permission);
        return getDataTable(list);
    }

    /**
     * 檢查單元格權限
     */
    @GetMapping("/check")
    public AjaxResult checkPermission(@RequestParam("workbookId") Long workbookId,
                                     @RequestParam("sheetIndex") String sheetIndex,
                                     @RequestParam("rowIndex") Integer rowIndex,
                                     @RequestParam("colIndex") Integer colIndex,
                                     @RequestParam("userId") Long userId,
                                     @RequestParam("deptId") Long deptId)
    {
        String permissionType = permissionService.checkCellPermission(workbookId, sheetIndex, rowIndex, colIndex, userId, deptId);
        return success(permissionType);
    }

    /**
     * 獲取工作表的所有單元格權限（用於前端渲染）
     */
    @GetMapping("/sheet/{workbookId}/{sheetIndex}")
    public AjaxResult getSheetPermissions(@PathVariable("workbookId") Long workbookId,
                                         @PathVariable("sheetIndex") String sheetIndex,
                                         @RequestParam("userId") Long userId,
                                         @RequestParam("deptId") Long deptId)
    {
        List<LuckysheetCellPermission> list = permissionService.selectSheetPermissions(workbookId, sheetIndex, userId, deptId);
        return success(list);
    }

    /**
     * 新增單元格權限
     */
    @Log(title = "單元格權限", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LuckysheetCellPermission permission)
    {
        return toAjax(permissionService.insertPermission(permission));
    }

    /**
     * 修改單元格權限
     */
    @Log(title = "單元格權限", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LuckysheetCellPermission permission)
    {
        return toAjax(permissionService.updatePermission(permission));
    }

    /**
     * 刪除單元格權限
     */
    @Log(title = "單元格權限", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(permissionService.deletePermissionByIds(ids));
    }
}
