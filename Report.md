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

### 3.1 Testing Frameworks and Implementation
TuxGuitar utilizes **JUnit 5 (junit-jupiter-engine)** as its primary testing framework. The testing architecture follows a modular approach, where test suites are decentralized into individual component directories to ensure isolated verification of logic.

### 3.2 Key Testing Modules and File Locations
Based on our repository analysis, the existing test cases are primarily concentrated in the following core logic modules:
* **TuxGuitar-lib (`common/TuxGuitar-lib/src/test/java`)**: Houses critical music model tests, such as `TestTGDuration` and `TestTrackManager`.
* **Testing Resources**: The module utilizes dedicated resource files for file format verification, including `test_20.xml` and `test_midi_20.tg`.

### 3.3 Build Verification and Troubleshooting
During the initial execution of the test suite via Maven, we encountered a build hurdle that required technical intervention.

#### **Technical Challenge: Path Encoding Conflict**
When running `mvn test` within the `TuxGuitar-lib` module, the build initially failed with multiple `java.io.FileNotFoundException` errors.
* **Symptom**: The Maven Surefire plugin reported that it could not locate standardized music resource files (e.g., `Untitled_20.xml`).
* **Root Cause Analysis**: The local directory path contained special characters and spaces (`&`, `:`, and `%20`), which prevented the Java `FileInputStream` from correctly resolving absolute paths to the `target/test-classes/` directory.
* **Resolution**: We performed a "clean relocation" by moving the project to a standardized alphanumeric directory path. We then executed `mvn clean test` to purge corrupted build artifacts and re-index the resource metadata.

#### **Successful Test Execution**
After resolving the environment-specific pathing issues, we successfully executed the full test suite for the core library.

![TuxGuitar-lib Test Success Summary](testallresult.jpg)

As shown in the execution log above, the system verified **64 test cases** with zero failures or errors, confirming the integrity of the core music engine, file I/O operations, and duration management logic.

---