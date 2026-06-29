# Exercise 3: Spring AOP Logging

## Objective
Implement Aspect-Oriented Programming (AOP) logging inside a Spring application to capture execution times of service methods without altering actual service codes.

---

## Folder Structure
```text
Exercise_3/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/library/
│   │   │       ├── aspect/
│   │   │       │      LoggingAspect.java
│   │   │       ├── repository/
│   │   │       │      BookRepository.java
│   │   │       ├── service/
│   │   │       │      BookService.java
│   │   │       └── LibraryManagementApplication.java
│   │   └── resources/
│   │          applicationContext.xml
└── pom.xml
```

---

## Files Used

### 1. LoggingAspect.java
* **Purpose**: Implements the execution logging aspect using the `@Around` advice.
* **Complete Code**: See [LoggingAspect.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_3/src/main/java/com/library/aspect/LoggingAspect.java).
* **Line-by-Line Explanation**:
  - `@Aspect`: Tells the framework this class is an Aspect containing pointcut advices.
  - `@Around("execution(* com.library.service.BookService.*(..))")`: Declares around advice targeting all service methods.
  - `joinPoint.proceed()`: Continues executing the underlying target method.
* **Interview Questions**: What is Around advice? (The most powerful advice type in AOP. It can run both before and after a method execution, control its input/output parameters, and even bypass execution entirely).

### 2. applicationContext.xml
* **Purpose**: Declares core beans and activates AOP scanning.
* **Complete Code**: See [applicationContext.xml](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_3/src/main/resources/applicationContext.xml).
* **Line-by-Line Explanation**:
  - `<aop:aspectj-autoproxy />`: Enables Dynamic Proxy scanning to apply aspect logics at runtime.

---

## How to Run
Navigate to `LibraryManagement/Exercise_3/` in terminal and run:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.library.LibraryManagementApplication"
```

---

## Expected Output
```text
Loading XML Context for AOP demonstration...
BookService: Instance created via default constructor.
BookService: setBookRepository() setter invoked. Dependency injected successfully.
Invoking BookService.manageBooks()...
BookService: Managing library catalog...
BookRepository: Checking library database connection...
[AOP TIMER] Method 'manageBooks' completed execution in 82 ms.
```

---

## Concepts Used
1. **Aspect**: A modular unit capturing cross-cutting requirements.
2. **Advice**: Actions executed at join points (`@Around`).
3. **Pointcut**: Expressions selecting which methods to intercept.
4. **Join Point**: A candidate execution point (method invocation).
5. **Proxy**: A dynamically generated object wrapper that executes aspect advices.

---

## Conclusion
Spring AOP enables clean separation of concerns by capturing logging and execution statistics without cluttering core business classes.
