@echo off
echo ========================================
echo  Git仓库初始化脚本
echo ========================================

REM 检查Git是否安装
where git >nul 2>nul
if %errorlevel% neq 0 (
    echo 错误: Git未安装!
    echo 请先安装Git: https://git-scm.com/download/win
    echo 安装完成后重新运行此脚本
    pause
    exit /b 1
)

echo 1. 初始化Git仓库...
git init

echo 2. 配置用户信息...
git config user.name "yttandrxq"
git config user.email "546787955@qq.com"

echo 3. 添加所有文件到暂存区...
git add .

echo 4. 提交初始版本...
git commit -m "初始提交: tool-zs-02项目创建"

echo 5. 添加远程仓库...
git remote add origin https://github.com/yttandrxq/tool-zs-02.git

echo 6. 重命名分支为main...
git branch -M main

echo 7. 推送到远程仓库...
echo 注意: 如果这是第一次推送，可能需要输入GitHub用户名和密码
echo 密码请使用Personal Access Token (PAT)
echo.
git push -u origin main

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo  恭喜! 项目已成功推送到GitHub!
    echo ========================================
    echo 仓库地址: https://github.com/yttandrxq/tool-zs-02
) else (
    echo.
    echo ========================================
    echo  推送失败!
    echo ========================================
    echo 可能的原因:
    echo 1. 远程仓库不存在，请先在GitHub创建
    echo 2. 认证失败，请检查用户名/密码
    echo 3. 网络连接问题
    echo.
    echo 请手动执行以下命令:
    echo git push -u origin main
)

pause