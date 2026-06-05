@echo off
chcp 65001 >nul
echo ========================================
echo  tool-zs-02 Git配置管理脚本
echo ========================================
echo.

REM 设置Git路径
set GIT_PATH=E:\program files\Git\bin\git.exe

REM 检查Git是否可访问
if not exist "%GIT_PATH%" (
    echo 错误: 未找到Git程序!
    echo Git路径: %GIT_PATH%
    echo 请确认Git已安装在指定位置
    echo 或修改脚本中的GIT_PATH变量
    pause
    exit /b 1
)

echo Git版本: 
"%GIT_PATH%" --version
echo.

REM 检查是否已初始化Git仓库
if exist ".git" (
    echo ✅ Git仓库已初始化
    echo.
    echo 当前Git配置:
    echo ----------------------------------------
    echo 用户信息:
    "%GIT_PATH%" config --get user.name
    "%GIT_PATH%" config --get user.email
    echo.
    echo 远程仓库:
    "%GIT_PATH%" remote -v
    echo.
    echo 当前分支:
    "%GIT_PATH%" branch --show-current
    echo.
    echo 最后提交:
    "%GIT_PATH%" log -1 --pretty=format:"%%h - %%an, %%ar : %%s"
    echo.
    echo 状态:
    "%GIT_PATH%" status -sb
    
    echo.
    echo 选择操作:
    echo  1. 重新初始化Git仓库 (清除现有配置)
    echo  2. 推送到远程仓库 (git push)
    echo  3. 拉取远程更新 (git pull)
    echo  4. 查看详细状态 (git status)
    echo  5. 退出
    echo.
    set /p choice=请选择: 
    
    if "%choice%"=="1" goto reinit
    if "%choice%"=="2" goto push
    if "%choice%"=="3" goto pull
    if "%choice%"=="4" goto status
    if "%choice%"=="5" goto exit
    echo 无效选择
    pause
    goto :eof
    
    :reinit
    echo 警告: 这将删除现有的Git配置!
    set /p confirm=确认重新初始化? (y/n): 
    if not "%confirm%"=="y" (
        echo 操作已取消
        pause
        goto :eof
    )
    echo 删除现有Git配置...
    rmdir /s /q .git
    echo 开始重新初始化...
    goto init
) else (
    echo ⚠️ Git仓库未初始化
    echo.
    set /p choice=是否初始化Git仓库? (y/n): 
    if "%choice%"=="y" goto init
    echo 操作已取消
    pause
    exit /b 0
)

:init
echo.
echo ========================================
echo  初始化Git仓库
echo ========================================
echo 1. 初始化Git仓库...
"%GIT_PATH%" init

echo 2. 配置用户信息...
"%GIT_PATH%" config user.name "yttandrxq"
"%GIT_PATH%" config user.email "546787955@qq.com"

echo 3. 添加所有文件到暂存区...
"%GIT_PATH%" add .

echo 4. 提交初始版本...
"%GIT_PATH%" commit -m "初始提交: tool-zs-02项目创建"

echo 5. 添加远程仓库...
"%GIT_PATH%" remote add origin https://github.com/yttandrxq/tool-zs-02.git

echo 6. 重命名分支为main...
"%GIT_PATH%" branch -M main

echo 7. 推送到远程仓库...
echo 注意: 如果这是第一次推送，可能需要输入GitHub用户名和密码
echo 密码请使用Personal Access Token (PAT)
echo.
"%GIT_PATH%" push -u origin main

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo  ✅ 恭喜! 项目已成功推送到GitHub!
    echo ========================================
    echo 仓库地址: https://github.com/yttandrxq/tool-zs-02
) else (
    echo.
    echo ========================================
    echo  ❌ 推送失败!
    echo ========================================
    echo 可能的原因:
    echo 1. 远程仓库不存在，请先在GitHub创建
    echo 2. 认证失败，请检查用户名/密码
    echo 3. 网络连接问题
    echo.
    echo 请手动执行以下命令:
    echo "%GIT_PATH%" push -u origin main
)
goto exit

:push
echo [推送更改到GitHub]
"%GIT_PATH%" push
goto exit

:pull
echo [拉取远程更新]
"%GIT_PATH%" pull
goto exit

:status
echo [详细状态]
"%GIT_PATH%" status
goto exit

:exit
echo.
pause