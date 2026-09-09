@echo off
title Doudi Game Server - One Click Start

powershell.exe -NoLogo -NoProfile -ExecutionPolicy Bypass -File "%~dp0run-game-server.ps1"
if errorlevel 1 (
    echo.
    echo Server startup failed. Check the error above.
    pause
    exit /b 1
)

echo.
echo Server startup completed. You may close this window.
pause
