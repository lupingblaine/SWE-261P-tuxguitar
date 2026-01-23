# Software Testing Report: Part 1 - Functional Partitioning
**Project:** TuxGuitar (Open Source Tablature Editor)  
**Course:** SWE 261P  
**Member:** Xiyao Li & Ping Lu  
**Date:** January 23, 2026

---

## 1. Introduction

### 1.1 Project Background
TuxGuitar is a widely adopted open-source application designed for creating, editing, and playing multi-track musical tablature. It functions as a versatile platform for music notation, compatible with numerous guitar score formats and offering both desktop and mobile interfaces. Its acceptance within the global music community is largely due to its cross-platform nature and robust handling of intricate musical compositions.

### 1.2 Technical Analysis and Scope
An examination of the project repository confirms that TuxGuitar qualifies as a substantial Java-based system, comfortably meeting the specified complexity thresholds:
* **Core Technology:** The application is developed chiefly in Java. The build incorporates the Standard Widget Toolkit (SWT) for the graphical user interface and allows for integration with optional native sound libraries such as FluidSynth or Jack for audio playback.
* **Codebase Volume:** The project contains roughly 266,800 lines of Java source code.
* **Architectural Complexity:** With a total of 2,658 distinct Java files, the system's scale significantly surpasses the baseline requirements of 15,000 lines of code and 100 classes.

---

## 2. Build and Environment Documentation

### 2.1 Prerequisites
To establish a functional build environment, the following dependencies must be configured:
* **JDK:** Version 9 or higher.
* **Build Tool:** Apache Maven 3.3 or higher.
* **GUI Library:** SWT 4.
* **Optional Components:** FluidSynth / Jack (for advanced audio features).

### 2.2 Compilation and Execution (macOS Example)
The following steps outline the procedure for building the project on a macOS environment:

1.  **Dependency Installation:** Install `openjdk`, `maven`, and `wget` via Homebrew:
    `brew install openjdk maven wget`
2.  **SWT Configuration:** Download the platform-specific SWT zip and install the `.jar` into the local Maven repository:
    ```bash
    TUX_ARCH=`uname -m | sed 's/arm64/aarch64/'`
    wget [https://download.eclipse.org/eclipse/downloads/drops4/R-4.37-202509050730/swt-4.37-cocoa-macosx-$](https://download.eclipse.org/eclipse/downloads/drops4/R-4.37-202509050730/swt-4.37-cocoa-macosx-$){TUX_ARCH}.zip
    unzip swt-4.37-cocoa-macosx-${TUX_ARCH}.zip
    mvn install:install-file -Dfile=swt.jar -DgroupId=org.eclipse.swt -DartifactId=org.eclipse.swt.cocoa.macosx -Dpackaging=jar -Dversion=4.37
    ```
3.  **Core Build Execution:** Navigate to the build directory and execute the Maven lifecycle:
    ```bash
    cd desktop/build-scripts/tuxguitar-macosx-swt-cocoa
    mvn -e clean verify -P native-modules
    ```
4.  **Running the SUT:** Launch the generated `.app` located in the `target/` directory:
    `open desktop/build-scripts/tuxguitar-macosx-swt-cocoa/target/TuxGuitar.app`

---

## 3. Study of Existing Testing Practices

### 3.1 Testing Frameworks and Architecture
TuxGuitar utilizes **JUnit 5 (junit-jupiter-engine)** as its primary testing engine. The testing infrastructure is organized following the standard Maven project structure, where test sources are decoupled from main application logic. The most critical logic tests are housed within the `common/` directory, which contains the platform-independent music theory and file format modules.

### 3.2 Key Testing Modules and File Locations
The following core modules contain the most significant testing assets, which we analyzed to understand the system's quality assurance practices:

* **TuxGuitar-lib (`common/TuxGuitar-lib/src/test/java`)**:
    Contains tests for core musical entities. A key example is `org.herac.tuxguitar.io.base.TGRawExporterTestCase`, which validates the fundamental data export logic.
* **TuxGuitar-compat (`common/TuxGuitar-compat/src/test/java`)**:
    Focuses on backward compatibility and cross-module integration.
* **TuxGuitar-midi (`common/TuxGuitar-midi/src/test/java`)**:
    Ensures the integrity of MIDI event generation and parsing.

### 3.3 Test Resource Management
Unlike many simpler projects, TuxGuitar manages a specialized set of binary and XML music files for regression testing. These are located in:
* `common/TuxGuitar-lib/src/test/resources/`
Notable test inputs include:
* `test_20.xml`: A standardized XML representation of a score for structural validation.
* `test_midi_20.tg`: A native TuxGuitar format file used to verify MIDI engine consistency.

### 3.4 Test Execution and Reporting
The project leverages the **Maven Surefire Plugin** to automate test execution. To run the suite for the core library and generate a coverage-ready report, we use the following scoped command:
```bash
mvn test -pl common/TuxGuitar-lib