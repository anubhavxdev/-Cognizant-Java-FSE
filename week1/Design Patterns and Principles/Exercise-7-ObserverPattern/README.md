# Exercise 7 - Observer Pattern

## Objective

The **Observer Pattern** is a behavioral design pattern that defines a **one-to-many dependency** between objects. When one object (the Subject) changes its state, all its dependents (Observers) are automatically notified and updated.

**In simple words:**
> Think of a YouTube channel. When a creator uploads a new video, ALL subscribers get a notification automatically. The creator doesn't need to message each subscriber individually -- YouTube handles the broadcasting. The Observer Pattern works the same way: the Subject broadcasts changes, and all registered Observers receive the update.

---

## Problem Statement

### Why do we need the Observer Pattern for stock monitoring?

In a stock market application, **multiple clients** (mobile apps, web dashboards, email alerts) need to receive **real-time updates** when stock prices change.

**Without Observer Pattern:**

```java
// Polling approach -- each client checks for changes repeatedly
while (true) {
    double price = stockMarket.getPrice("AAPL");
    if (price != lastKnownPrice) {
        updateDisplay(price);      // Wasteful!
        lastKnownPrice = price;    // CPU-intensive!
    }
    Thread.sleep(1000);            // Network-heavy!
}
```

**Problems with polling:**
- Wastes CPU and network resources checking repeatedly
- Delays between polls mean missed real-time updates
- Each client must implement its own polling logic
- Tight coupling between clients and the data source
- Doesn't scale with many clients

**With Observer Pattern:**

```java
// Event-driven approach -- clients are PUSHED updates
stockMarket.registerObserver(mobileApp);
stockMarket.registerObserver(webApp);

// When price changes, ALL observers are notified instantly
stockMarket.setStockPrice("AAPL", 200.5);
// mobileApp.update() called automatically
// webApp.update() called automatically
```

**Benefits:**
- No wasted resources -- updates happen only when data changes
- Instant notifications -- zero delay
- Loose coupling -- Subject and Observers are independent
- Scales easily -- add new observers without modifying existing code

---

## Design Pattern Used

**Observer Pattern** (Behavioral Pattern)

- **Category:** Behavioral Design Pattern
- **Intent:** Define a one-to-many dependency so that when one object changes state, all its dependents are notified and updated automatically.
- **Also Known As:** Publish-Subscribe (Pub-Sub), Event-Listener, Dependents
- **Source:** Gang of Four (GoF) Design Patterns

---

## Project Structure

```
Exercise-7-ObserverPattern/
|
|-- Stock.java               <-- Subject Interface
|-- Observer.java             <-- Observer Interface
|-- StockMarket.java          <-- Concrete Subject
|-- MobileApp.java            <-- Concrete Observer
|-- WebApp.java               <-- Concrete Observer
|-- ObserverPatternTest.java  <-- Client Code (test)
|-- README.md                 <-- Documentation (this file)
```

---

## UML Class Diagram

```
    +------------------------+         +---------------------+
    |    <<interface>>       |         |   <<interface>>     |
    |       Stock            |         |     Observer        |
    |------------------------|         |---------------------|
    | + registerObserver()   |         | + update(stockName, |
    | + removeObserver()     |         |          price)     |
    | + notifyObservers()    |         +----------+----------+
    +-----------+------------+                    |
                | implements                      | implements
                v                          +------+------+
    +------------------------+             |             |
    |     StockMarket        |             v             v
    |------------------------|    +--------------+ +-------------+
    | - observers: List      |    |  MobileApp   | |   WebApp    |
    | - stockName: String    |    |--------------| |-------------|
    | - price: double        |    | + update()   | | + update()  |
    |------------------------|    +--------------+ +-------------+
    | + registerObserver()   |
    | + removeObserver()     |
    | + notifyObservers()    |
    | + setStockPrice()      |
    +------------------------+
```

---

## Observer Workflow Diagram

This diagram shows the step-by-step flow of events when a stock price changes:

```
  +------------------+          +------------------+
  |    Client Code   |          |   StockMarket    |
  | (ObserverPattern |          |   (Subject)      |
  |     Test)        |          +--------+---------+
  +--------+---------+                   |
           |                             |
  Step 1:  | registerObserver(mobileApp) |
           |---------------------------->|
           |                             | Adds to observer list
           |                             |
  Step 2:  | registerObserver(webApp)    |
           |---------------------------->|
           |                             | Adds to observer list
           |                             |
  Step 3:  | setStockPrice("AAPL",200.5) |
           |---------------------------->|
           |                             |
           |                    +--------+---------+
           |                    | notifyObservers()|
           |                    +--------+---------+
           |                             |
           |              +--------------+---------------+
           |              |                              |
           |              v                              v
           |   +-------------------+          +-------------------+
           |   |    MobileApp      |          |     WebApp        |
           |   |-------------------|          |-------------------|
           |   | update("AAPL",    |          | update("AAPL",    |
           |   |        200.5)     |          |        200.5)     |
           |   | Prints:           |          | Prints:           |
           |   | "Mobile App:     |          | "Web App:         |
           |   |  AAPL updated    |          |  AAPL updated     |
           |   |  to 200.5"       |          |  to 200.5"        |
           |   +-------------------+          +-------------------+
```

### Sequence of Events:

| Step | Action | Who Does It | What Happens |
|------|--------|-------------|--------------|
| 1 | `registerObserver(mobileApp)` | Client | MobileApp is added to the observer list |
| 2 | `registerObserver(webApp)` | Client | WebApp is added to the observer list |
| 3 | `setStockPrice("AAPL", 200.5)` | Client | Stock price is updated internally |
| 4 | `notifyObservers()` | StockMarket (auto) | Iterates through all registered observers |
| 5 | `mobileApp.update("AAPL", 200.5)` | StockMarket | MobileApp receives and displays the update |
| 6 | `webApp.update("AAPL", 200.5)` | StockMarket | WebApp receives and displays the update |

---

## Components

### Subject Interface -- `Stock`

Defines the contract for managing observers and broadcasting notifications.

| Method | Purpose |
|--------|---------|
| `registerObserver(Observer)` | Add an observer to the notification list |
| `removeObserver(Observer)` | Remove an observer from the notification list |
| `notifyObservers()` | Notify all registered observers of a change |

### Observer Interface -- `Observer`

Defines the callback method that observers must implement.

| Method | Purpose |
|--------|---------|
| `update(String stockName, double price)` | Receive stock price update from the Subject |

### Concrete Subject -- `StockMarket`

The data source that maintains a list of observers and broadcasts changes.

| Field/Method | Purpose |
|--------------|---------|
| `List<Observer> observers` | Stores all registered observers |
| `String stockName` | Current stock name |
| `double price` | Current stock price |
| `setStockPrice(name, price)` | Updates data and triggers `notifyObservers()` |

### Concrete Observers -- `MobileApp` & `WebApp`

Clients that receive and react to stock price updates.

| Observer | Behavior on Update |
|----------|-------------------|
| `MobileApp` | Prints "Mobile App: \<stock\> updated to \<price\>" |
| `WebApp` | Prints "Web App: \<stock\> updated to \<price\>" |

---

## How to Compile and Run

```bash
# Navigate to the project folder
cd Exercise-7-ObserverPattern

# Compile all Java files
javac *.java

# Run the test class
java ObserverPatternTest
```

---

## Expected Output

```
=============================================
   Observer Pattern - Stock Market Monitor    
=============================================

--- Registering Observers ---
MobileApp registered.
WebApp registered.

--- Stock Price Change 1 ---
StockMarket: AAPL price changed to 200.5
Mobile App: AAPL updated to 200.5
Web App: AAPL updated to 200.5

--- Stock Price Change 2 ---
StockMarket: GOOGL price changed to 2800.75
Mobile App: GOOGL updated to 2800.75
Web App: GOOGL updated to 2800.75

--- Removing WebApp Observer ---
WebApp removed from observers.

--- Stock Price Change 3 (WebApp removed) ---
StockMarket: AAPL price changed to 210.0
Mobile App: AAPL updated to 210.0

=============================================
   Stock monitoring completed successfully!   
=============================================
```

**Key observations:**
- In Changes 1 and 2, **both** MobileApp and WebApp are notified
- After WebApp is removed, Change 3 only notifies **MobileApp**
- This proves dynamic subscribe/unsubscribe works correctly

---

## Advantages of the Observer Pattern

| Advantage | Explanation |
|-----------|-------------|
| **Loose Coupling** | The Subject knows observers only through the `Observer` interface. It doesn't know their concrete classes. |
| **Open/Closed Principle** | New observers can be added without modifying the Subject or existing observers. |
| **Dynamic Relationships** | Observers can be added or removed at runtime, changing the notification behavior dynamically. |
| **Broadcast Communication** | One event triggers notifications to ALL subscribers automatically -- no manual iteration in client code. |
| **Event-Driven Architecture** | Enables reactive programming where actions happen in response to events, not polling. |

---

## Disadvantages of the Observer Pattern

| Disadvantage | Explanation |
|-------------|-------------|
| **Unexpected Updates** | Observers may receive updates they don't expect or at inconvenient times, leading to unwanted side effects. |
| **Memory Leaks** | If observers are not properly unregistered, they continue to be referenced by the Subject and cannot be garbage collected. |
| **Order of Notification** | The order in which observers are notified is not guaranteed (depends on the list implementation). |
| **Performance** | Notifying many observers can be slow if update logic is complex or if there are thousands of subscribers. |
| **Debugging Difficulty** | It can be hard to trace the flow of notifications through multiple observers, especially in large systems. |
| **Cascade Updates** | An observer's update might trigger another state change, causing cascading notifications that are hard to predict. |

---

## Real-World Examples of the Observer Pattern

| Example | Explanation |
|---------|-------------|
| **Java Swing Event Listeners** | `button.addActionListener(listener)` -- UI components notify listeners when events occur (clicks, key presses). Classic Observer in Java GUI. |
| **JavaScript DOM Events** | `element.addEventListener("click", handler)` -- the DOM element is the Subject, and event handlers are Observers. |
| **Java `PropertyChangeListener`** | JavaBeans use `PropertyChangeSupport` to notify listeners when a bean property changes -- built-in Observer implementation. |
| **Spring ApplicationEvent** | Spring's `ApplicationEventPublisher` broadcasts events to all registered `@EventListener` beans -- enterprise Observer pattern. |
| **Message Queues (Kafka, RabbitMQ)** | Producers publish messages to topics/queues, and consumers (subscribers) receive them -- distributed Observer pattern. |
| **Social Media Notifications** | When a user posts content, all followers receive notifications -- the user is the Subject, followers are Observers. |
| **Stock Market Tickers** | Real stock market apps like Bloomberg Terminal use Observer to push real-time price updates to all connected clients. |
| **MVC Architecture** | In Model-View-Controller, the Model is the Subject and Views are Observers. When the Model changes, all Views are updated automatically. |

---

## Push vs Pull Model

| Aspect | Push Model (Our Implementation) | Pull Model |
|--------|--------------------------------|------------|
| **Data Flow** | Subject PUSHES data to Observer via `update(name, price)` | Subject notifies Observer, Observer PULLS data via getter methods |
| **Observer Method** | `update(String stockName, double price)` | `update(Stock subject)` -- then calls `subject.getPrice()` |
| **Simplicity** | Simpler -- data is delivered directly | More complex -- Observer must know how to query the Subject |
| **Flexibility** | Less flexible -- all observers get same data | More flexible -- each observer queries only what it needs |
| **Common Usage** | Small data, simple notifications | Large/complex data where observers need different subsets |

---

## Viva Questions with Answers (Interview Preparation)

### Q1: What is the Observer Pattern?
**A:** The Observer Pattern is a behavioral design pattern that defines a one-to-many dependency between objects. When the Subject (one) changes state, all its registered Observers (many) are notified and updated automatically. It enables event-driven, reactive communication.

---

### Q2: What are the key components of the Observer Pattern?
**A:** The four key components are:
1. **Subject Interface** (`Stock`) -- declares register, remove, and notify methods
2. **Concrete Subject** (`StockMarket`) -- stores data, maintains observer list, broadcasts changes
3. **Observer Interface** (`Observer`) -- declares the `update()` callback method
4. **Concrete Observer** (`MobileApp`, `WebApp`) -- implements update() to react to changes

---

### Q3: What is the difference between Push and Pull models in Observer Pattern?
**A:** In the **Push model**, the Subject sends data directly to Observers via the update method parameters (e.g., `update(stockName, price)`). In the **Pull model**, the Subject only notifies Observers that something changed, and Observers query the Subject for the specific data they need. Push is simpler; Pull is more flexible.

---

### Q4: How does the Observer Pattern achieve loose coupling?
**A:** The Subject only knows its Observers through the `Observer` interface. It doesn't know their concrete classes (MobileApp, WebApp), what they do with the data, or any implementation details. Similarly, Observers only know the Subject through the `Stock` interface. This means both sides can change independently.

---

### Q5: What happens if an Observer is not removed after it's no longer needed?
**A:** This causes a **memory leak**. The Subject holds a reference to the Observer in its list, preventing the garbage collector from reclaiming the Observer's memory. The Observer also continues to receive unwanted notifications. Always call `removeObserver()` when an Observer is no longer needed.

---

### Q6: How is the Observer Pattern used in Java's built-in APIs?
**A:** Java has several built-in Observer implementations:
- `java.util.EventListener` and Swing's `ActionListener`
- `java.beans.PropertyChangeListener` and `PropertyChangeSupport`
- `java.util.Observable` (deprecated since Java 9)
- JavaFX's `ObservableValue` and `ChangeListener`
- `java.util.concurrent.Flow` (Java 9+ reactive streams)

---

### Q7: What is the difference between Observer Pattern and Pub-Sub?
**A:** In the **Observer Pattern**, Observers register directly with the Subject -- they have a direct reference. In **Pub-Sub** (Publish-Subscribe), there is a **message broker** (middleware) between publishers and subscribers -- they don't know about each other. Pub-Sub is more decoupled and is used in distributed systems (Kafka, RabbitMQ).

---

### Q8: Can one Observer subscribe to multiple Subjects?
**A:** Yes! An Observer can register with multiple Subjects. For example, a `MobileApp` could subscribe to multiple `StockMarket` instances tracking different exchanges. The Observer would need to identify which Subject sent the update (e.g., via the stockName parameter).

---

### Q9: How does the Observer Pattern relate to MVC architecture?
**A:** In MVC:
- The **Model** is the Subject -- it holds the data
- The **View** is the Observer -- it displays the data
- When the Model changes, it notifies all Views, which update their display automatically

This is exactly the Observer Pattern! MVC is one of the most common applications of Observer.

---

### Q10: What are the disadvantages of the Observer Pattern?
**A:** Key disadvantages include:
1. **Memory leaks** if observers are not properly unregistered
2. **Cascade updates** where one update triggers another, causing unpredictable chains
3. **Performance issues** with many observers or complex update logic
4. **Debugging difficulty** since notification flow is implicit, not explicit in the call stack
5. **Thread safety** concerns in multi-threaded environments

---

*This project is part of the Cognizant Java FSE Assignment -- Exercise 7.*
