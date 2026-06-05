# tool-zs-02

一个用于获取本地IP地址的Java工具项目。

## 功能特性

- 获取本地非回环IP地址
- 支持IPv4地址
- 自动跳过未启用的网络接口

## 项目结构

```
tool-zs-02/
├── src/main/java/com/example/App.java  # 主程序
├── pom.xml                            # Maven配置文件
├── .gitignore                         # Git忽略文件
├── setup_git.bat                      # Git初始化脚本
└── run.bat                            # 运行脚本
```

## 使用方法

### 1. 编译项目
```bash
mvn clean compile
```

### 2. 运行程序
```bash
mvn exec:java -Dexec.mainClass="com.example.App"
```
或者直接运行 `run.bat`

### 3. 打包为JAR
```bash
mvn clean package
```

## 项目结构

```
tool-zs-02/
├── src/main/java/com/example/App.java      # 主程序 (获取IP地址)
├── src/test/java/com/example/AppTest.java  # 单元测试
├── pom.xml                                 # Maven配置文件
├── .gitignore                              # Git忽略规则
├── README.md                               # 项目文档 (本文件)
├── run.bat                                 # 一键运行脚本
├── setup_git.bat                           # Git配置管理脚本
├── git_commands.bat                        # Git操作菜单脚本
├── 下一步操作.txt                          # 详细操作指南
└── target/                                 # 编译输出目录 (被忽略)
```

## Git版本控制

### ✅ Git配置已完成
- **远程仓库**: `https://github.com/yttandrxq/tool-zs-02`
- **本地仓库**: `E:\svn\tool-zs-02\.git`
- **用户信息**: yttandrxq (546787955@qq.com)
- **当前分支**: main (跟踪 origin/main)
- **最后提交**: 9de0ff3 "init" (8个文件)

### Git操作脚本
1. **`git_commands.bat`** - Git操作菜单 (状态、提交、推送等)
2. **`setup_git.bat`** - Git配置管理 (重新初始化、验证配置)

### 日常Git工作流
```bash
# 查看状态
git_commands.bat status

# 添加更改并提交
git_commands.bat add
git_commands.bat commit

# 推送到GitHub
git_commands.bat push

# 拉取更新
git_commands.bat pull
```

### 验证GitHub仓库
访问: https://github.com/yttandrxq/tool-zs-02

## 依赖项

- Java 1.7+
- Maven 3.x

## 代码示例

```java
public class App {
    public static void main(String[] args) {
        String ip = App.getLocalIpAddress();
        System.out.println("本地IP地址: " + ip);
    }
}
```

## 注意事项

1. 如果有多块网卡，将返回第一个非回环IPv4地址
2. 需要网络接口处于启用状态
3. 在无网络环境将返回127.0.0.1

## 许可证

本项目遵循MIT许可证。