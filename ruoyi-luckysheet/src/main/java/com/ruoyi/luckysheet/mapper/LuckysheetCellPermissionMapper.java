package com.ruoyi.luckysheet.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.luckysheet.domain.LuckysheetCellPermission;

/**
 * Luckysheet單元格權限Mapper接口
 * 
 * @author ruoyi
 */
public interface LuckysheetCellPermissionMapper
{
    /**
     * 查詢單元格權限列表
     * 
     * @param permission 單元格權限
     * @return 單元格權限集合
     */
    public List<LuckysheetCellPermission> selectPermissionList(LuckysheetCellPermission permission);

    /**
     * 查詢用戶對指定單元格的權限
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @param userId 用戶ID
     * @param deptId 部門ID
     * @return 權限類型
     */
    public String selectCellPermission(@Param("workbookId") Long workbookId,
                                      @Param("sheetIndex") String sheetIndex,
                                      @Param("rowIndex") Integer rowIndex,
                                      @Param("colIndex") Integer colIndex,
                                      @Param("userId") Long userId,
                                      @Param("deptId") Long deptId);

    /**
     * 查詢工作表的所有單元格權限（用於前端渲染）
     * 
     * @param workbookId 工作簿ID
     * @param sheetIndex 工作表索引
     * @param userId 用戶ID
     * @param deptId 部門ID
     * @return 權限集合
     */
    public List<LuckysheetCellPermission> selectSheetPermissions(@Param("workbookId") Long workbookId,
                                                                @Param("sheetIndex") String sheetIndex,
                                                                @Param("userId") Long userId,
                                                                @Param("deptId") Long deptId);

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
     * 刪除單元格權限
     * 
     * @param id 單元格權限主鍵
     * @return 結果
     */
    public int deletePermissionById(Long id);

    /**
     * 批量刪除單元格權限
     * 
     * @param ids 需要刪除的數據主鍵集合
     * @return 結果
     */
    public int deletePermissionByIds(Long[] ids);

    /**
     * 刪除工作簿的所有單元格權限
     * 
     * @param workbookId 工作簿ID
     * @return 結果
     */
    public int deletePermissionByWorkbookId(Long workbookId);
}
