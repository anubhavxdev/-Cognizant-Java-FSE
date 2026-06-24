# Exercise 2 - Factory Method Pattern

## Objective

The **Factory Method Pattern** is a creational design pattern that provides an interface for creating objects in a superclass, but allows subclasses to alter the type of objects that will be created.

**In simple words:**
> Instead of creating objects directly using `new`, we delegate the creation to special "factory" classes. Each factory knows how to create a specific type of object. The client code only talks to the factory and the product interface — it never needs to know the exact class being created.

**Think of it like a real-world factory:**
A car factory produces cars. You don't build the car yourself — you tell the factory what you want, and it builds it for you. Different factories (Toyota, Honda, BMW) produce different cars, but they all give you a "Car" that you can drive.

---

## Design Pattern Used

**Factory Method Pattern** (Creational Pattern)

- **Category:** Creational Design Pattern
- **Intent:** Define an interface for creating an object, but let subclasses decide which class to instantiate.
- **Also Known As:** Virtual Constructor
- **Source:** Gang of Four (GoF) Design Patterns

---

## Project Structure

```
Exercise-2-FactoryMethodPattern/
│
├── Document.java                 ← Product Interface
├── WordDocument.java             ← Concrete Product
├── PdfDocument.java              ← Concrete Product
├── ExcelDocument.java            ← Concrete Product
├── DocumentFactory.java          ← Abstract Creator (Factory)
├── WordDocumentFactory.java      ← Concrete Creator (Factory)
├── PdfDocumentFactory.java       ← Concrete Creator (Factory)
├── ExcelDocumentFactory.java     ← Concrete Creator (Factory)
├── FactoryPatternTest.java       ← Client Code (Test)
└── README.md                     ← Documentation
```

---

## UML Class Diagram

```
        ┌─────────────────────┐
        │   <<interface>>     │
        │     Document        │
        │─────────────────────│
        │ + open(): void      │
        └─────────┬───────────┘
                  │ implements
       ┌──────────┼──────────┐
       │          │          │
       ▼          ▼          ▼
┌──────────┐ ┌──────────┐ ┌──────────┐
│   Word   │ │   Pdf    │ │  Excel   │
│ Document │ │ Document │ │ Document │
└──────────┘ └──────────┘ └──────────┘
       ▲          ▲          ▲
       │ creates  │ creates  │ creates
       │          │          │
┌──────────┐ ┌──────────┐ ┌──────────┐
│   Word   │ │   Pdf    │ │  Excel   │
│ Document │ │ Document │ │ Document │
│ Factory  │ │ Factory  │ │ Factory  │
└────┬─────┘ └────┬─────┘ └────┬─────┘
     │             │             │
     └─────────────┼─────────────┘
                   │ extends
        ┌──────────┴──────────┐
        │  <<abstract>>       │
        │  DocumentFactory    │
        │─────────────────────│
        │ + createDocument()  │
        │   : Document        │
        └─────────────────────┘
```

---

## Files Description

| File | Role | Description |
|------|------|-------------|
| `Document.java` | **Product Interface** | Defines the common contract (`open()` method) that all document types must implement. |
| `WordDocument.java` | **Concrete Product** | Implements `Document` interface. Prints "Opening Word Document..." when `open()` is called. |
| `PdfDocument.java` | **Concrete Product** | Implements `Document` interface. Prints "Opening PDF Document..." when `open()` is called. |
| `ExcelDocument.java` | **Concrete Product** | Implements `Document` interface. Prints "Opening Excel Document..." when `open()` is called. |
| `DocumentFactory.java` | **Abstract Creator** | Abstract class with the factory method `createDocument()`. Subclasses must override this method. |
| `WordDocumentFactory.java` | **Concrete Creator** | Extends `DocumentFactory`. Creates and returns a `WordDocument` object. |
| `PdfDocumentFactory.java` | **Concrete Creator** | Extends `DocumentFactory`. Creates and returns a `PdfDocument` object. |
| `ExcelDocumentFactory.java` | **Concrete Creator** | Extends `DocumentFactory`. Creates and returns an `ExcelDocument` object. |
| `FactoryPatternTest.java` | **Client Code** | Demonstrates the pattern by creating documents through their respective factories and calling `open()`. |
| `README.md` | **Documentation** | This file — explains the pattern, structure, and usage. |

---

## How to Compile and Run

```bash
# Navigate to the project folder
cd Exercise-2-FactoryMethodPattern

# Compile all Java files
javac *.java

# Run the test class
java FactoryPatternTest
```

---

## Expected Output

```
==============================================
 Factory Method Pattern - Document Management 
==============================================

Opening Word Document...
Opening PDF Document...
Opening Excel Document...

==============================================
 All documents created and opened successfully!
==============================================
```

---

## Benefits of Factory Method Pattern

| Benefit | Explanation |
|---------|-------------|
| **Loose Coupling** | Client code depends on abstractions (`Document`, `DocumentFactory`), not on concrete classes (`WordDocument`, `PdfDocument`). This makes the code flexible and easy to change. |
| **Extensibility** | Adding a new document type (e.g., `PowerPointDocument`) requires only creating a new concrete product and a new concrete factory — **no existing code needs to be modified**. This follows the **Open/Closed Principle**. |
| **Better Maintainability** | Each factory is responsible for creating only one type of product. This **Single Responsibility Principle** makes the code easier to understand, test, and maintain. |
| **Encapsulation of Creation Logic** | The complex object creation logic is hidden inside the factory classes. The client code remains clean and simple. |
| **Follows SOLID Principles** | The pattern naturally supports the Open/Closed Principle (O) and the Dependency Inversion Principle (D) from SOLID. |

---

## Why Factory Method Pattern is Used

The Factory Method Pattern is used when:

1. **You don't know the exact type of object to create at compile time.** The decision is deferred to subclasses.

2. **You want to decouple object creation from object usage.** The client code should not be responsible for knowing how to create specific objects.

3. **You want to follow the Open/Closed Principle.** New product types can be added without modifying existing factory or client code.

4. **You want to centralize creation logic.** If object creation involves complex setup, validation, or configuration, the factory encapsulates all of that.

---

## Difference Between Simple Factory and Factory Method

| Aspect | Simple Factory | Factory Method |
|--------|---------------|----------------|
| **Structure** | A single factory class with a method (often using `if-else` or `switch`) to create objects. | An abstract factory class with concrete factory subclasses, each creating a specific product. |
| **Design Pattern?** | Not a formal GoF design pattern — it's a programming idiom. | A formal GoF creational design pattern. |
| **Extensibility** | Adding a new product requires **modifying** the existing factory class (violates Open/Closed Principle). | Adding a new product requires only **adding** a new factory subclass (follows Open/Closed Principle). |
| **Coupling** | Higher coupling — the single factory knows about all product types. | Lower coupling — each factory only knows about its own product. |
| **Flexibility** | Less flexible; hard to extend without code changes. | Highly flexible; easily extensible via new subclasses. |
| **Example** | `DocumentFactory.create("word")` with a switch inside. | `new WordDocumentFactory().createDocument()` — each factory is a separate class. |

---

## Real-World Examples of Factory Method Pattern

| Example | Explanation |
|---------|-------------|
| **Java `Collection.iterator()`** | Different collections (`ArrayList`, `HashSet`, `LinkedList`) each provide their own `Iterator` implementation through the factory method `iterator()`. |
| **Java `NumberFormat.getInstance()`** | Returns different `NumberFormat` subclass instances based on the locale — the client doesn't know which concrete class is used. |
| **JDBC `DriverManager.getConnection()`** | Different database drivers (MySQL, PostgreSQL, Oracle) provide their own `Connection` implementations. The client code only works with the `Connection` interface. |
| **Logging Frameworks (Log4j, SLF4J)** | `LoggerFactory.getLogger()` creates logger instances based on configuration. Different factories produce different logging backends. |
| **Document Editors (Microsoft Office, Google Docs)** | A document editor application can use factory methods to create different document types (text, spreadsheet, presentation) based on user selection. |
| **GUI Frameworks (Swing, JavaFX)** | UI toolkit factories create platform-specific components (buttons, text fields) — the application code works with abstract component interfaces. |

---

## Key Terminology

| Term | Meaning |
|------|---------|
| **Product** | The interface or abstract class defining the type of object the factory creates (`Document`). |
| **Concrete Product** | A specific implementation of the product (`WordDocument`, `PdfDocument`, `ExcelDocument`). |
| **Creator (Factory)** | The abstract class that declares the factory method (`DocumentFactory`). |
| **Concrete Creator** | A subclass that implements the factory method to create a specific product (`WordDocumentFactory`). |
| **Client** | The code that uses the factory to create products without knowing the concrete classes (`FactoryPatternTest`). |

---

*This project is part of the Cognizant Java FSE Assignment — Exercise 2.*
