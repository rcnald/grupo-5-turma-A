@echo off
set DIST_DIR=dist

if not exist %DIST_DIR% mkdir %DIST_DIR%

rem Compilando todos os arquivos .java, incluindo subdiretórios
javac -cp ".;src/lib/gson-2.11.0.jar" -d %DIST_DIR% src\*.java src\lib\utils\*.java src\lib\interfaces\*.java

if %errorlevel% neq 0 (
    echo "Erro na compilação. Verifique o código e tente novamente."
    exit /b %errorlevel%
)
