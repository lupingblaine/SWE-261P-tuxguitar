# Software Testing Report:  Part 4 Continuous Integration.
**Project:** TuxGuitar (Open Source Tablature Editor)  
**Course:** SWE 261P  
**Member:** Xiyao Li & Ping Lu  
**Date:** February 20, 2026  
**Base repositoy link:** [Click](https://github.com/helge17/tuxguitar)  
**Forked repositoy link:** [Click](https://github.com/lupingblaine/SWE-261P-tuxguitar)

## 1. Continuous Integration

### 1.1 What Continuous Integration Is and Why It Matters
Continuous Integration (CI) is a software development practice in which developers frequently integrate code changes into a shared repository. Each change is automatically built and tested to verify that the system remains correct and stable. The primary purpose of CI is to detect integration problems early and reduce the risk of defects in the codebase.

CI helps prevent common issues such as integration conflicts, late bug discovery, and inconsistent development environments. By providing fast and automated feedback after every commit, CI allows developers to quickly identify and fix errors.

Overall, CI improves software quality, keeps the main branch in a buildable state, and enhances team collaboration. It also serves as a foundation for Continuous Delivery and Continuous Deployment.

### 1.2 CI Platform Selection
For this project, we selected **GitHub Actions** as the CI platform.

Reasons:
* The repository is hosted on GitHub, so integration is native and easy to maintain.
* Workflow configuration is version-controlled in `.github/workflows/`.
* It provides cloud runners and supports Java/Maven builds directly.
* Build/test feedback appears on each push and pull request.

## 2. CI Configuration

### 2.1 Workflow File
**New CI workflow file:**  
`.github/workflows/part4-ci.yml`

### 2.2 What the Workflow Does
The workflow performs automated build and test for the core module `common/TuxGuitar-lib`.

Main behavior:
* Triggers on `push`, `pull_request`, and manual `workflow_dispatch`.
* Checks out the repository.
* Sets up **Temurin JDK 21** and enables Maven dependency cache.
* Runs Maven wrapper command to **build + test**:

```bash
./mvnw -f common/TuxGuitar-lib/pom.xml -e clean verify
```

This command compiles production code, compiles test code, executes JUnit tests, and verifies the module in a single CI step.

### 2.3 Workflow YAML
```yaml
name: Part4 CI Build and Test

on:
  push:
  pull_request:
  workflow_dispatch:

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v5
      - name: Set up JDK 21
        uses: actions/setup-java@v5
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven
      - name: Build and test TuxGuitar-lib
        run: ./mvnw -f common/TuxGuitar-lib/pom.xml -e clean verify
```

## 3. Build and Test Verification

### 3.1 Local Validation Command
Before relying on cloud CI, we validated the same command locally:

```bash
./mvnw -f common/TuxGuitar-lib/pom.xml -e clean verify
```

Observed result:
* **BUILD SUCCESS**
* **Tests run: 76, Failures: 0, Errors: 0, Skipped: 0**

### 3.2 CI Run Evidence (GitHub Actions)
After committing `.github/workflows/part4-ci.yml`, push the branch to GitHub and open the **Actions** tab to verify the run.

Suggested screenshots to include:
* Workflow list showing `Part4 CI Build and Test`.
* Run summary page showing all steps green.
* Log lines for Maven command and test summary.

![CI Workflow Run - Success](Part4Files/ci-build-test-success.png)

## 4. Issues Encountered and Notes
During validation, the build output may show compiler warnings and expected XML parsing stack traces inside negative test scenarios. These logs did **not** fail the run; the Maven result remained `BUILD SUCCESS`.

No blocking CI configuration issue was encountered for the selected module-level build and test pipeline.
