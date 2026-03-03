
# SWE 261P 项目演示总 README（Part 1 - Part 5）
> 项目：SWE-261P-tuxguitar  
> 说明：所有命令默认在**仓库根目录（包含 ./mvnw 的目录）**执行。

============================================================
# Part 1 – Functional Testing & Partitioning（功能测试 & 等价类划分）
============================================================

## 相关测试文件路径
- common/TuxGuitar-lib/src/test/java/.../TestMusicKeyUtils.java
- common/TuxGuitar-lib/src/test/java/.../TestTGDuration.java

## 运行单个测试

### MusicKeyUtils
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestMusicKeyUtils test

### TGDuration
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestTGDuration test

## 运行整个模块测试
./mvnw -f common/TuxGuitar-lib/pom.xml clean test


============================================================
# Part 2 – FSM（有限状态机 Functional Model）
============================================================

## 相关测试文件路径
- common/TuxGuitar-lib/src/test/java/.../TestTGVoiceStateMachine.java
- common/TuxGuitar-lib/src/test/java/.../TestMidiPlayerMode.java

## 运行单个 FSM 测试

### TGVoice 状态机
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestTGVoiceStateMachine test

### MidiPlayerMode 状态机
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestMidiPlayerMode test

## 运行全部模块测试
./mvnw -f common/TuxGuitar-lib/pom.xml clean test


============================================================
# Part 3 – White-Box Testing & Coverage（白盒测试 & 覆盖率）
============================================================

## 相关测试文件路径
- common/TuxGuitar-lib/src/test/java/.../TestBase64Codec.java

## 运行新增测试
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestBase64Codec test

## 生成 JaCoCo 覆盖率报告
./mvnw -f common/TuxGuitar-lib/pom.xml   org.jacoco:jacoco-maven-plugin:0.8.11:prepare-agent   test   org.jacoco:jacoco-maven-plugin:0.8.11:report

## 覆盖率报告位置
common/TuxGuitar-lib/target/site/jacoco/index.html


============================================================
# Part 4 – Continuous Integration（持续集成）
============================================================

## 新增 Workflow 文件
.github/workflows/part4-ci.yml

## CI 执行命令（GitHub Actions 中）
./mvnw -f common/TuxGuitar-lib/pom.xml -e clean verify

## 本地复现 CI
./mvnw -f common/TuxGuitar-lib/pom.xml -e clean verify

## GitHub 演示步骤
1. Push 或 Pull Request 触发 workflow
2. 打开 GitHub → Actions
3. 选择 Part4 CI Build and Test
4. 查看 JDK 安装、Maven 构建、测试日志
5. 展示 BUILD SUCCESS


============================================================
# Part 5 – Testable Design / Stubbing / Mocking
============================================================

## 一、Stubbing 相关文件

### 现有 Stubbing 示例
common/TuxGuitar-lib/src/test/java/app/tuxguitar/player/base/TestLetRing.java

### 新增 Stubbing 测试
common/TuxGuitar-lib/src/test/java/app/tuxguitar/thread/TestTGThreadManagerStubbing.java

### 相关生产代码
common/TuxGuitar-lib/src/main/java/app/tuxguitar/thread/TGThreadManager.java
common/TuxGuitar-lib/src/main/java/app/tuxguitar/thread/TGThreadHandler.java

## 运行 Stubbing 测试
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestLetRing test
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestTGThreadManagerStubbing test


------------------------------------------------------------
## 二、Bad Testable Design 改进

### 修改生产代码
common/TuxGuitar-lib/src/main/java/app/tuxguitar/player/base/MidiSynthesizerProxy.java

### 新增测试
common/TuxGuitar-lib/src/test/java/app/tuxguitar/player/base/TestMidiSynthesizerProxyTestability.java

## 运行测试
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestMidiSynthesizerProxyTestability test


------------------------------------------------------------
## 三、Mocking（Mockito）

### Proxy Delegation 测试
common/TuxGuitar-lib/src/test/java/app/tuxguitar/player/base/TestMidiSynthesizerProxyMocking.java

### MidiTransmitter Fan-out 测试
common/TuxGuitar-lib/src/test/java/app/tuxguitar/player/base/TestMidiTransmitterMocking.java

### 相关生产代码
common/TuxGuitar-lib/src/main/java/app/tuxguitar/player/base/MidiTransmitter.java
common/TuxGuitar-lib/src/main/java/app/tuxguitar/player/base/MidiSynthesizer.java

### Mockito 依赖位置
common/TuxGuitar-lib/pom.xml

## 运行 Mocking 测试
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestMidiSynthesizerProxyMocking test
./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestMidiTransmitterMocking test


============================================================
# 一键运行整个模块测试（推荐演示用）
============================================================

./mvnw -f common/TuxGuitar-lib/pom.xml clean test

