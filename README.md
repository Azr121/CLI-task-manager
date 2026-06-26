# CLI Task Manager

A command-line application for managing tasks, built in Java 21 using Maven.
Supports creating, viewing, completing, and deleting tasks with file-based 
persistence across sessions.

---

## Architecture

This project follows a layered architecture where each package has a single, 
defined responsibility. No layer reaches past its neighbour.

User Input → ConsoleUI → TaskService → TaskRepository → tasks.txt

↕

Task / Priority

### `model/` — Data
Defines what a Task is. `Task.java` holds the data (title, priority, 
completion status, creation date) with private fields and controlled access 
via getters/setters. `Priority.java` is an enum constraining priority to 
exactly three values: LOW, MEDIUM, HIGH — eliminating the possibility of 
invalid states like "Urgent" or "urgent" creeping into the system.

### `repository/` — Storage
`TaskRepository.java` is the only class that touches the file system. It 
serialises Task objects to a plain text file and deserialises them back on 
load. Keeping I/O isolated here means the rest of the application has no 
knowledge of how or where data is stored — swap the file for a database 
later and nothing else changes.

### `service/` — Business Logic  
`TaskService.java` sits between the UI and the repository. It enforces the 
rules: no duplicate titles, no completing a task that's already complete, 
no empty task names. The UI asks the service to do things; the service 
decides whether those things are valid before delegating to the repository.

### `ui/` — Console Interface
`ConsoleUI.java` handles all user interaction — printing menus, reading 
input, and displaying results. It knows nothing about files or business 
rules. If this were a web application, only this layer would be replaced.

### `Main.java` — Entry Point
Wires the layers together and starts the application. Intentionally minimal.

---

## Data Flow Example

When a user adds a task:
1. `ConsoleUI` reads the title and priority from the console
2. `TaskService` validates the input and creates a `Task` object
3. `TaskRepository` appends the serialised task to `tasks.txt`
4. On next startup, `TaskRepository` reads `tasks.txt` and rebuilds the list

---

## Technologies

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 21 LTS | Core language |
| Maven | 3.x | Build and dependency management |
| JUnit 5 | 5.10.0 | Unit testing |

---

## How to Run

**Prerequisites:** Java 21+, Maven 3.x

```bash
git clone https://github.com/Azr121/cCi-task-manager.git
cd cCi-task-manager
mvn compile
mvn exec:java -Dexec.mainClass="com.azr121.taskmanager.Main"
```

**Run tests:**
```bash
mvn test
```

---

## Features

- [ ] Add a task with title and priority
- [ ] View all tasks
- [ ] Mark a task complete
- [ ] Delete a task
- [ ] File persistence across sessions
- [ ] Input validation