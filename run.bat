@echo off
echo ========================================
echo  运行 tool-zs-02 项目
echo ========================================

REM 检查Java是否安装
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo 错误: Java未安装或未添加到PATH!
    echo 请先安装Java: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM 检查Maven是否安装
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo 错误: Maven未安装或未添加到PATH!
    echo 请先安装Maven: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo 正在编译项目...
echo.

REM 编译项目
mvn clean compile -q

if %errorlevel% neq 0 (
    echo 编译失败!
    pause
    exit /b 1
)

echo 编译成功!
echo 正在运行程序...
echo.

REM 运行主程序 (使用Java命令)
java -cp target/classes com.example.App

echo.
echo ========================================
echo  程序执行完毕
echo ========================================
pause