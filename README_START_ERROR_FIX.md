# ⚡ 立即解決啟動錯誤

## 如果您看到這個錯誤：

```
NullPointerException: Cannot invoke "PropertyEntity.getValue()" because "dbVersionProperty" is null
```

---

## 🎯 最快解決方案（30秒）

### 步驟 1：編輯配置文件

打開文件：`ruoyi-admin/src/main/resources/application.yml`

### 步驟 2：找到 Flowable 配置

找到這一段（大約在第 139 行）：

```yaml
# Flowable工作流配置
flowable:
  # 是否启用Flowable（如果遇到数据库初始化问题，可以临时设置为false）
  # enabled: false
```

### 步驟 3：取消註釋

將 `# enabled: false` 改為 `enabled: false`（去掉 `#` 號）：

```yaml
# Flowable工作流配置
flowable:
  # 是否启用Flowable（如果遇到数据库初始化问题，可以临时设置为false）
  enabled: false  # ← 取消這一行的註釋
```

### 步驟 4：重啟應用

```bash
mvn spring-boot:run
```

✅ **完成！** 應用現在應該可以正常啟動了。

---

## 🔄 需要 Flowable 功能時

### 步驟 1：創建數據庫

```bash
mysql -u root -p < sql/init_flowable_database.sql
```

或者手動：

```sql
CREATE DATABASE IF NOT EXISTS `ry-vue` DEFAULT CHARACTER SET utf8mb4;
```

### 步驟 2：重新啟用 Flowable

在 `application.yml` 中：

```yaml
flowable:
  enabled: true  # 或者刪除這一行
```

### 步驟 3：重啟應用

Flowable 會自動創建所有需要的表（約 70 個）。

---

## 📚 詳細文檔

- **快速入門**：`FLOWABLE_QUICK_START.md`
- **完整指南**：`問題解決總結.md`
- **故障排除**：`FLOWABLE_TROUBLESHOOTING_INDEX.md`

---

## ❓ 常見問題

**Q: 禁用 Flowable 會影響其他功能嗎？**  
A: 不會。只有工作流功能不可用，其他所有模塊正常運行。

**Q: 可以隨時重新啟用嗎？**  
A: 可以。創建數據庫後，修改配置並重啟即可。

**Q: 為什麼會出現這個錯誤？**  
A: Flowable 需要在數據庫中創建表，但數據庫尚未初始化。臨時禁用是最快的解決方案。

---

## 🎉 就是這麼簡單！

1. 取消註釋 `enabled: false`
2. 重啟應用
3. 完成

需要工作流時，創建數據庫再啟用即可。
