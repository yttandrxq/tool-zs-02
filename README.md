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

## 项目配置

### Git仓库设置
远程仓库地址: `https://github.com/yttandrxq/tool-zs-02`

### 初始化Git仓库
1. 确保已安装Git
2. 运行 `setup_git.bat` 脚本
3. 按照提示操作

如果 `setup_git.bat` 失败，请手动执行以下命令:

```bash
# 初始化Git仓库
git init

# 配置用户信息
git config user.name "yttandrxq"
git config user.email "546787955@qq.com"

# 添加文件并提交
git add .
git commit -m "初始提交"

# 添加远程仓库
git remote add origin https://github.com/yttandrxq/tool-zs-02.git

# 推送代码
git branch -M main
git push -u origin main
```

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