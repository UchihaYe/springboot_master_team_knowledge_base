# 环境配置指南

## 依赖问题解决方案

如果您遇到以下依赖项错误：
- `找不到依赖项 'org.sqlite:sqlite-jdbc:'`
- `找不到依赖项 'org.apache.commons:commons-io:2.11.0'`

这些问题已经在最新的 `pom.xml` 中得到修复：

### 修复内容：

1. **SQLite JDBC 驱动**:
   - 从 `org.sqlite:sqlite-jdbc` 更改为 `org.xerial:sqlite-jdbc:3.44.1.0`
   - 这是正确的SQLite JDBC驱动程序

2. **Commons IO**:
   - 从 `org.apache.commons:commons-io` 更改为 `commons-io:commons-io:2.11.0`
   - 使用正确的groupId

## Java 环境配置

### Windows 系统

如果遇到 JAVA_HOME 错误，请按以下步骤配置：

1. **安装 Java 17+**
   - 下载并安装 JDK 17 或更高版本
   - 推荐使用 OpenJDK 或 Oracle JDK

2. **配置环境变量**
   ```cmd
   # 设置 JAVA_HOME（替换为您的实际JDK路径）
   set JAVA_HOME=C:\Program Files\Java\jdk-17

   # 添加到 PATH
   set PATH=%JAVA_HOME%\bin;%PATH%

   # 验证安装
   java -version
   javac -version
   ```

3. **永久设置环境变量**
   - 右键"此电脑" -> "属性" -> "高级系统设置"
   - 点击"环境变量"
   - 新建系统变量：
     - 变量名: `JAVA_HOME`
     - 变量值: `C:\Program Files\Java\jdk-17` (您的JDK安装路径)
   - 编辑 PATH 变量，添加: `%JAVA_HOME%\bin`

### Linux/macOS 系统

1. **安装 Java 17+**
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install openjdk-17-jdk

   # macOS (使用 Homebrew)
   brew install openjdk@17
   ```

2. **配置环境变量**
   ```bash
   # 添加到 ~/.bashrc 或 ~/.zshrc
   export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
   export PATH=$JAVA_HOME/bin:$PATH

   # 重新加载配置
   source ~/.bashrc
   ```

## Maven 配置

### 检查 Maven 安装

```bash
mvn -version
```

如果 Maven 未安装，请：

**Windows:**
1. 下载 Maven 3.6+
2. 解压到如 `C:\Program Files\Apache\maven`
3. 设置环境变量：
   - `MAVEN_HOME`: `C:\Program Files\Apache\maven`
   - 添加到 PATH: `%MAVEN_HOME%\bin`

**Linux/macOS:**
```bash
# Ubuntu/Debian
sudo apt install maven

# macOS
brew install maven
```

## 项目启动步骤

1. **确保环境正确**
   ```bash
   java -version    # 应显示 Java 17+
   mvn -version     # 应显示 Maven 3.6+
   ```

2. **清理并编译项目**
   ```bash
   cd backend
   mvn clean compile
   ```

3. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

## 常见问题解决

### 问题 1: "JAVA_HOME 未正确定义"
**解决方案**: 按照上述步骤正确设置 JAVA_HOME 环境变量

### 问题 2: "找不到依赖项"
**解决方案**:
1. 确保使用最新的 `pom.xml`
2. 运行 `mvn clean install` 重新下载依赖

### 问题 3: "编译错误"
**解决方案**:
1. 检查 Java 版本是否为 17+
2. 运行 `mvn clean compile` 重新编译

### 问题 4: "端口被占用"
**解决方案**:
1. 更改 `application.yml` 中的端口号
2. 或者停止占用 8080 端口的其他进程

## IDE 配置

### IntelliJ IDEA
1. 导入项目时选择 "Maven" 项目
2. 确保 Project SDK 设置为 Java 17+
3. 在 Settings -> Build -> Build Tools -> Maven 中配置正确的 Maven 路径

### Eclipse
1. Import -> Existing Maven Projects
2. 确保 Java Build Path 中的 JRE 版本为 17+
3. 右键项目 -> Maven -> Reload Projects

### VS Code
1. 安装 Java Extension Pack
2. 确保 settings.json 中配置了正确的 JAVA_HOME

## 验证安装

运行以下命令验证一切配置正确：

```bash
cd backend
mvn clean compile
mvn spring-boot:run
```

如果启动成功，您应该看到：
```
Started TeamKnowledgeBaseApplication in X.XXX seconds
```

然后可以访问：
- 应用: http://localhost:8080
- API文档: http://localhost:8080/swagger-ui.html
- 健康检查: http://localhost:8080/actuator/health