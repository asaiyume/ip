import java.util.Scanner;

public class Zbot {
    private static Task[] tasks = new Task[100];
    private static int taskCount = 0;

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