# gm_company

公司管理类项目（后端骨架工程 + 需求/设计/缺陷文档）。

## 目录结构

| 目录 | 说明 |
| --- | --- |
| `01需求文档/` | 需求文档 |
| `02需求设计文档/` | 需求设计文档 |
| `03缺陷文档/` | 缺陷文档 |
| `04后端工程/` | 后端工程（ace-backend，Spring Boot） |
| `05前端工程/` | 前端工程（暂未初始化） |

## 提交备注约定（重要）

在 **GBK 控制台（代码页 936）** 的 PowerShell 下，直接执行：

```powershell
git commit -m "初始化项目"
```

中文参数会被按 **GBK** 编码传给 git，而 git/GitHub 按 **UTF-8** 显示，
最终出现类似 `鍒濆鍖?` 的乱码。

**正确做法**：把备注写入一个 UTF-8（无 BOM）文件，再用 `git commit -F` 提交，
让中文完全不出现在命令行上。本仓库已提供辅助脚本 `git-commit.ps1`：

```powershell
# 含中文：写到文件最可靠
.\git-commit.ps1 -File msg.txt

# 纯 ASCII：可直接用 -Message
.\git-commit.ps1 -Message "fix: correct typo"
```

> 说明：在默认 UTF-8 的终端（如 Windows Terminal）里，`git commit -m "中文"`
> 通常正常；但走文件方式在所有环境下都稳定，建议统一使用。

## 备注规范

建议采用 Conventional Commits 风格，例如：

- `feat:` 新功能
- `fix:` 缺陷修复
- `docs:` 文档
- `chore:` 构建/工具/杂项
