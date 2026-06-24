# Exercise 6 - Proxy Pattern

## Objective

The **Proxy Pattern** is a structural design pattern that provides a **substitute or placeholder** for another object. The proxy controls access to the original object, allowing you to perform operations before or after the request reaches the real object.

**In simple words:**
> Imagine you have a personal assistant (proxy) who handles your calls. When someone calls you, the assistant first checks if it's important, then either connects the call or handles it themselves. You (the real object) are only disturbed when absolutely necessary. The Proxy Pattern works the same way -- it stands in front of the real object and controls when and how it is accessed.

---

## Problem Statement

### Why is the Proxy Pattern needed for image loading?

Loading images from a remote server is an **expensive operation**:

| Operation | Cost |
|-----------|------|
| Network connection | High (latency, bandwidth) |
| Download image data | High (depends on file size) |
| Store in memory | Medium (RAM consumption) |
| Display on screen | Low (fast operation) |

**Without Proxy:**
```java
// Image loads IMMEDIATELY from the server -- even if we
// never actually display it. Resources wasted!
Image img = new RealImage("sample.jpg");
// ... maybe we never call img.display()
```

**With Proxy:**
```java
// NO loading happens here -- just stores the filename (cheap)
Image img = new ProxyImage("sample.jpg");

// Loading happens ONLY when we actually need to display
img.display();  // First call: loads + displays
img.display();  // Second call: displays only (cached!)
```

**Benefits:**
- Loading is **deferred** until the image is actually needed
- Once loaded, the image is **cached** and never loaded again
- If `display()` is never called, the image is **never loaded** (zero cost)

---

## Design Pattern Used

**Proxy Pattern** (Structural Pattern)

- **Category:** Structural Design Pattern
- **Intent:** Provide a surrogate or placeholder for another object to control access to it.
- **Also Known As:** Surrogate
- **Source:** Gang of Four (GoF) Design Patterns

---

## Project Structure

```
Exercise-6-ProxyPattern/
|
|-- Image.java              <-- Subject Interface
|-- RealImage.java          <-- Real Subject (expensive)
|-- ProxyImage.java         <-- Proxy (lazy init + cache)
|-- ProxyPatternTest.java   <-- Client Code (test)
|-- README.md               <-- Documentation (this file)
```

---

## UML Class Diagram

```
         +----------------------+
         |    <<interface>>     |
         |       Image          |
         |----------------------|
         | + display(): void    |
         +----------+-----------+
                    | implements
           +--------+--------+
           |                 |
           v                 v
  +-----------------+   +------------------+
  |   RealImage     |   |   ProxyImage     |
  |-----------------|   |------------------|
  | - fileName      |   | - realImage      |
  |-----------------|   | - fileName       |
  | + display()     |   |------------------|
  +-----------------+   | + display()      |
           ^            +------------------+
           |                 |
           +----- creates ---+
                (lazy init)
```

### How the Proxy Controls Access

```
Client --> ProxyImage.display()
               |
               +-- Is realImage null?
               |
          YES  |              NO
               v               v
     Create RealImage     Skip creation
     (loads from server)  (use cached)
               |               |
               v               v
          realImage.display()
               |
               v
     "Displaying image: ..."
```

---

## Components

### Subject -- `Image`

The **Subject** interface defines the common contract (`display()`) shared by both the Real Subject and the Proxy. The client works exclusively with this interface.

| Role | Details |
|------|---------|
| Type | Interface |
| Method | `display()` |
| Purpose | Unified interface for real and proxy objects |

### Real Subject -- `RealImage`

The **Real Subject** is the actual object that performs the core work. Creating a RealImage triggers an expensive loading operation (simulated by a print statement).

| Role | Details |
|------|---------|
| Implements | `Image` |
| Constructor | Accepts filename, loads from remote server |
| display() | Prints "Displaying image: \<filename\>" |
| Cost | Expensive to create (remote loading) |

### Proxy -- `ProxyImage`

The **Proxy** stands in for the RealImage and controls access to it. It provides lazy initialization (defer creation) and caching (create only once).

| Role | Details |
|------|---------|
| Implements | `Image` |
| Contains | `private RealImage realImage` (initially null) |
| Constructor | Stores filename only (cheap -- no loading) |
| display() | Creates RealImage on first call, caches for reuse |

### Client -- `ProxyPatternTest`

The **Client** works with the `Image` interface. It creates a `ProxyImage` and calls `display()` multiple times to demonstrate lazy initialization and caching.

---

## How to Compile and Run

```bash
# Navigate to the project folder
cd Exercise-6-ProxyPattern

# Compile all Java files
javac *.java

# Run the test class
java ProxyPatternTest
```

---

## Expected Output

```
=============================================
      Proxy Pattern - Image Viewer            
=============================================

--- Creating ProxyImage (no loading yet) ---
ProxyImage created. Image NOT loaded yet.

--- First call to display() ---
Loading image from remote server: sample.jpg
Displaying image: sample.jpg

--- Second call to display() (cached) ---
Displaying image: sample.jpg

--- Another ProxyImage ---
ProxyImage created for wallpaper.png.

--- First call to display() ---
Loading image from remote server: wallpaper.png
Displaying image: wallpaper.png

--- Second call to display() (cached) ---
Displaying image: wallpaper.png

=============================================
      All images displayed successfully!      
=============================================
```

**Key observation:** "Loading image from remote server" appears only **once per image**, proving that the proxy caches the RealImage after the first load.

---

## Advantages of the Proxy Pattern

| Advantage | Explanation |
|-----------|-------------|
| **Lazy Initialization** | Expensive objects are created only when actually needed, not at construction time. Saves resources if the object is never used. |
| **Caching** | Once the real object is created, it is cached and reused. Avoids redundant expensive operations. |
| **Access Control** | The proxy can add permission checks, logging, or rate limiting before delegating to the real object. |
| **Transparency** | The client works with the `Image` interface and has no idea it's using a proxy. The proxy is a drop-in replacement. |
| **Separation of Concerns** | Resource management logic (lazy loading, caching) is in the proxy, keeping the RealImage class clean and focused. |
| **Open/Closed Principle** | New proxies (e.g., logging, security) can be added without modifying RealImage or client code. |

---

## Types of Proxy Pattern

| Type | Purpose | Example |
|------|---------|---------|
| **Virtual Proxy** | Delays creation of expensive objects until needed | Our `ProxyImage` (lazy loading) |
| **Protection Proxy** | Controls access based on permissions | Admin-only access to certain operations |
| **Remote Proxy** | Represents an object in a different JVM / server | Java RMI stubs |
| **Caching Proxy** | Caches results of expensive operations | Database query result caching |
| **Logging Proxy** | Logs all requests to the real object | Audit trail for sensitive operations |
| **Smart Reference** | Performs extra actions on access (e.g., reference counting) | Garbage collection helpers |

---

## Real-World Examples of the Proxy Pattern

| Example | Explanation |
|---------|-------------|
| **Java RMI (Remote Method Invocation)** | RMI stubs act as proxies for remote objects. The client calls methods on the stub (proxy), which forwards them over the network to the real object on another JVM. |
| **Hibernate Lazy Loading** | Hibernate uses proxy objects for entity relationships. Related entities are NOT loaded from the database until you actually access them -- classic virtual proxy. |
| **Spring AOP Proxies** | Spring creates proxy objects around beans to add cross-cutting concerns (transactions, security, logging) without modifying the actual bean code. |
| **Java `java.lang.reflect.Proxy`** | Java's built-in dynamic proxy creates proxy instances at runtime that implement specified interfaces and delegate calls to an `InvocationHandler`. |
| **CDN (Content Delivery Network)** | A CDN acts as a caching proxy for web content. It serves cached copies of images/pages instead of hitting the origin server every time. |
| **Browser Image Loading** | Browsers use placeholder proxies for images. They show a loading spinner (proxy) until the actual image data is downloaded (real object). |
| **Database Connection Pools** | Connection pool frameworks (HikariCP, C3P0) use proxy connections that manage the lifecycle of real database connections behind the scenes. |

---

## Difference Between Proxy and Other Similar Patterns

| Aspect | Proxy | Adapter | Decorator |
|--------|-------|---------|-----------|
| **Purpose** | Controls **access** to an object | Makes **incompatible interfaces** compatible | **Adds behavior** to an object |
| **Interface** | Same as real subject | Different (converts one to another) | Same as component |
| **Focus** | When/how the real object is accessed | Interface translation | Feature enhancement |
| **Creates Real Object?** | Yes (lazily) | No (wraps existing) | No (wraps existing) |
| **Example** | ProxyImage delays loading | PayPalAdapter converts API | SMSDecorator adds SMS |

---

## Viva Questions with Answers (Interview Preparation)

### Q1: What is the Proxy Pattern?
**A:** The Proxy Pattern is a structural design pattern that provides a surrogate or placeholder for another object to control access to it. The proxy implements the same interface as the real object, so the client can use either one interchangeably.

---

### Q2: What is lazy initialization?
**A:** Lazy initialization means delaying the creation of an object until it is actually needed. In our example, the `RealImage` is not created when `ProxyImage` is constructed -- it is created only when `display()` is called for the first time. This saves resources if the object is never used.

---

### Q3: How does caching work in the Proxy Pattern?
**A:** After the real object is created for the first time, the proxy stores (caches) a reference to it. On subsequent calls, the proxy reuses the cached object instead of creating a new one. In our example, `realImage` is set to `null` initially, created on the first `display()` call, and reused on all subsequent calls.

---

### Q4: What are the different types of proxies?
**A:** The main types are:
1. **Virtual Proxy** -- delays expensive object creation (our example)
2. **Protection Proxy** -- controls access based on permissions
3. **Remote Proxy** -- represents a remote object locally (Java RMI)
4. **Caching Proxy** -- caches expensive operation results
5. **Logging Proxy** -- logs requests before forwarding

---

### Q5: How is Proxy different from Decorator?
**A:** Both wrap an object with the same interface, but their **intent** differs:
- **Proxy** controls **access** (when/if the real object is used)
- **Decorator** adds **behavior** (enhances what the object does)

A proxy manages the real object's lifecycle; a decorator enhances its functionality.

---

### Q6: Why is the RealImage constructor expensive?
**A:** In our simulation, the constructor calls `loadFromRemoteServer()` which represents a costly operation like downloading image data over the network. In production, this could involve opening HTTP connections, transferring megabytes of data, and storing them in memory.

---

### Q7: What happens if display() is never called on ProxyImage?
**A:** The RealImage is **never created**. The proxy simply holds the filename string and nothing else. This is the key benefit of lazy initialization -- you pay zero cost for objects you never use.

---

### Q8: Can the Proxy Pattern be combined with other patterns?
**A:** Yes! Common combinations include:
- **Proxy + Factory** -- factory creates either real or proxy objects based on configuration
- **Proxy + Singleton** -- proxy ensures only one instance of the real object exists
- **Proxy + Observer** -- proxy notifies observers when the real object is accessed

---

### Q9: Give a real-world Java example of the Proxy Pattern.
**A:** **Hibernate Lazy Loading** is a classic example. When you load an `Employee` entity that has a `Department` relationship, Hibernate doesn't immediately load the Department from the database. Instead, it creates a proxy object. The actual database query runs only when you call `employee.getDepartment().getName()` -- lazy initialization via proxy.

---

### Q10: Is the Proxy Pattern the same as the Wrapper Pattern?
**A:** Not exactly. Both Proxy and Decorator (Wrapper) wrap an object, but:
- **Proxy** controls the **lifecycle** and **access** to the real object (it often creates the real object itself)
- **Decorator/Wrapper** enhances the **behavior** of an existing object (it receives the object to wrap)

The Proxy "owns" the real object; the Decorator "borrows" it.

---

*This project is part of the Cognizant Java FSE Assignment -- Exercise 6.*
