# SWE 261P Project -- Part 1 升级版 Cheat Sheet（Section 1--4.2.2）

------------------------------------------------------------------------

## 一、项目概述（Project Overview）

**项目名称：** TuxGuitar\
**类型：** 开源吉他谱 / 乐谱编辑软件\
**语言：** Java\
**规模：** \~266,800 行代码，\~2,658 个 Java 文件

**核心作用：** - 创建 / 编辑 / 播放 多轨乐谱（multi-track tablature） -
支持 MIDI、Guitar Pro 等格式 - 使用 SWT 作为 GUI 框架

面试总结一句话：\
\> TuxGuitar is a large-scale Java-based music notation system with
modular architecture and a dedicated core library layer.

------------------------------------------------------------------------

## 二、构建与测试环境（Build & Test Environment）

### 依赖（Prerequisites）

-   JDK 9+
-   Maven 3.3+
-   SWT 4

### 关键模块（Core Module）

测试集中在：

    common/TuxGuitar-lib

### 路径问题（Path Encoding Conflict）

如果路径包含特殊字符（& : %20），可能导致： - FileNotFoundException -
测试资源无法加载

解决方法： - 将项目移动至纯字母数字路径 - 执行：

    mvn clean test

------------------------------------------------------------------------

## 三、核心测试概念（Core Testing Concepts）

### 1️⃣ 系统化功能测试（Systematic Functional Testing）

黑盒测试（black-box testing），
但测试用例不是随便选的，而是基于策略（strategy-based）。

目标： - 有计划覆盖行为（behavior coverage） - 提高缺陷发现率（defect
detection efficiency）

------------------------------------------------------------------------

### 2️⃣ 分区测试（Partition Testing / Equivalence Class Testing）

思想：

将输入空间（input domain）划分为若干等价类（equivalence classes）。\
每个类选一个代表值（representative value）。

通常重点测试： - 合法输入（valid input） - 非法输入（invalid input） -
边界值（boundary values）

目的： \> Reduce infinite input space to finite, meaningful partitions.

------------------------------------------------------------------------

## 四、TestTGDuration 新增测试（Partition Testing 示例）

测试对象：

    splitPreciseDuration(total, max, factory)

作用： - 将总时长 total 拆分为若干合法 duration - 每段 ≤ max -
所有段相加 = total - 无法表示 → 返回 null

------------------------------------------------------------------------

### P1 -- Valid Simple Split（基础等价类）

示例： - total = 1/2 whole - max = 1/8 whole

预期： - 返回 4 个 1/8

意义： - 验证标准拆分逻辑正确 - 抓 off-by-one bug

------------------------------------------------------------------------

### P2 -- Fine Subdivision（细粒度分区）

示例： - total = 3/64

验证： - 所有拆分后 duration 求和 = total

意义： - 防止精度丢失（precision loss） - 验证复杂组合正确

------------------------------------------------------------------------

### P3 -- Impossible Case（非法等价类）

示例： - total = 1/19

预期： - 返回 null

意义： - 验证系统能正确拒绝非法输入 - 防止死循环（infinite loop）

------------------------------------------------------------------------

### P4 -- Max Boundary（上限边界测试）

示例： - max = 3/8 whole

验证： - 每段 ≤ max - 总和正确

意义： - 验证约束条件（constraint enforcement）

------------------------------------------------------------------------

### P5 -- Robustness（鲁棒性测试）

示例： - total = 5 whole

验证： - 不抛异常（no crash）

意义： - 防止溢出（overflow） - 防止异常路径错误

------------------------------------------------------------------------

## 五、TestMusicKeyUtils 新增测试（输入合法性分区）

测试对象：

    noteName / noteFullName / sharpNoteFullName

输入维度： 1. MIDI note 2. Key Signature（调号）

------------------------------------------------------------------------

### Q1 -- Valid MIDI Boundary

-   MIN_MIDI_NOTE = 12 → C0
-   MAX_MIDI_NOTE = 127 → G9

意义： - 抓边界错误（boundary bug） - 验证八度计算（octave calculation）

------------------------------------------------------------------------

### Q2 -- Invalid MIDI

示例： - midi = 0 - midi = 200

预期： - 返回 null

意义： - 验证输入校验（input validation）

------------------------------------------------------------------------

### Q3 -- Invalid Key Signature

示例： - keySignature = -1 - keySignature = 15

预期： - 返回 null

意义： - 验证调号合法性校验

------------------------------------------------------------------------

## 六、运行命令（Run Commands）

### 单独运行 TestMusicKeyUtils

    ./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestMusicKeyUtils test

### 单独运行 TestTGDuration

    ./mvnw -f common/TuxGuitar-lib/pom.xml -Dtest=TestTGDuration test

### 运行整个模块测试

    cd common/TuxGuitar-lib
    mvn clean test

------------------------------------------------------------------------

## 七、高频问答（Likely Questions）

**Q: 为什么用 Partition Testing？**\
A: 因为输入空间巨大（huge input domain），必须通过等价类降低复杂度。

**Q: 为什么测试边界值？**\
A: 边界最容易出错（boundary bugs）。

**Q: 为什么 invalid 输入返回 null？**\
A: 这是 API 设计选择（defensive API design），保持一致性。

**Q: factory 是什么？**\
A: 对象创建器（Factory Pattern），统一创建模型对象。

------------------------------------------------------------------------

# 最后一句总结（Presentation 版本）

> In Part 1, we analyzed the existing test architecture, applied
> systematic partition testing to selected features, designed
> representative boundary and invalid inputs, and implemented additive
> JUnit tests to strengthen functional coverage.
