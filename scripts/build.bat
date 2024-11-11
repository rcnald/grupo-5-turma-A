@echo off
set DIST_DIR=dist

if not exist %DIST_DIR% mkdir %DIST_DIR%

javac -cp ".;lib/gson-2.11.0.jar" -d %DIST_DIR% src\*.java

if %errorlevel% neq 0 (
    echo "Erro na compilação. Verifique o código e tente novamente."
    exit /b %errorlevel%
)