import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Zbot {
    private static Task[] tasks = new Task[100];
    private static int taskCount = 0;
    private static final String DATA_FILE_PATH = "./data/zbot.txt";
    
    private static void loadTasksFromFile() {
        try {
            File dataFile = new File(DATA_FILE_PATH);
            if (!dataFile.exists()) {
                return; // No saved data yet
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line;
            taskCount = 0;
            
            while ((line = reader.readLine()) != null && taskCount < tasks.length) {
                Task task = parseTaskFromString(line);
                if (task != null) {
                    tasks[taskCount] = task;
                    taskCount++;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
    }
    
    private static void saveTasksToFile() {
        try {
            File dataDir = new File("./data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            FileWriter writer = new FileWriter(DATA_FILE_PATH);
            for (int i = 0; i < taskCount; i++) {
                writer.write(taskToSaveString(tasks[i]) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }
    
    private static String taskToSaveString(Task task) {
        if (task instanceof Todo) {
            return "T | " + (task.isDone() ? "1" : "0") + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + (task.isDone() ? "1" : "0") + " | " + task.getDescription() + " | " + deadline.getByForSaving();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + (task.isDone() ? "1" : "0") + " | " + task.getDescription() + " | " + event.getFromForSaving() + " | " + event.getToForSaving();
        }
        return "";
    }
    
    private static Task parseTaskFromString(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }
        
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        
        Task task = null;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length >= 4) {
                    task = new Deadline(description, parts[3]);
                }
                break;
            case "E":
                if (parts.length >= 5) {
                    task = new Event(description, parts[3], parts[4]);
                }
                break;
        }
        
        if (task != null && isDone) {
            task.markAsDone();
        }
        
        return task;
    }

    private static void handleCommand(CommandType command, String input) {
        switch (command) {
            case LIST:
                handleList();
                break;
            case MARK:
                handleMark(input);
                break;
            case UNMARK:
                handleUnmark(input);
                break;
            case DELETE:
                handleDelete(input);
                break;
            case TODO:
                handleTodo(input);
                break;
            case DEADLINE:
                handleDeadline(input);
                break;
            case EVENT:
                handleEvent(input);
                break;
            case UNKNOWN:
            default:
                System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
                break;
        }
    }

    private static void handleList() {
        if (taskCount == 0) {
            System.out.println("No tasks in your list yet!");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < taskCount; i++) {
                System.out.println((i + 1) + ". " + tasks[i]);
            }
        }
    }

    private static void handleMark(String input) {
        try {
            String numberStr = input.substring(5).trim();
            if (numberStr.isEmpty()) {
                System.out.println("OOPS!!! The task number for mark command cannot be empty.");
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= taskCount) {
                    System.out.println("OOPS!!! Task number " + (taskIndex + 1) + " does not exist.");
                } else {
                    tasks[taskIndex].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskIndex]);
                    saveTasksToFile();
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("OOPS!!! Please provide a valid task number for mark command.");
        }
    }

    private static void handleUnmark(String input) {
        try {
            String numberStr = input.substring(7).trim();
            if (numberStr.isEmpty()) {
                System.out.println("OOPS!!! The task number for unmark command cannot be empty.");
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= taskCount) {
                    System.out.println("OOPS!!! Task number " + (taskIndex + 1) + " does not exist.");
                } else {
                    tasks[taskIndex].markAsUndone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskIndex]);
                    saveTasksToFile();
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("OOPS!!! Please provide a valid task number for unmark command.");
        }
    }

    private static void handleDelete(String input) {
        try {
            String numberStr = input.substring(7).trim();
            if (numberStr.isEmpty()) {
                System.out.println("OOPS!!! The task number for delete command cannot be empty.");
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= taskCount) {
                    System.out.println("OOPS!!! Task number " + (taskIndex + 1) + " does not exist.");
                } else {
                    Task deletedTask = tasks[taskIndex];
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + deletedTask);
                    
                    // Shift remaining tasks left
                    for (int i = taskIndex; i < taskCount - 1; i++) {
                        tasks[i] = tasks[i + 1];
                    }
                    taskCount--;
                    
                    System.out.println("Now you have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
                    saveTasksToFile();
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("OOPS!!! Please provide a valid task number for delete command.");
        }
    }

    private static void handleTodo(String input) {
        if (input.equals("todo")) {
            System.out.println("OOPS!!! The description of a todo cannot be empty.");
        } else {
            String description = input.substring(5).trim();
            if (description.isEmpty()) {
                System.out.println("OOPS!!! The description of a todo cannot be empty.");
            } else {
                Task task = new Todo(description);
                tasks[taskCount] = task;
                taskCount++;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + task);
                System.out.println("Now you have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
                saveTasksToFile();
            }
        }
    }

    private static void handleDeadline(String input) {
        if (input.equals("deadline")) {
            System.out.println("OOPS!!! The description of a deadline cannot be empty.");
        } else {
            try {
                String content = input.substring(9);
                if (!content.contains(" /by ")) {
                    System.out.println("OOPS!!! Please specify the deadline with '/by' format.");
                } else {
                    String[] parts = content.split(" /by ", 2);
                    String description = parts[0].trim();
                    String by = parts[1].trim();
                    if (description.isEmpty()) {
                        System.out.println("OOPS!!! The description of a deadline cannot be empty.");
                    } else if (by.isEmpty()) {
                        System.out.println("OOPS!!! The deadline time cannot be empty.");
                    } else {
                        Task task = new Deadline(description, by);
                        tasks[taskCount] = task;
                        taskCount++;
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + task);
                        System.out.println("Now you have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
                        saveTasksToFile();
                    }
                }
            } catch (Exception e) {
                System.out.println("OOPS!!! Please use the format: deadline <description> /by <time>");
            }
        }
    }

    private static void handleEvent(String input) {
        if (input.equals("event")) {
            System.out.println("OOPS!!! The description of an event cannot be empty.");
        } else {
            try {
                String content = input.substring(6);
                if (!content.contains(" /from ") || !content.contains(" /to ")) {
                    System.out.println("OOPS!!! Please specify the event with '/from' and '/to' format.");
                } else {
                    String[] fromSplit = content.split(" /from ", 2);
                    String description = fromSplit[0].trim();
                    String[] toSplit = fromSplit[1].split(" /to ", 2);
                    String from = toSplit[0].trim();
                    String to = toSplit[1].trim();
                    
                    if (description.isEmpty()) {
                        System.out.println("OOPS!!! The description of an event cannot be empty.");
                    } else if (from.isEmpty()) {
                        System.out.println("OOPS!!! The event start time cannot be empty.");
                    } else if (to.isEmpty()) {
                        System.out.println("OOPS!!! The event end time cannot be empty.");
                    } else {
                        Task task = new Event(description, from, to);
                        tasks[taskCount] = task;
                        taskCount++;
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + task);
                        System.out.println("Now you have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
                        saveTasksToFile();
                    }
                }
            } catch (Exception e) {
                System.out.println("OOPS!!! Please use the format: event <description> /from <start> /to <end>");
            }
        }
    }
    
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        
        System.out.println("Hello! I'm Zbot");
        System.out.println("What can I do for you?");
        
        loadTasksFromFile();
        
        Scanner scanner = new Scanner(System.in);
        String input;
        
        while (!(input = scanner.nextLine()).equals("bye")) {
            input = input.trim();
            if (input.isEmpty()) {
                System.out.println("OOPS!!! Please enter a command.");
            } else {
                CommandType command = CommandType.fromString(input);
                handleCommand(command, input);
            }
        }
        
        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}