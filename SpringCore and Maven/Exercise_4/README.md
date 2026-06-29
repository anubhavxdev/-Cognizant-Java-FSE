# Exercise 4: Maven Configuration

## Objective
Configure a standalone Maven `pom.xml` build configuration file containing only the core dependencies needed for Spring Core, Spring WebMVC, and Spring AOP operations under Java 8.

---

## Folder Structure
```text
Exercise_4/
├── pom.xml
└── README.md
```

---

## Files Used

### 1. pom.xml
* **Purpose**: Declares and controls dependency imports and compile configurations.
* **Complete Code**: See [pom.xml](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_4/pom.xml).
* **Line-by-Line Explanation**:
  - `spring-core`: Foundational utility components of the Spring ecosystem.
  - `spring-beans`: Instantiates and maintains object registrations.
  - `spring-context`: Provides the main runtime container environment.
  - `spring-aop`: Powers cross-cutting program interceptors.
  - `spring-webmvc`: Provides MVC servlet architectures for building Web APIs.
  - `aspectjweaver`: Implements aspectj expressions and AOP weavers.
  - `maven-compiler-plugin`: Binds compiler compliance to Java 1.8.
* **Common Errors**: Conflicting versions of Spring dependencies (which can raise `NoSuchMethodError`). Always keep versions synchronized (e.g. using a property variable like `${spring.version}`).
* **Interview Questions**: What is AspectJ Weaver used for in Spring? (It provides the full runtime libraries to parse aspect declarations and compile dynamic proxy bytecode).

---

## How to Run
To compile and verify the pom.xml dependencies:
```bash
mvn clean compile
```

---

## Expected Output
Running `mvn clean compile` will yield:
```text
[INFO] Scanning for projects...
[INFO] Building exercise-4 1.0-SNAPSHOT
[INFO] BUILD SUCCESS
```

---

## Concepts Used
1. **Maven Build Tool**: Programmatic dependency and compiler lifecycle manager.
2. **Dynamic Transitive Resolution**: Pulling dependent packages automatically based on core declarations.
3. **Compiler Plugin**: Enforcing Java 8 language standard constraints.

---

## Conclusion
A minimal and clean `pom.xml` ensures compilation consistency across team environments while keeping dependencies lean.
