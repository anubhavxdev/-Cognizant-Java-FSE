# Exercise 2: Dependency Injection (Setter Injection)

## Objective
Implement dependency injection using Setter Injection through XML Bean wiring in a standalone Spring application.

---

## Folder Structure
```text
Exercise_2/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/library/
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

### 1. BookService.java
* **Purpose**: Demonstrates setter injection capability.
* **Complete Code**: See [BookService.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_2/src/main/java/com/library/service/BookService.java).
* **Line-by-Line Explanation**:
  - `BookService()`: The default constructor is called first during bean creation.
  - `setBookRepository(...)`: Spring calls this setter method next to inject the repository.
* **Interview Questions**: What are the characteristics of Setter Injection? (Ideal for optional or mutable dependencies; does not block object initialization if dependency is absent).

### 2. applicationContext.xml
* **Purpose**: Wires the relationship using declarative XML tags.
* **Complete Code**: See [applicationContext.xml](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_2/src/main/resources/applicationContext.xml).
* **Line-by-Line Explanation**:
  - `<property name="bookRepository" ref="bookRepository"/>`: Instructs the container to call `setBookRepository` passing the referenced `bookRepository` bean instance.

---

## How to Run
Navigate to `LibraryManagement/Exercise_2/` in terminal and run:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.library.LibraryManagementApplication"
```

---

## Expected Output
```text
Initializing XML ApplicationContext...
BookService: Instance created via default constructor.
BookService: setBookRepository() setter invoked. Dependency injected successfully.
Retrieving bookService bean...
Executing service operations...
BookService: Managing library catalog...
BookRepository: Checking library database connection...
```

---

## Concepts Used
1. **IoC (Inversion of Control)**: Delegating the control of object lifecycles and dependency management to the framework container.
2. **Dependency Injection**: Design pattern where objects receive their dependencies from external sources rather than creating them.
3. **Setter Injection**: Passing dependencies using standard Java setters.
4. **Bean Lifecycle**:
   ```text
   Instantiation (Constructor) -> Populate Properties (Setters) -> BeanNameAware -> BeanPostProcessors -> Init -> Ready -> Destroy
   ```

---

## Conclusion
Setter Injection allows building loosely coupled classes where dependencies are populated at runtime by the container.
