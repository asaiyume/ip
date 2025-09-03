# Zbot

Zbot is a task management chatbot application built in Java. It allows users to manage their tasks through a command-line interface with persistent storage.

## Features

- **Task Management**: Create, list, mark, unmark, and delete tasks
- **Task Types**: Support for three types of tasks:
  - **Todo**: Simple tasks with descriptions
  - **Deadline**: Tasks with due dates (format: `/by <time>`)  
  - **Event**: Tasks with time ranges (format: `/from <start> /to <end>`)
- **Search**: Find tasks by keyword
- **Persistent Storage**: Tasks are automatically saved to and loaded from `./data/zbot.txt`

## Commands

- `list` - Show all tasks
- `todo <description>` - Add a todo task
- `deadline <description> /by <time>` - Add a deadline task
- `event <description> /from <start> /to <end>` - Add an event task
- `mark <task_number>` - Mark a task as done
- `unmark <task_number>` - Mark a task as not done
- `delete <task_number>` - Delete a task
- `find <keyword>` - Search for tasks containing the keyword
- `bye` - Exit the application

## Setup and Running

### Prerequisites
- JDK 17 or higher
- IntelliJ IDEA (recommended) or any Java IDE

### Setup in IntelliJ
1. Open IntelliJ (close existing projects if needed)
2. Click `Open` and select this project directory
3. Configure the project to use **JDK 17**:
   - Go to File → Project Structure → Project Settings → Project
   - Set Project SDK to JDK 17
   - Set Project language level to SDK default
4. Locate `src/main/java/zbot/Zbot.java`, right-click and choose `Run Zbot.main()`

### Running with Gradle
```bash
# Run the application
./gradlew run

# Build the JAR file
./gradlew shadowJar

# Run tests
./gradlew test
```

### Running the JAR
```bash
java -jar zbot.jar
```

## Project Structure

```
src/
├── main/java/zbot/
│   ├── Zbot.java           # Main application class
│   ├── command/            # Command type definitions
│   ├── parser/             # Input parsing logic
│   ├── storage/            # File I/O operations
│   ├── task/               # Task classes (Task, Todo, Deadline, Event)
│   ├── tasklist/           # Task list management
│   └── ui/                 # User interface handling
└── test/java/zbot/         # Unit tests
```

## Example Usage

```
Hello! I'm Zbot
What can I do for you?

> todo read book
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.

> deadline submit report /by 2023-12-01
Got it. I've added this task:
[D][ ] submit report (by: 2023-12-01)
Now you have 2 tasks in the list.

> list
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: 2023-12-01)

> mark 1
Nice! I've marked this task as done:
[T][X] read book

> bye
Bye. Hope to see you again soon!
```
