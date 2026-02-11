package com.ruoyi.luckysheet.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.luckysheet.mapper.LuckysheetCellPermissionMapper;
import com.ruoyi.luckysheet.domain.LuckysheetCellPermission;
import com.ruoyi.luckysheet.service.ILuckysheetCellPermissionService;

/**
 * Luckysheet單元格權限Service業務層處理
 * 
 * @author ruoyi
 */
@Service
public class LuckysheetCellPermissionServiceImpl implements ILuckysheetCellPermissionService
{
    @Autowired
    private LuckysheetCellPermissionMapper permissionMapper;

    /**
     * 查詢單元格權限列表
     * 
     * @param permission 單元格權限
     * @return 單元格權限
     */
    @Override
    public List<LuckysheetCellPermission> selectPermissionList(LuckysheetCellPermission permission)
    {
        return permissionMapper.selectPermissionList(permission);
    }

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
    @Override
    public String checkCellPermission(Long workbookId, String sheetIndex, Integer rowIndex, Integer colIndex, Long userId, Long deptId)
    {
        String permission = permissionMapper.selectCellPermission(workbookId, sheetIndex, rowIndex, colIndex, userId, deptId);
        // 如果沒有設置權限，默認可編輯
        return permission != null ? permission : "2";
    }

    /**
     * 查詢工作表的所有單元格權限
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param userId 用戶ID
     * @param deptId 部門ID
     * @return 權限集合
     */
    @Override
    public List<LuckysheetCellPermission> selectSheetPermissions(Long workbookId, String sheetIndex, Long userId, Long deptId)
    {
        return permissionMapper.selectSheetPermissions(workbookId, sheetIndex, userId, deptId);
    }

    /**
     * 新增單元格權限
     * 
     * @param permission 單元格權限
     * @return 結果
     */
    @Override
    public int insertPermission(LuckysheetCellPermission permission)
    {
        return permissionMapper.insertPermission(permission);
    }

    /**
     * 修改單元格權限
     * 
     * @param permission 單元格權限
     * @return 結果
     */
    @Override
    public int updatePermission(LuckysheetCellPermission permission)
    {
        return permissionMapper.updatePermission(permission);
    }

    /**
     * 批量刪除單元格權限
     * 
     * @param ids 需要刪除的單元格權限主鍵
     * @return 結果
     */
    @Override
    public int deletePermissionByIds(Long[] ids)
    {
        return permissionMapper.deletePermissionByIds(ids);
    }

    /**
     * 刪除單元格權限信息
     * 
     * @param id 單元格權限主鍵
     * @return 結果
     */
    @Override
    public int deletePermissionById(Long id)
    {
        return permissionMapper.deletePermissionById(id);
    }
}
