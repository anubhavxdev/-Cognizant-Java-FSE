# Exercise 7: Constructor and Setter Injection

## Objective
Implement and compare both Constructor Injection and Setter Injection wiring styles in a standalone Spring application.

---

## Folder Structure
```text
Exercise_7/
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
* **Purpose**: Declares both parametrized constructor and setter wiring points.
* **Complete Code**: See [BookService.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_7/src/main/java/com/library/service/BookService.java).
* **Line-by-Line Explanation**:
  - `BookService(BookRepository)`: Injects dependency during object construction.
  - `setBookRepository(BookRepository)`: Injects dependency post-instantiation.

### 2. applicationContext.xml
* **Purpose**: Compiles bean relationship wirings.
* **Complete Code**: See [applicationContext.xml](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_7/src/main/resources/applicationContext.xml).
* **Line-by-Line Explanation**:
  - `<constructor-arg ref="bookRepository"/>`: Resolves constructor signature mappings.
  - `<property name="bookRepository" ref="bookRepository"/>`: Triggers setter methods.

---

## How to Run
Navigate to `LibraryManagement/Exercise_7/` in terminal and run:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.library.LibraryManagementApplication"
```

---

## Expected Output
```text
Initializing XML Context...
BookService: Parametrized constructor (Constructor Injection) invoked.
BookService: Default constructor invoked.
BookService: setBookRepository() setter method invoked.
XML Context initialized successfully.

--- DEMONSTRATING CONSTRUCTOR INJECTION ---
BookService: Managing catalog using: Constructor Injection
BookRepository: Checking library database connection...

--- DEMONSTRATING SETTER INJECTION ---
BookService: Managing catalog using: Setter Injection
BookRepository: Checking library database connection...
```

---

## Constructor vs. Setter Injection

| Feature | Constructor Injection | Setter Injection |
|---|---|---|
| **Instantiation State** | Object is fully initialized with dependency. | Object is initialized without dependency first, then injected. |
| **Immutability** | Allows declaring final fields (`private final`). | Field cannot be final. |
| **Mandatory Dependencies** | Preferred. Prevents creating objects in invalid states. | Not recommended (harder to enforce checks). |
| **Optional Dependencies** | Less clean (requires multiple constructor overrides). | Preferred. Easily skipped or modified later. |
| **Circular Dependencies** | Catches circular references at compile time (throws exception). | Resolves circular references easily. |

### When to Prefer Constructor Injection
* For mandatory dependencies to ensure target beans are never instantiated in an invalid/incomplete state.
* To promote immutability by using the `final` keyword on fields.
* To simplify unit testing without loading the Spring Container (dependencies can be passed easily via `new BookService(mockRepo)`).

### When to Prefer Setter Injection
* For optional dependencies or properties with default values.
* If dependencies need to be reconfigured or updated dynamically during runtime.

---

## Conclusion
Spring supports both patterns. Generally, constructor injection is preferred for core components, while setter injection remains useful for optional runtime configurations.
