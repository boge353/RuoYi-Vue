# Flowable 快速启动指南

## 問題：應用啟動失敗

如果您看到這個錯誤：
```
NullPointerException: Cannot invoke "PropertyEntity.getValue()" because "dbVersionProperty" is null
```

**原因**：Flowable工作流表還沒有在數據庫中創建。

---

## ⚡ 最快解決方案（三選一）

### 方案 A：臨時禁用 Flowable（最快，讓應用先跑起來）

在 `ruoyi-admin/src/main/resources/application.yml` 中添加：

```yaml
flowable:
  enabled: false  # 臨時禁用Flowable
```

**優點**：
- ✅ 應用立即可以啟動
- ✅ 不需要操作數據庫
- ✅ 其他功能正常使用
- ✅ 需要時再啟用

**步驟**：
1. 編輯 `application.yml`
2. 添加上述配置
3. 重啟應用
4. ✅ 完成！

---

### 方案 B：創建數據庫（推薦用於生產）

**一行命令搞定**：
```bash
mysql -u root -p < sql/init_flowable_database.sql
```

然後啟動應用，Flowable會自動創建所有表（約70個）。

**優點**：
- ✅ 完整Flowable功能
- ✅ 自動創建表
- ✅ 適合生產環境

---

### 方案 C：手動創建數據庫

如果您的數據庫不存在：

```sql
CREATE DATABASE IF NOT EXISTS `ry-vue` DEFAULT CHARACTER SET utf8mb4;
```

然後啟動應用，Flowable會自動創建所有表。

---

## 📊 驗證是否成功

### 如果禁用了Flowable：
應用啟動成功，沒有Flowable相關錯誤。

### 如果啟用了Flowable：

**檢查表數量**：
```sql
SELECT COUNT(*) FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'ry-vue' 
AND (TABLE_NAME LIKE 'ACT_%' OR TABLE_NAME LIKE 'FLW_%');
```
應該返回約 70 個表。

**檢查版本**：
```sql
SELECT * FROM ACT_GE_PROPERTY WHERE NAME_ = 'schema.version';
```
應該顯示：6.8.1.0

---

## 🔄 重新啟用 Flowable

如果您之前禁用了Flowable，想要啟用：

1. **確保數據庫已創建**：
   ```bash
   mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS \`ry-vue\` DEFAULT CHARACTER SET utf8mb4;"
   ```

2. **修改配置**：
   ```yaml
   flowable:
     enabled: true  # 或者刪除這一行（默認為true）
   ```

3. **重啟應用**
   - Flowable會自動創建所有需要的表
   - 查看日誌確認初始化成功

---

## 📚 更多文檔

- **中文總結**：`問題解決總結.md`
- **NPE專項修復**：`FLOWABLE_NPE_FIX.md`
- **完整故障排除**：`FLOWABLE_TROUBLESHOOTING_INDEX.md`
- **快速修復**：`QUICK_FIX.md`

---

## ⚠️ 重要提示

### JDK 版本
本項目使用 **JDK 21**，請確保：
```bash
java -version
# 應該顯示：java version "21.x.x"
```

### 數據庫連接
確保 `application-druid.yml` 中的數據庫連接配置正確：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ry-vue?useSSL=false...
    username: root
    password: your_password
```

---

## 🎯 推薦流程

**首次使用**：
1. 使用方案 A 禁用 Flowable
2. 確保應用正常啟動
3. 創建數據庫
4. 重新啟用 Flowable

**後續使用**：
- Flowable 保持啟用狀態
- 表已存在，直接使用

---

## ❓ 常見問題

**Q: 禁用 Flowable 會影響其他功能嗎？**
A: 不會。只是工作流功能不可用，其他模塊完全正常。

**Q: 如何確認 Flowable 是否已禁用？**
A: 查看啟動日誌，如果沒有 "正在初始化Flowable工作流引擎" 的消息，說明已禁用。

**Q: 數據庫名稱必須是 ry-vue 嗎？**
A: 不是，但要與 application-druid.yml 中配置的名稱一致。

**Q: 可以稍後再啟用 Flowable 嗎？**
A: 可以。隨時修改配置並重啟即可。

---

## 📞 獲取幫助

如果仍有問題，請查看：
1. 控制台完整錯誤日誌
2. 數據庫連接是否正常
3. 數據庫名稱是否正確
4. JDK 版本是否為 21

參考文檔中的 "FLOWABLE_TROUBLESHOOTING_INDEX.md" 獲取更多幫助。
