@echo off
echo ========================================
echo  运行 tool-zs-02 项目
echo ========================================

REM 优先使用内置 JRE，其次使用系统 Java
set "APP_HOME=%~dp0"
set "JAVA_CMD="

if exist "%APP_HOME%jre\bin\java.exe" (
    echo [INFO] 使用内置 JRE 编译...
    set "JAVA_CMD=%APP_HOME%jre\bin\java.exe"
) else (
    where java >nul 2>nul
    if %errorlevel% neq 0 (
        echo 错误: Java未安装或未添加到PATH!
        echo 请先安装Java: https://www.oracle.com/java/technologies/downloads/
        pause
        exit /b 1
    )
    set "JAVA_CMD=java"
)

echo 正在编译项目...
echo.

REM 编译项目
mvn clean package -q -DskipTests

if %errorlevel% neq 0 (
    echo 编译失败!
    pause
    exit /b 1
)

echo 编译成功!
echo 正在运行程序...
echo.

REM 运行打包后的 jar
"%JAVA_CMD%" -jar "%APP_HOME%target\城市天气.jar"

echo.
echo ========================================
echo  程序执行完毕
echo ========================================
pause