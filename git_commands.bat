@echo off
chcp 65001 >nul
echo ========================================
echo  tool-zs-02 Git操作菜单
echo ========================================
echo.

REM 设置Git路径
set GIT_PATH=E:\program files\Git\bin\git.exe

if "%1"=="" goto menu

REM 处理命令行参数
if "%1"=="status" goto status
if "%1"=="log" goto log
if "%1"=="add" goto add
if "%1"=="commit" goto commit
if "%1"=="push" goto push
if "%1"=="pull" goto pull
if "%1"=="remote" goto remote
if "%1"=="branch" goto branch
if "%1"=="sync-status" goto sync-status
if "%1"=="init-check" goto init-check
goto unknown

:menu
echo 请选择操作:
echo.
echo  1. 查看状态 (status)
echo  2. 查看提交历史 (log)
echo  3. 添加所有更改 (add .)
echo  4. 提交更改 (commit)
echo  5. 推送更改 (push)
echo  6. 拉取更新 (pull)
echo  7. 查看远程仓库 (remote -v)
echo  8. 查看分支 (branch -a)
echo  9. 同步状态检查 (sync-status)
echo 10. Git配置检查 (init-check)
echo  0. 退出
echo.
set /p choice=请输入选项编号: 

if "%choice%"=="1" goto status
if "%choice%"=="2" goto log
if "%choice%"=="3" goto add
if "%choice%"=="4" goto commit
if "%choice%"=="5" goto push
if "%choice%"=="6" goto pull
if "%choice%"=="7" goto remote
if "%choice%"=="8" goto branch
if "%choice%"=="9" goto sync-status
if "%choice%"=="10" goto init-check
if "%choice%"=="0" goto exit
echo 无效选项
goto menu

:status
echo [查看Git状态]
"%GIT_PATH%" status
goto end

:log
echo [查看提交历史]
"%GIT_PATH%" log --oneline --graph --all
goto end

:add
echo [添加所有更改到暂存区]
"%GIT_PATH%" add .
goto end

:commit
echo [提交更改]
set /p msg=请输入提交描述: 
if "%msg%"=="" (
    echo 错误: 提交描述不能为空
    goto commit
)
"%GIT_PATH%" commit -m "%msg%"
goto end

:push
echo [推送更改到GitHub]
"%GIT_PATH%" push
goto end

:pull
echo [拉取远程更新]
"%GIT_PATH%" pull
goto end

:remote
echo [查看远程仓库]
"%GIT_PATH%" remote -v
goto end

:branch
echo [查看分支]
"%GIT_PATH%" branch -a
goto end

:sync-status
echo [同步状态检查]
echo 本地分支状态:
"%GIT_PATH%" status -sb
echo.
echo 远程分支状态:
"%GIT_PATH%" remote show origin
goto end

:init-check
echo [Git配置检查]
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
"%GIT_PATH%" log -1 --pretty=format:"%h - %an, %ar : %s"
goto end

:unknown
echo 未知命令: %1
echo 可用命令: status, log, add, commit, push, pull, remote, branch, sync-status, init-check
goto end

:exit
echo 退出Git操作菜单
goto :eof

:end
echo.
pause
goto menu