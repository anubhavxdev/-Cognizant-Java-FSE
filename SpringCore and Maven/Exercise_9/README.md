# Exercise 9: Spring Boot CRUD Application

## Objective
Create a standalone Spring Boot 2.x application demonstrating automatic configuration, H2 database connection pools, Spring Data JPA repositories, and basic REST CRUD controller actions.

---

## Folder Structure
```text
Exercise_9/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/library/
│   │   │       ├── Book.java
│   │   │       ├── BookRepository.java
│   │   │       ├── BookController.java
│   │   │       └── LibraryManagementApplication.java
│   │   └── resources/
│   │          application.properties
└── pom.xml
```

---

## Files Used

### 1. Book.java
* **Purpose**: JPA Database Entity representing the book schema.
* **Complete Code**: See [Book.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_9/src/main/java/com/library/Book.java).
* **Line-by-Line Explanation**:
  - `@Entity`: Marks class for database mapper bindings.
  - `@Id`: Designates the primary key.
  - `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Relies on H2 auto-increment sequences.

### 2. BookRepository.java
* **Purpose**: Interface extending `JpaRepository` to leverage automatic query methods.
* **Complete Code**: See [BookRepository.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_9/src/main/java/com/library/BookRepository.java).

### 3. BookController.java
* **Purpose**: Exposes REST endpoints directly mapping requests to JpaRepository CRUD actions.
* **Complete Code**: See [BookController.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_9/src/main/java/com/library/BookController.java).
* **Line-by-Line Explanation**:
  - `@RestController`: Configures class as a web controller returning serialized objects (JSON).
  - `@GetMapping`: Processes HTTP GET requests.
  - `@PostMapping`: Processes HTTP POST requests.
  - `@PutMapping`: Processes HTTP PUT updates.
  - `@DeleteMapping`: Processes HTTP DELETE actions.

### 4. application.properties
* **Purpose**: Declares H2 connection parameters and Hibernate options.
* **Complete Code**: See [application.properties](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/LibraryManagement/Exercise_9/src/main/resources/application.properties).

---

## How to Run
Navigate to `LibraryManagement/Exercise_9/` in terminal and run:
```bash
mvn spring-boot:run
```
Once started:
* The H2 console will be available at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:librarydb`, User: `sa`).
* The API endpoints can be tested via cURL or Postman on `http://localhost:8080/api/books`.

---

## REST Endpoints & Sample JSON Payloads

### 1. Create a Book (POST `/api/books`)
* **Request JSON**:
  ```json
  {
    "title": "Clean Code",
    "author": "Robert C. Martin"
  }
  ```
* **Expected Response (`201 Created`)**:
  ```json
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin"
  }
  ```

### 2. Fetch All Books (GET `/api/books`)
* **Expected Response (`200 OK`)**:
  ```json
  [
    {
      "id": 1,
      "title": "Clean Code",
      "author": "Robert C. Martin"
    }
  ]
  ```

---

## Annotations Explained
1. **`@RestController`**: Combines `@Controller` and `@ResponseBody` to mark classes as REST endpoints returning serialized data.
2. **`@RequestMapping`**: Configures base routing path mappings.
3. **`@Entity`**: Informs the Hibernate engine to map this Java bean to a matching database table.
4. **`@Id`**: Specifies the primary key of the entity.
5. **`@GeneratedValue`**: Defines how the primary key values are generated.

---

## Conclusion
Spring Boot greatly simplifies development by using autoconfigurations to set up DataSource connection pools, JPA properties, and web dispatchers automatically.
