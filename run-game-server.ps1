param([switch]$Chapter9Test)

$ErrorActionPreference = "Stop"

. "$PSScriptRoot\dev-env.ps1"

$javaExe = "$env:JAVA_HOME\bin\java.exe"
$gameJar = "$PSScriptRoot\dpcq-game-server\target\dpcq-game-server-0.0.1-SNAPSHOT.jar"
$mysqlDir = "D:\doudi-resources\tools\mysql-8.0.46-winx64"
$mysqlExe = "$mysqlDir\bin\mysqld.exe"
$mysqlConfig = "$mysqlDir\my.ini"
$logDir = "$PSScriptRoot\dpcq-game-server\target\run-logs"
$adbExe = "D:\Android\Sdk\platform-tools\adb.exe"

function Get-ListeningProcessIds {
    param([int]$Port)

    return @(
        Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue |
            Select-Object -ExpandProperty OwningProcess -Unique
    )
}

function Wait-ForPort {
    param(
        [int]$Port,
        [int]$TimeoutSeconds,
        [System.Diagnostics.Process]$Process
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    while ((Get-Date) -lt $deadline) {
        if ((Get-ListeningProcessIds -Port $Port).Count -gt 0) {
            return $true
        }

        if ($null -ne $Process -and $Process.HasExited) {
            return $false
        }

        Start-Sleep -Milliseconds 500
    }

    return $false
}

function Assert-FileExists {
    param(
        [string]$Path,
        [string]$Message
    )

    if (-not (Test-Path -LiteralPath $Path -PathType Leaf)) {
        throw "$Message`nMissing file: $Path"
    }
}

function Set-AdbReversePorts {
    if (-not (Test-Path -LiteralPath $adbExe -PathType Leaf)) {
        Write-Warning "ADB was not found. Android reverse ports were not configured."
        return
    }

    $previousErrorActionPreference = $ErrorActionPreference
    try {
        # adb 首次拉起 daemon 时会把正常提示写入 stderr；不要让它触发 Stop。
        $ErrorActionPreference = "SilentlyContinue"
        $deviceOutput = @(& $adbExe devices 2>$null)
        $devicesExitCode = $LASTEXITCODE
    }
    finally {
        $ErrorActionPreference = $previousErrorActionPreference
    }

    if ($devicesExitCode -ne 0) {
        Write-Warning "ADB devices failed. Android reverse ports were not configured."
        return
    }

    $devices = @(
        $deviceOutput | ForEach-Object {
            if ($_ -match '^(\S+)\s+device(?:\s|$)') {
                $Matches[1]
            }
        }
    )

    if ($devices.Count -eq 0) {
        Write-Warning "No online Android device was found. Start the emulator, then run this script again."
        return
    }

    foreach ($device in $devices) {
        & $adbExe -s $device reverse tcp:18080 tcp:18080 | Out-Null
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to configure ADB reverse for port 18080 on $device."
        }

        & $adbExe -s $device reverse tcp:19090 tcp:19090 | Out-Null
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to configure ADB reverse for port 19090 on $device."
        }

        Write-Host "ADB reverse configured for ${device}: 18080, 19090" -ForegroundColor Green
    }
}

Assert-FileExists -Path $javaExe -Message "Java 8 was not found."
Assert-FileExists -Path $gameJar -Message "Game server JAR was not found. Run build.ps1 first."
Assert-FileExists -Path $mysqlExe -Message "MySQL was not found."
Assert-FileExists -Path $mysqlConfig -Message "MySQL configuration was not found."

$mysqlPids = @(Get-ListeningProcessIds -Port 3307)
if ($mysqlPids.Count -eq 0) {
    Write-Host "Starting MySQL on port 3307..." -ForegroundColor Yellow
    $mysqlProcess = Start-Process `
        -FilePath $mysqlExe `
        -ArgumentList "--defaults-file=$mysqlConfig", "--console" `
        -WorkingDirectory $mysqlDir `
        -WindowStyle Hidden `
        -PassThru

    if (-not (Wait-ForPort -Port 3307 -TimeoutSeconds 30 -Process $mysqlProcess)) {
        throw "MySQL failed to listen on 127.0.0.1:3307 within 30 seconds."
    }

    $mysqlPids = @(Get-ListeningProcessIds -Port 3307)
    Write-Host "MySQL started. PID: $($mysqlPids -join ', ')" -ForegroundColor Green
}
else {
    $unexpectedMysql = @(
        $mysqlPids | Where-Object {
            (Get-Process -Id $_ -ErrorAction SilentlyContinue).ProcessName -ne "mysqld"
        }
    )
    if ($unexpectedMysql.Count -gt 0) {
        throw "Port 3307 is occupied by a non-MySQL process. PID: $($unexpectedMysql -join ', ')."
    }

    Write-Host "MySQL is already running. PID: $($mysqlPids -join ', ')" -ForegroundColor Green
}

Set-AdbReversePorts

$httpPids = @(Get-ListeningProcessIds -Port 18080)
$webSocketPids = @(Get-ListeningProcessIds -Port 19090)
if ($httpPids.Count -gt 0 -or $webSocketPids.Count -gt 0) {
    $sharedPids = @($httpPids | Where-Object { $webSocketPids -contains $_ })
    if ($sharedPids.Count -ne 1 -or $httpPids.Count -ne 1 -or $webSocketPids.Count -ne 1) {
        throw "Game port state is inconsistent: 18080 PID=[$($httpPids -join ', ')], 19090 PID=[$($webSocketPids -join ', ')]."
    }

    $existingGamePid = $sharedPids[0]
    $existingProcess = Get-CimInstance Win32_Process -Filter "ProcessId = $existingGamePid"
    if ($null -eq $existingProcess -or $existingProcess.Name -notlike "java*" -or $existingProcess.CommandLine -notlike "*$([IO.Path]::GetFileName($gameJar))*") {
        throw "Ports 18080/19090 are occupied by another process. PID: $existingGamePid."
    }

    if ($Chapter9Test -and $existingProcess.CommandLine -notlike '*--game.test.chapter9-start=true*') {
        throw "The running server is not in Chapter 9 test mode. Stop that game server first, then rerun with -Chapter9Test."
    }
    Write-Host "Game server is already running. PID: $existingGamePid (no duplicate started)" -ForegroundColor Green
    if ($existingProcess.CommandLine -like '*--game.test.chapter9-start=true*') {
        Write-Warning "Chapter 9 TEST mode is active. New player initialization starts at chapter 9."
    }
    Write-Host "HTTP: 127.0.0.1:18080"
    Write-Host "WebSocket: 127.0.0.1:19090"
    exit 0
}

New-Item -ItemType Directory -Path $logDir -Force | Out-Null
$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$outputLog = Join-Path $logDir "server-$timestamp.out.log"
$errorLog = Join-Path $logDir "server-$timestamp.err.log"

Write-Host "Starting game server on ports 18080/19090..." -ForegroundColor Yellow
# 由 Windows WMI 创建独立进程，避免随调用终端/工具的进程树一起退出。
# cmd 仅负责日志重定向；不安装计划任务或后台服务。
$startupInfo = New-CimInstance -ClassName Win32_ProcessStartup -ClientOnly -Property @{ ShowWindow = [uint16]0 }
$testArgument = ''
if ($Chapter9Test) {
    $testArgument = ' --game.test.chapter9-start=true'
    Write-Warning "Chapter 9 TEST mode: each player creation resets to the captured chapter 9 checkpoint."
}
$launchCommand = '"{0}" /d /s /c ""{1}" -jar "{2}"{5} 1>"{3}" 2>"{4}""' -f $env:ComSpec, $javaExe, $gameJar, $outputLog, $errorLog, $testArgument
$launchResult = Invoke-CimMethod -ClassName Win32_Process -MethodName Create -Arguments @{
    CommandLine = $launchCommand
    CurrentDirectory = $PSScriptRoot
    ProcessStartupInformation = $startupInfo
}
if ($launchResult.ReturnValue -ne 0) {
    throw "Unable to start detached game server. Windows error: $($launchResult.ReturnValue)"
}
$gameProcess = Get-Process -Id $launchResult.ProcessId -ErrorAction SilentlyContinue

$httpReady = Wait-ForPort -Port 18080 -TimeoutSeconds 45 -Process $gameProcess
$webSocketReady = $false
if ($httpReady) {
    $webSocketReady = Wait-ForPort -Port 19090 -TimeoutSeconds 15 -Process $gameProcess
}

if (-not $httpReady -or -not $webSocketReady) {
    Write-Host "Game server startup failed. Recent output:" -ForegroundColor Red
    if (Test-Path -LiteralPath $outputLog) {
        Get-Content -LiteralPath $outputLog -Tail 30
    }
    if (Test-Path -LiteralPath $errorLog) {
        Get-Content -LiteralPath $errorLog -Tail 30
    }
    throw "Game server failed to listen on 18080/19090. Full log: $outputLog"
}

$gamePids = @(Get-ListeningProcessIds -Port 18080)
Write-Host "Game server started. PID: $($gamePids -join ', ')" -ForegroundColor Green
Write-Host "HTTP: 127.0.0.1:18080"
Write-Host "WebSocket: 127.0.0.1:19090"
Write-Host "Output log: $outputLog"
Write-Host "Error log: $errorLog"
