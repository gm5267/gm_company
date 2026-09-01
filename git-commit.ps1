<#
.SYNOPSIS
    以 UTF-8(无 BOM) 方式提交，避免中文备注在 GBK 控制台的 PowerShell 下出现乱码。
.DESCRIPTION
    在代码页为 GBK(936) 的控制台里，直接 `git commit -m "中文"` 会把中文按 GBK 编码传给 git，
    而 git/GitHub 按 UTF-8 显示，于是变成 "鍒濆鍖?" 之类的乱码。
    本脚本把备注写成 UTF-8 无 BOM 的临时文件，再用 `git commit -F` 提交，彻底规避该问题。
    关键：中文备注请通过 -File（UTF-8 文件）传入，不要通过 -Message 写在命令行上。
.PARAMETER Message
    提交备注。仅建议用于纯 ASCII；含中文时请用 -File，以免命令行编码问题。
.PARAMETER File
    UTF-8(无 BOM 亦可) 备注文件路径。含中文时最可靠。
.EXAMPLE
    .\git-commit.ps1 -File msg.txt
    .\git-commit.ps1 -Message "fix: correct typo"
#>
param(
    [string]$Message,
    [string]$File
)

if ($File) {
    if (-not (Test-Path $File)) { Write-Error "文件不存在: $File"; exit 1 }
    $msg = [System.IO.File]::ReadAllText($File, [System.Text.Encoding]::UTF8).Trim()
}
elseif ($Message) {
    $msg = $Message
}
else {
    Write-Error "请通过 -Message 或 -File 提供提交备注。"; exit 1
}

$tmp = Join-Path $env:TEMP ("git-cmt-" + [guid]::NewGuid().ToString("N") + ".txt")
try {
    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($tmp, $msg, $utf8NoBom)
    git commit -F $tmp @args
}
finally {
    if (Test-Path $tmp) { Remove-Item $tmp -Force }
}
