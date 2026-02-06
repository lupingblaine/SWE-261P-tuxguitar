# Get Coverage

```bash
./mvnw -f common/TuxGuitar-lib/pom.xml \
  org.jacoco:jacoco-maven-plugin:0.8.11:prepare-agent \
  test \
  org.jacoco:jacoco-maven-plugin:0.8.11:report

open common/TuxGuitar-lib/target/site/jacoco/index.html
```
