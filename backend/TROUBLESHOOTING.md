# 问题排查指南

## 依赖项问题解决

### ✅ 问题已修复

您报告的以下问题已经在最新版本中得到修复：

1. **`找不到依赖项 'org.sqlite:sqlite-jdbc:'`** ✅ 已修复
   - 现在使用: `org.xerial:sqlite-jdbc:3.44.1.0`

2. **`找不到依赖项 'org.apache.commons:commons-io:2.11.0'`** ✅ 已修复
   - 现在使用: `commons-io:commons-io:2.11.0`

## 环境配置问题

### JAVA_HOME 未正确定义

**症状**:
```
The JAVA_HOME environment variable is not defined correctly
This environment variable is needed to run this program
```

**解决方案**:

#### Windows:
1. 确认 Java 17+ 已安装
2. 设置环境变量:
   ```cmd
   # 临时设置（当前会话有效）
   set JAVA_HOME=C:\Program Files\Java\jdk-17
   set PATH=%JAVA_HOME%\bin;%PATH%

   # 验证
   java -version
   mvn -version
   ```

3. 永久设置:
   - Windows + R → 输入 `sysdm.cpl`
   - 高级 → 环境变量
   - 新建系统变量: `JAVA_HOME` = `C:\Program Files\Java\jdk-17`
   - 编辑 PATH，添加: `%JAVA_HOME%\bin`

#### Linux/macOS:
```bash
# 查找 Java 安装路径
which java
/usr/libexec/java_home  # macOS only

# 设置环境变量（添加到 ~/.bashrc 或 ~/.zshrc）
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64  # Linux
export JAVA_HOME=$(/usr/libexec/java_home -v 17)     # macOS
export PATH=$JAVA_HOME/bin:$PATH

# 重新加载
source ~/.bashrc
```

## 常见启动问题

### 1. 端口被占用

**错误信息**:
```
Port 8080 was already in use
```

**解决方案**:
- 停止占用端口的进程，或
- 修改 `application.yml` 中的端口:
  ```yaml
  server:
    port: 8081
  ```

### 2. 依赖下载失败

**症状**: Maven 无法下载依赖

**解决方案**:
```bash
# 清理并重新下载
mvn clean
mvn dependency:resolve
mvn compile
```

### 3. 编译错误

**症状**: 编译时出现语法错误

**解决方案**:
1. 确认 Java 版本 17+
2. 清理重新编译:
   ```bash
   mvn clean compile
   ```

### 4. 数据库连接问题

**症状**: SQLite 相关错误

**解决方案**:
1. 确认 `data` 目录存在:
   ```bash
   mkdir -p data
   ```
2. 检查数据库路径权限

## 验证环境

运行以下命令验证环境配置：

```bash
# 检查 Java
java -version
# 应显示: openjdk version "17.x.x" 或类似

# 检查 Maven
mvn -version
# 应显示: Apache Maven 3.6.x 或更高

# 检查项目依赖
cd backend
mvn dependency:tree
# 应成功显示依赖树

# 编译测试
mvn clean compile
# 应显示: BUILD SUCCESS
```

## 开发工具配置

### IntelliJ IDEA
1. File → Open → 选择 `backend` 文件夹
2. 确保 Project Structure → Project Settings → Project → Project SDK 设置为 Java 17+
3. Settings → Build, Execution, Deployment → Build Tools → Maven
   - Maven home path: 指向您的 Maven 安装路径
   - User settings file: 通常为 `~/.m2/settings.xml`

### VS Code
1. 安装 Extension Pack for Java
2. 打开 `backend` 文件夹
3. Ctrl+Shift+P → "Java: Open Java Language Server Log"
4. 确认没有错误信息

### Eclipse
1. File → Import → Existing Maven Projects
2. 选择 `backend` 文件夹
3. 右键项目 → Properties → Java Build Path
4. 确认 JRE 版本为 17+

## 启动步骤

1. **使用启动脚本（推荐）**:
   ```bash
   # Windows
   cd backend
   run.bat

   # Linux/macOS
   cd backend
   ./run.sh
   ```

2. **手动启动**:
   ```bash
   cd backend
   mvn clean compile
   mvn spring-boot:run
   ```

3. **IDE 中启动**:
   - 运行 `TeamKnowledgeBaseApplication.java` 的 main 方法

## 验证启动成功

启动成功后，您应该能够访问：

- **应用主页**: http://localhost:8080
- **API 文档**: http://localhost:8080/swagger-ui.html
- **健康检查**: http://localhost:8080/actuator/health

健康检查应返回:
```json
{"status":"UP"}
```

## 获取帮助

如果问题仍然存在：

1. 查看完整错误日志
2. 检查 `logs/teamkb.log` 文件
3. 确认所有环境配置正确
4. 参考 `ENVIRONMENT_SETUP.md` 详细配置说明

## 常用命令速查

```bash
# 项目命令
mvn clean                    # 清理编译产物
mvn compile                  # 编译项目
mvn spring-boot:run         # 启动应用
mvn test                    # 运行测试
mvn package                 # 打包应用

# 依赖管理
mvn dependency:tree         # 查看依赖树
mvn dependency:resolve      # 下载依赖
mvn dependency:analyze      # 分析依赖

# 环境检查
java -version              # Java 版本
mvn -version              # Maven 版本
echo $JAVA_HOME           # Java 安装路径 (Linux/macOS)
echo %JAVA_HOME%          # Java 安装路径 (Windows)
```