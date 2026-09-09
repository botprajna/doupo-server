. "$PSScriptRoot\dev-env.ps1"

Set-Location $PSScriptRoot
mvn clean install

if ($LASTEXITCODE -ne 0) {
    throw "Maven 构建失败"
}

Write-Host "项目构建成功" -ForegroundColor Green
