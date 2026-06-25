# Exercise 11 - Dependency Injection

## Objective

The **Dependency Injection (DI) Pattern** is a design pattern used to implement **Inversion of Control (IoC)**. It allows the creation of dependent objects outside of a class and provides those objects to a class in various ways (Constructor Injection, Setter Injection, or Field Injection).

This exercise demonstrates the use of **Constructor Injection** to decouple a service class (`CustomerService`) from its data access layer (`CustomerRepository`).

**In simple words:**
> Think of a car and an engine:
> - **Tight Coupling:** The car manufactures its own engine inside its body. If you want to change the engine (e.g., to an electric engine), you have to redesign/modify the entire car.
> - **Dependency Injection (Loose Coupling):** The engine is built separately. When assembling the car, you "inject" (install) the engine into the engine bay. The car doesn't care who made the engine, as long as it conforms to the standard engine interface. You can swap engines easily without modifying the car itself.

---

## Problem Statement

### Why do we need Dependency Injection?

Without Dependency Injection, classes create their own dependencies using the `new` keyword. This leads to **Tight Coupling**:

```java
// TIGHT COUPLING -- The service is locked to a specific database implementation
public class CustomerService {
    private CustomerRepository repository;

    public CustomerService() {
        // Hardcoded dependency! Cannot change or mock this easily.
        this.repository = new CustomerRepositoryImpl();
    }
}
```

**Problems with Tight Coupling:**
1. **Difficulty in Testing:** You cannot easily test `CustomerService` in isolation because it is hardwired to `CustomerRepositoryImpl` (which might connect to a real database). Mocking the repository is extremely difficult.
2. **Lack of Flexibility:** If you want to switch from `CustomerRepositoryImpl` (e.g., SQL database) to `NoSqlCustomerRepositoryImpl`, you must modify the source code of `CustomerService`.
3. **Violates Dependency Inversion Principle (DIP):** High-level modules (`CustomerService`) depend directly on low-level modules (`CustomerRepositoryImpl`) instead of abstractions.

**With Dependency Injection (Loose Coupling):**
- The service depends on an interface (`CustomerRepository`).
- The concrete implementation is passed (injected) from the outside.
- The service class remains unchanged even if the implementation changes.

---

## Design Pattern Classification

- **Category:** Behavioral / Creational (often classified under Creational because it deals with object assembly, but is primarily an architectural pattern).
- **Intent:** Separate the creation of an object's dependencies from its behavior to promote loose coupling and high testability.
- **Key Principle:** *Dependency Inversion Principle (DIP)* - "Depend on abstractions, not on concretions."

---

## Tight Coupling vs. Loose Coupling

| Feature | Tight Coupling (Hardcoded Dependency) | Loose Coupling (Dependency Injection) |
| :--- | :--- | :--- |
| **Instantiation** | The class creates its own dependencies using `new`. | Dependencies are created externally and injected. |
| **Dependency Type** | Depends on concrete classes (`CustomerRepositoryImpl`). | Depends on interfaces (`CustomerRepository`). |
| **Testability** | Hard to unit test in isolation (requires real database/resources). | Extremely easy to unit test by injecting Mock objects. |
| **Maintainability** | High risk; changing one class breaks dependent classes. | Low risk; changing implementation does not affect the client. |
| **Flexibility** | Cannot switch implementations at runtime. | Easy to swap implementations dynamically at runtime. |

---

## Project Structure

```
Exercise-11-DependencyInjection/
|
|-- CustomerRepository.java       <-- Dependency Interface (Abstraction)
|-- CustomerRepositoryImpl.java   <-- Concrete Dependency (Implementation)
|-- CustomerService.java          <-- Service Class (Client receiving Injection)
|-- DependencyInjectionTest.java  <-- Assembler / Test Runner (Performs Injection)
|-- README.md                     <-- Documentation (this file)
```

---

## UML Class Diagram

```
+-----------------------------------+            +-----------------------------------+
|          CustomerService          |            |           <<interface>>           |
|-----------------------------------|            |         CustomerRepository        |
| - repository: CustomerRepository  +----------->|-----------------------------------|
|-----------------------------------|            | + findCustomerById(id: int):String|
| + CustomerService(repository:     |            +-----------------^-----------------+
|     CustomerRepository)           |                              |
| + findCustomerById(id: int):String|                              | implements
+-----------------------------------+                              |
                                                 +-----------------+-----------------+
                                                 |       CustomerRepositoryImpl      |
                                                 |-----------------------------------|
                                                 | + findCustomerById(id: int):String|
                                                 +-----------------------------------+
```

---

## Workflow of Dependency Injection

```
 [1. Instantiate]                   [2. Inject]                         [3. Execute]
 
+------------------------+      +---------------------+             +-----------------+
| CustomerRepositoryImpl |      |   CustomerService   |             | Client (Test)   |
| (Concrete Dependency)  |      |   (Client Class)    |             | (Assembler)     |
+-----------+------------+      +----------+----------+             +--------+--------+
            |                              |                                 |
            |   New Instance               |                                 |
            |<-----------------------------+---------------------------------|
            |                              |                                 |
            |   Passes dependency in       |                                 |
            |   constructor                |                                 |
            |----------------------------->|                                 |
            |                              |                                 |
            |                              |   service.findCustomerById(101) |
            |                              |<--------------------------------|
            |   Delegated lookup           |                                 |
            |<-----------------------------|                                 |
            |   Returns "John Doe"         |                                 |
            |----------------------------->|                                 |
            |                              |   Returns result to Client      |
            |                              |-------------------------------->|
```

---

## Constructor Injection vs. Setter Injection vs. Field Injection

| Injection Type | Description | Best For | Code Example | Pros / Cons |
| :--- | :--- | :--- | :--- | :--- |
| **Constructor Injection** | Dependencies are passed through the class constructor. | **Mandatory** & Immutable dependencies. | `public CustomerService(CustomerRepository repo)` | **Pros:** Immutable fields (can use `final`), guarantees all dependencies are present at construction, easier to test. |
| **Setter Injection** | Dependencies are passed through setter methods. | **Optional** or changeable dependencies. | `public void setRepository(CustomerRepository repo)` | **Pros:** Can change dependency later.<br>**Cons:** Object can exist in a half-configured state; fields cannot be `final`. |
| **Field Injection** | Dependencies are injected directly into fields via reflection. | Framework-driven boilerplate reduction (e.g., Spring). | `@Autowired private CustomerRepository repo;` | **Pros:** Clean code with no boilerplate.<br>**Cons:** Tight coupling with DI framework, hard to test without framework, fields cannot be `final`. |

---

## Dependency Injection in the Spring Framework

In modern Java development, the **Spring Framework** acts as the Assembler/IoC Container. Instead of wiring components manually, Spring manages them automatically using **Stereotype Annotations**:

1. **`@Component` / `@Repository` / `@Service`**: Tells Spring to manage these classes as "Beans" in its application context container.
2. **`@Autowired`**: Instructs Spring to automatically inject the matching bean. When a class has a single constructor, Spring automatically uses Constructor Injection without even needing the `@Autowired` annotation (starting from Spring 4.3).

### Equivalent Spring Code:

```java
// 1. Mark the concrete repository as a Spring-managed repository bean
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    @Override
    public String findCustomerById(int id) {
        return "John Doe";
    }
}

// 2. Mark the service and let Spring inject the repository via Constructor Injection
@Service
public class CustomerService {
    private final CustomerRepository repository;

    // Spring automatically injects the CustomerRepository bean here
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public String findCustomerById(int id) {
        return repository.findCustomerById(id);
    }
}
```

---

## Real-World Examples of Dependency Injection

| Framework / Library | Language | Description |
| :--- | :--- | :--- |
| **Spring IoC Container** | Java | The industry standard for enterprise Java applications, wiring databases, transaction handlers, and REST controllers automatically. |
| **Google Guice** | Java | A lightweight, annotation-driven DI framework developed by Google, commonly used in modular desktop and backend systems. |
| **Java EE / Jakarta EE CDI** | Java | Contexts and Dependency Injection (CDI) is the standard DI specification in enterprise Java application servers (like WildFly or GlassFish). |
| **Dagger & Hilt** | Java / Kotlin | Compile-time dependency injection frameworks developed by Google, primarily used in Android applications to ensure optimal performance. |
| **NestJS / Angular** | TypeScript | Popular web frameworks that use built-in DI containers to inject services, repositories, and controllers. |

---

## How to Compile and Run

Make sure you have Java installed on your system. Run these commands in your shell:

```powershell
# Navigate to the project directory
cd "Exercise-11-DependencyInjection"

# Compile all Java files in the directory
javac *.java

# Run the test execution class
java DependencyInjectionTest
```

---

## Expected Output

Upon executing `DependencyInjectionTest`, you should see the following output in the console:

```
=============================================
   Dependency Injection Pattern - Customer App
=============================================

Customer Found:
ID: 101
Name: John Doe

--- Testing with a non-existent customer ID ---
Customer with ID 999 not found in the repository.

=============================================
   DI Demonstration completed successfully!  
=============================================
```

---

## Viva Questions with Answers (Interview Preparation)

### Q1: What is Dependency Injection (DI)?
**A:** Dependency Injection is a design pattern where an object's dependencies (services it requires to work) are provided (injected) by an external entity (assembler or container) rather than being created by the object itself. It is a way to achieve Inversion of Control (IoC).

---

### Q2: What is the difference between Inversion of Control (IoC) and Dependency Injection (DI)?
**A:** **IoC** is a broad software engineering principle where the control flow of a program is inverted (e.g., instead of our code calling a framework, the framework calls our code). **DI** is a specific design pattern and mechanism used to implement IoC by feeding dependencies to a class at runtime.

---

### Q3: Why is Constructor Injection preferred over Setter Injection?
**A:** Constructor Injection is preferred because:
1. It allows dependencies to be declared `final`, making the injected fields **immutable**.
2. It guarantees that the object is always created in a **valid state** (you cannot instantiate the class without providing required dependencies).
3. It prevents circular dependencies at startup (compile-time/run-time validation).
4. It is easier to unit test manually without a DI framework.

---

### Q4: When should you use Setter Injection instead of Constructor Injection?
**A:** Setter Injection is useful for **optional dependencies** or dependencies that might change or be re-configured at runtime. However, if a dependency is mandatory for the class to function, Constructor Injection should always be used.

---

### Q5: Explain the Dependency Inversion Principle (DIP) in SOLID.
**A:** DIP states that:
1. High-level modules should not depend on low-level modules. Both should depend on abstractions (interfaces).
2. Abstractions should not depend on details. Details should depend on abstractions.
In this exercise, `CustomerService` (high-level) depends on the interface `CustomerRepository` (abstraction), not `CustomerRepositoryImpl` (detail).

---

### Q6: What role does an "Assembler" or "IoC Container" play in DI?
**A:** The Assembler (or IoC Container like Spring) is the third party responsible for:
1. Instantiating the dependency (`CustomerRepositoryImpl`).
2. Instantiating the client (`CustomerService`).
3. Injecting the dependency into the client (wiring them together).
This removes the object lifecycle and configuration responsibility from the business classes.

---

### Q7: What are the main benefits of Dependency Injection?
**A:** 
- **Loose Coupling:** Classes are independent of concrete implementation details.
- **Enhanced Testability:** Dependencies can be swapped with Mock objects (e.g., using Mockito) during unit testing.
- **Reusability:** Components can be reused with different configurations.
- **Maintainability:** Changing one class does not require changing code in other dependent classes.
- **Parallel Development:** Developers can work on different components (e.g., UI vs Database) simultaneously by agreeing on an interface.

---

### Q8: Does Spring require the `@Autowired` annotation on constructors?
**A:** Starting from **Spring 4.3**, if a Spring bean (class) has only **one constructor**, the `@Autowired` annotation on that constructor is **optional**. Spring will automatically detect and inject the required dependencies.

---

### Q9: Can Dependency Injection cause any performance overhead?
**A:** Generally, **no**. In runtime reflection-based frameworks (like Spring), there is a minor overhead during application startup when beans are created and wired together. However, once the beans are initialized, execution speed is identical to manual wiring. In compile-time DI frameworks (like Dagger/Hilt), there is zero runtime overhead because wiring code is generated at compile time.

---

### Q10: What is a Mock object, and how does DI make Mocking easier?
**A:** A Mock object is a dummy implementation of an interface that simulates real behavior (e.g., returning predefined data without querying a database) for testing purposes. Because `CustomerService` accepts any `CustomerRepository` interface implementation in its constructor, we can write a test that passes a Mock repository directly:
```java
// Testing CustomerService using a Mock repository
CustomerRepository mockRepo = Mockito.mock(CustomerRepository.class);
Mockito.when(mockRepo.findCustomerById(101)).thenReturn("Mocked User");

CustomerService service = new CustomerService(mockRepo); // Inject Mock!
assertEquals("Mocked User", service.findCustomerById(101));
```

---

*This project is part of the Cognizant Java FSE Assignment -- Exercise 11.*
