package com.ruoyi.luckysheet.service;

import java.util.List;
import com.ruoyi.luckysheet.domain.LuckysheetCellPermission;

/**
 * Luckysheet單元格權限Service接口
 * 
 * @author ruoyi
 */
public interface ILuckysheetCellPermissionService
{
    /**
     * 查詢單元格權限列表
     * 
     * @param permission 單元格權限
     * @return 單元格權限集合
     */
    public List<LuckysheetCellPermission> selectPermissionList(LuckysheetCellPermission permission);

    /**
     * 檢查用戶對指定單元格的權限
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @param userId 用戶ID
     * @param deptId 部門ID
     * @return 權限類型（0禁止 1只讀 2可編輯）
     */
    public String checkCellPermission(Long workbookId, String sheetIndex, Integer rowIndex, Integer colIndex, Long userId, Long deptId);

    /**
     * 查詢工作表的所有單元格權限
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param userId 用戶ID
     * @param deptId 部門ID
     * @return 權限集合
     */
    public List<LuckysheetCellPermission> selectSheetPermissions(Long workbookId, String sheetIndex, Long userId, Long deptId);

    /**
     * 新增單元格權限
     * 
     * @param permission 單元格權限
     * @return 結果
     */
    public int insertPermission(LuckysheetCellPermission permission);

    /**
     * 修改單元格權限
     * 
     * @param permission 單元格權限
     * @return 結果
     */
    public int updatePermission(LuckysheetCellPermission permission);

    /**
     * 批量刪除單元格權限
     * 
     * @param ids 需要刪除的單元格權限主鍵集合
     * @return 結果
     */
    public int deletePermissionByIds(Long[] ids);

    /**
     * 刪除單元格權限信息
     * 
     * @param id 單元格權限主鍵
     * @return 結果
     */
    public int deletePermissionById(Long id);
}
