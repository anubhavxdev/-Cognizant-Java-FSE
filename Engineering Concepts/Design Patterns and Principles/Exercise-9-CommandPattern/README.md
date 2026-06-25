# Exercise 9 - Command Pattern

## Objective

The **Command Pattern** is a behavioral design pattern that turns a request into a standalone **object**. This object contains all the information needed to perform the action, including the method to call, the method's arguments, and the object that owns the method (the receiver).

**In simple words:**
> Think of ordering food at a restaurant. You don't walk into the kitchen and cook. Instead, you write your order on a slip (Command), give it to the waiter (Invoker), and the chef (Receiver) prepares the food. The order slip encapsulates your request as an object -- it can be queued, modified, or even cancelled before the chef sees it. That's the Command Pattern.

---

## Problem Statement

### Why do we need the Command Pattern for home automation?

A home automation system controls multiple devices (lights, fans, TVs, ACs) with various actions (on, off, dim, speed up). Without the Command Pattern:

**Without Command Pattern:**
```java
// The RemoteControl is tightly coupled to every device
public class RemoteControl {
    private Light light;
    private Fan fan;
    private TV tv;

    public void pressLightOn()  { light.turnOn(); }
    public void pressLightOff() { light.turnOff(); }
    public void pressFanOn()    { fan.switchOn(); }
    public void pressFanOff()   { fan.switchOff(); }
    public void pressTVOn()     { tv.powerOn(); }
    // Adding a new device = modifying RemoteControl!
}
```

**Problems:**
- RemoteControl knows about **every device** (tight coupling)
- Adding new devices requires **modifying** RemoteControl
- Cannot store, queue, or undo commands
- Violates Open/Closed and Single Responsibility Principles

**With Command Pattern:**
```java
// RemoteControl knows NOTHING about devices!
remote.setCommand(new LightOnCommand(light));
remote.pressButton();  // Light turns ON

remote.setCommand(new FanOnCommand(fan));
remote.pressButton();  // Fan turns ON
// Adding a new device = new command class. Zero changes to Remote!
```

---

## Design Pattern Used

**Command Pattern** (Behavioral Pattern)

- **Category:** Behavioral Design Pattern
- **Intent:** Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations.
- **Also Known As:** Action, Transaction
- **Source:** Gang of Four (GoF) Design Patterns

---

## Project Structure

```
Exercise-9-CommandPattern/
|
|-- Command.java             <-- Command Interface
|-- Light.java               <-- Receiver
|-- LightOnCommand.java      <-- Concrete Command (ON)
|-- LightOffCommand.java     <-- Concrete Command (OFF)
|-- RemoteControl.java       <-- Invoker
|-- CommandPatternTest.java   <-- Client Code (test)
|-- README.md                 <-- Documentation (this file)
```

---

## UML Class Diagram

```
+---------------------+         +------------------+
|   RemoteControl     |         |  <<interface>>   |
|   (Invoker)         |         |    Command       |
|---------------------|         |------------------|
| - command: Command  |-------->| + execute()      |
|---------------------|         +--------+---------+
| + setCommand()      |                  |
| + pressButton()     |             implements
+---------------------+          +------+------+
                                 |             |
                                 v             v
                        +----------------+ +----------------+
                        | LightOnCommand | | LightOffCommand|
                        |----------------| |----------------|
                        | - light: Light | | - light: Light |
                        |----------------| |----------------|
                        | + execute()    | | + execute()    |
                        +-------+--------+ +-------+--------+
                                |                  |
                            delegates          delegates
                                |                  |
                                v                  v
                        +---------------------------+
                        |          Light             |
                        |       (Receiver)           |
                        |---------------------------|
                        | + turnOn()                 |
                        | + turnOff()                |
                        +---------------------------+
```

---

## Command Flow Explanation

This is the step-by-step flow showing how a request travels from the Client to the Receiver:

```
+--------+      +--------------+      +----------------+      +---------+
| Client |      | RemoteControl|      | LightOnCommand |      |  Light  |
| (Test) |      |  (Invoker)   |      |   (Command)    |      |(Receiver|
+---+----+      +------+-------+      +-------+--------+      +----+----+
    |                  |                       |                     |
    | 1. setCommand(lightOn)                   |                     |
    |----------------->|                       |                     |
    |                  | stores command ref     |                     |
    |                  |                       |                     |
    | 2. pressButton() |                       |                     |
    |----------------->|                       |                     |
    |                  | 3. command.execute()   |                     |
    |                  |---------------------->|                     |
    |                  |                       | 4. light.turnOn()   |
    |                  |                       |-------------------->|
    |                  |                       |                     |
    |                  |                       |          "Light is ON"
    |                  |                       |                     |
```

### Flow Summary:

| Step | Who | Does What |
|------|-----|-----------|
| 1 | **Client** | Creates Receiver, Commands, and assigns a Command to the Invoker |
| 2 | **Client** | Calls `pressButton()` on the Invoker |
| 3 | **Invoker** | Calls `execute()` on the stored Command (doesn't know what it does) |
| 4 | **Command** | Calls the specific method on the Receiver (`light.turnOn()`) |
| 5 | **Receiver** | Performs the actual work ("Light is ON") |

### Key Insight:
The **Invoker** (RemoteControl) and **Receiver** (Light) never communicate directly. The **Command** object is the only link between them. This is complete **decoupling**.

---

## Components

### Command Interface -- `Command`

Declares the `execute()` method that all commands must implement.

| Role | Details |
|------|---------|
| Type | Interface |
| Method | `execute()` |
| Purpose | Common contract for all command objects |

### Receiver -- `Light`

The object that **actually performs** the work. It contains the real business logic.

| Method | Action |
|--------|--------|
| `turnOn()` | Prints "Light is ON" |
| `turnOff()` | Prints "Light is OFF" |

### Concrete Commands -- `LightOnCommand` & `LightOffCommand`

Each command binds a specific action to a specific receiver.

| Command | Receiver | Action | execute() calls |
|---------|----------|--------|-----------------|
| `LightOnCommand` | `Light` | Turn ON | `light.turnOn()` |
| `LightOffCommand` | `Light` | Turn OFF | `light.turnOff()` |

### Invoker -- `RemoteControl`

Triggers commands without knowing what they do or who receives them.

| Method | Purpose |
|--------|---------|
| `setCommand(command)` | Assigns a command to the button |
| `pressButton()` | Calls `execute()` on the assigned command |

---

## How to Compile and Run

```bash
# Navigate to the project folder
cd Exercise-9-CommandPattern

# Compile all Java files
javac *.java

# Run the test class
java CommandPatternTest
```

---

## Expected Output

```
=============================================
   Command Pattern - Home Automation System   
=============================================

--- Pressing Button: Light ON ---
Light is ON

--- Pressing Button: Light OFF ---
Light is OFF

--- Replaying Commands ---
Light is ON
Light is OFF

=============================================
   Home automation executed successfully!     
=============================================
```

---

## Advantages of the Command Pattern

| Advantage | Explanation |
|-----------|-------------|
| **Decoupling** | The Invoker and Receiver are completely independent. The RemoteControl doesn't know about the Light, and vice versa. |
| **Open/Closed Principle** | New commands and devices can be added without modifying existing code. Just create new Command classes. |
| **Undo/Redo Support** | Commands can store previous state and implement an `undo()` method to reverse their action. |
| **Command Queuing** | Commands are objects -- they can be stored in a queue and executed later (batch processing, job scheduling). |
| **Logging & Auditing** | Commands can be logged before execution, creating an audit trail of all actions. |
| **Macro Commands** | Multiple commands can be grouped into a single "macro" command that executes them all in sequence. |
| **Single Responsibility** | Each command class encapsulates exactly one action. Clean, focused, easy to test. |

## Disadvantages

| Disadvantage | Explanation |
|-------------|-------------|
| **More Classes** | Each action requires a separate command class, increasing the number of files. |
| **Complexity** | The indirection (Client -> Invoker -> Command -> Receiver) adds complexity for simple scenarios. |
| **Overkill for Simple Cases** | If you only have a couple of actions that never change, a direct method call is simpler. |

---

## Command Pattern vs Strategy Pattern

| Aspect | Command Pattern | Strategy Pattern |
|--------|----------------|------------------|
| **Purpose** | Encapsulates a **request/action** as an object | Encapsulates an **algorithm** as an object |
| **Focus** | WHAT to do (action) | HOW to do it (algorithm) |
| **Participants** | Invoker, Command, Receiver | Context, Strategy |
| **Decouples** | Invoker from Receiver | Context from Algorithm |
| **Supports** | Undo, queue, log, replay | Runtime algorithm switching |
| **Example** | "Turn light ON" command | "Pay via Credit Card" strategy |
| **Typical Use** | GUI buttons, menu actions, macros | Sorting, payment, routing |

---

## Real-World Examples of the Command Pattern

| Example | Explanation |
|---------|-------------|
| **Java Swing `ActionListener`** | `button.addActionListener(e -> doSomething())` -- each listener is a command that executes when the button is clicked. The button (invoker) doesn't know what happens. |
| **`Runnable` Interface in Java** | `new Thread(new MyRunnable()).start()` -- `Runnable` is a command interface with a single `run()` method. The Thread (invoker) executes whatever command is passed. |
| **Undo/Redo in Text Editors** | Every editing action (type, delete, paste) is a command object. The editor stores a history stack of commands and can undo/redo them. |
| **Database Transactions** | SQL statements can be treated as commands that are queued, executed in order, and rolled back (undo) if something fails. |
| **Job Schedulers (Quartz, Cron)** | Scheduled jobs are command objects that are queued and executed at specific times. |
| **Home Automation (Alexa, Google Home)** | Voice commands are translated into command objects that control smart devices -- exactly like our example! |
| **Git Version Control** | Each commit is a command that records changes. Git can revert (undo), cherry-pick (replay), and rebase (reorder) commits. |
| **Macro Recording** | Recording a macro in Excel or an IDE captures a sequence of commands that can be replayed later. |

---

## Viva Questions with Answers (Interview Preparation)

### Q1: What is the Command Pattern?
**A:** The Command Pattern is a behavioral design pattern that encapsulates a request as a standalone object containing all information needed to perform the action. This allows requests to be parameterized, queued, logged, and undone.

---

### Q2: What are the four participants in the Command Pattern?
**A:**
1. **Command (Interface)** -- declares `execute()` (`Command.java`)
2. **Concrete Command** -- binds an action to a receiver (`LightOnCommand`)
3. **Receiver** -- performs the actual work (`Light`)
4. **Invoker** -- triggers the command (`RemoteControl`)
5. **Client** -- creates and configures everything (`CommandPatternTest`)

---

### Q3: How does the Command Pattern achieve decoupling?
**A:** The Invoker (RemoteControl) only knows the `Command` interface -- it calls `execute()` without knowing what device is being controlled or what action is performed. The Receiver (Light) has no knowledge of the Command or Invoker. The Command object is the only link, and both sides are independent.

---

### Q4: How would you implement Undo functionality?
**A:** Add an `undo()` method to the Command interface. Each concrete command stores the previous state and reverses its action:
```java
public interface Command {
    void execute();
    void undo();
}

public class LightOnCommand implements Command {
    public void execute() { light.turnOn(); }
    public void undo() { light.turnOff(); }   // Reverse!
}
```
The Invoker stores the last executed command and calls `undo()` when needed.

---

### Q5: What is the difference between Command and Strategy patterns?
**A:** Command encapsulates a **request/action** (what to do), while Strategy encapsulates an **algorithm** (how to do something). Command supports undo, queuing, and logging; Strategy supports runtime algorithm switching. Command decouples the invoker from the receiver; Strategy decouples the context from the algorithm.

---

### Q6: How is `Runnable` an example of the Command Pattern?
**A:** `Runnable` is a command interface with a single method `run()`. The `Thread` class is the invoker that calls `run()`. The actual work (receiver logic) is implemented inside the `Runnable`:
```java
Runnable command = () -> System.out.println("Task executed");
Thread invoker = new Thread(command);
invoker.start();  // Invoker triggers the command
```

---

### Q7: Can one Invoker hold multiple commands?
**A:** Yes! A real remote control would have multiple buttons, each with its own command. You could use an array or map:
```java
Command[] buttons = new Command[10];
buttons[0] = new LightOnCommand(light);
buttons[1] = new FanOnCommand(fan);
```

---

### Q8: What is a Macro Command?
**A:** A Macro Command is a composite command that executes multiple commands in sequence. It implements `Command` and holds a list of sub-commands:
```java
public class MacroCommand implements Command {
    private List<Command> commands;
    public void execute() {
        for (Command cmd : commands) cmd.execute();
    }
}
```

---

### Q9: How does the Command Pattern support logging?
**A:** Since commands are objects, they can be serialized and stored in a log file before execution. This creates an audit trail. If the system crashes, the log can be replayed to restore state.

---

### Q10: When should you NOT use the Command Pattern?
**A:** Don't use it when:
- Actions are simple and direct (a plain method call suffices)
- You don't need undo, queuing, logging, or macro capabilities
- Adding the extra classes (Command, ConcreteCommand) doesn't justify the flexibility gained
- The system has very few, static actions that won't change

---

*This project is part of the Cognizant Java FSE Assignment -- Exercise 9.*
