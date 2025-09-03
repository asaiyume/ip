package zbot;

import zbot.command.CommandType;
import zbot.parser.Parser;
import zbot.storage.Storage;
import zbot.task.Task;
import zbot.task.Todo;
import zbot.task.Deadline;
import zbot.task.Event;
import zbot.tasklist.TaskList;
import zbot.ui.Ui;
import java.util.ArrayList;

public class Zbot {
    private TaskList tasks;
    private Storage storage;
    private Ui ui;

    /**
     * Constructs a new Zbot instance with the specified data file path.
     * @param filePath The path to the data file for storing tasks
     */
    public Zbot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.loadTasks());
    }

    /**
     * Starts the main application loop, handling user commands until bye command.
     */
    public void run() {
        ui.showWelcome();

        String input;
        while (!(input = ui.readCommand()).equals("bye")) {
            if (input.isEmpty()) {
                ui.showError("Please enter a command.");
            } else {
                handleCommand(input);
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    private void handleCommand(String input) {
        String response = getResponse(input);
        System.out.println(response);
    }

    /**
     * Generates a response for the given user input.
     * @param input The user input
     * @return The response string
     */
    public String getResponse(String input) {
        CommandType command = Parser.parseCommand(input);

        switch (command) {
            case LIST:
                return handleListResponse();
            case MARK:
                return handleMarkResponse(input);
            case UNMARK:
                return handleUnmarkResponse(input);
            case DELETE:
                return handleDeleteResponse(input);
            case TODO:
                return handleTodoResponse(input);
            case DEADLINE:
                return handleDeadlineResponse(input);
            case EVENT:
                return handleEventResponse(input);
            case FIND:
                return handleFindResponse(input);
            case BYE:
                return "Bye. Hope to see you again soon!";
            case UNKNOWN:
            default:
                return "I'm sorry, but I don't know what that means :-(";
        }
    }

    private void handleList() {
        ui.showTaskList(tasks);
    }

    private String handleListResponse() {
        if (tasks.isEmpty()) {
            return "No tasks in your list yet!";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.getSize(); i++) {
                sb.append((i + 1)).append(". ").append(tasks.getTask(i)).append("\n");
            }
            return sb.toString().trim();
        }
    }

    private void handleMark(String input) {
        try {
            String numberStr = Parser.extractTaskNumber(input, "mark");
            if (numberStr.isEmpty()) {
                ui.showError("The task number for mark command cannot be empty.");
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= tasks.getSize()) {
                    ui.showError("Task number " + (taskIndex + 1) + " does not exist.");
                } else {
                    tasks.markTask(taskIndex);
                    ui.showTaskMarked(tasks.getTask(taskIndex));
                    storage.saveTasks(tasks.getTasks());
                }
            }
        } catch (NumberFormatException e) {
            ui.showError("Please provide a valid task number for mark command.");
        }
    }

    private void handleUnmark(String input) {
        try {
            String numberStr = Parser.extractTaskNumber(input, "unmark");
            if (numberStr.isEmpty()) {
                ui.showError("The task number for unmark command cannot be empty.");
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= tasks.getSize()) {
                    ui.showError("Task number " + (taskIndex + 1) + " does not exist.");
                } else {
                    tasks.unmarkTask(taskIndex);
                    ui.showTaskUnmarked(tasks.getTask(taskIndex));
                    storage.saveTasks(tasks.getTasks());
                }
            }
        } catch (NumberFormatException e) {
            ui.showError("Please provide a valid task number for unmark command.");
        }
    }

    private void handleDelete(String input) {
        try {
            String numberStr = Parser.extractTaskNumber(input, "delete");
            if (numberStr.isEmpty()) {
                ui.showError("The task number for delete command cannot be empty.");
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= tasks.getSize()) {
                    ui.showError("Task number " + (taskIndex + 1) + " does not exist.");
                } else {
                    Task deletedTask = tasks.getTask(taskIndex);
                    tasks.deleteTask(taskIndex);
                    ui.showTaskDeleted(deletedTask, tasks.getSize());
                    storage.saveTasks(tasks.getTasks());
                }
            }
        } catch (NumberFormatException e) {
            ui.showError("Please provide a valid task number for delete command.");
        }
    }

    private void handleTodo(String input) {
        String description = Parser.extractTodoDescription(input);
        if (description.isEmpty()) {
            ui.showError("The description of a todo cannot be empty.");
        } else {
            Task task = new Todo(description);
            tasks.addTask(task);
            ui.showTaskAdded(task, tasks.getSize());
            storage.saveTasks(tasks.getTasks());
        }
    }

    private void handleDeadline(String input) {
        if (input.equals("deadline")) {
            ui.showError("The description of a deadline cannot be empty.");
        } else {
            try {
                String[] parts = Parser.extractDeadlineParts(input);
                if (parts == null) {
                    ui.showError("Please specify the deadline with '/by' format.");
                } else {
                    String description = parts[0];
                    String by = parts[1];
                    if (description.isEmpty()) {
                        ui.showError("The description of a deadline cannot be empty.");
                    } else if (by.isEmpty()) {
                        ui.showError("The deadline time cannot be empty.");
                    } else {
                        Task task = new Deadline(description, by);
                        tasks.addTask(task);
                        ui.showTaskAdded(task, tasks.getSize());
                        storage.saveTasks(tasks.getTasks());
                    }
                }
            } catch (Exception e) {
                ui.showError("Please use the format: deadline <description> /by <time>");
            }
        }
    }

    private void handleEvent(String input) {
        if (input.equals("event")) {
            ui.showError("The description of an event cannot be empty.");
        } else {
            try {
                String[] parts = Parser.extractEventParts(input);
                if (parts == null) {
                    ui.showError("Please specify the event with '/from' and '/to' format.");
                } else {
                    String description = parts[0];
                    String from = parts[1];
                    String to = parts[2];

                    if (description.isEmpty()) {
                        ui.showError("The description of an event cannot be empty.");
                    } else if (from.isEmpty()) {
                        ui.showError("The event start time cannot be empty.");
                    } else if (to.isEmpty()) {
                        ui.showError("The event end time cannot be empty.");
                    } else {
                        Task task = new Event(description, from, to);
                        tasks.addTask(task);
                        ui.showTaskAdded(task, tasks.getSize());
                        storage.saveTasks(tasks.getTasks());
                    }
                }
            } catch (Exception e) {
                ui.showError("Please use the format: event <description> /from <start> /to <end>");
            }
        }
    }

    private void handleFind(String input) {
        String keyword = Parser.extractFindKeyword(input);
        if (keyword.isEmpty()) {
            ui.showError("Please specify a keyword to search for.");
        } else {
            ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
            ui.showFindResults(matchingTasks);
        }
    }

    private String handleMarkResponse(String input) {
        try {
            String numberStr = Parser.extractTaskNumber(input, "mark");
            if (numberStr.isEmpty()) {
                return "OOPS!!! The task number for mark command cannot be empty.";
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= tasks.getSize()) {
                    return "OOPS!!! Task number " + (taskIndex + 1) + " does not exist.";
                } else {
                    tasks.markTask(taskIndex);
                    storage.saveTasks(tasks.getTasks());
                    return "Nice! I've marked this task as done:\n  " + tasks.getTask(taskIndex);
                }
            }
        } catch (NumberFormatException e) {
            return "OOPS!!! Please provide a valid task number for mark command.";
        }
    }

    private String handleUnmarkResponse(String input) {
        try {
            String numberStr = Parser.extractTaskNumber(input, "unmark");
            if (numberStr.isEmpty()) {
                return "OOPS!!! The task number for unmark command cannot be empty.";
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= tasks.getSize()) {
                    return "OOPS!!! Task number " + (taskIndex + 1) + " does not exist.";
                } else {
                    tasks.unmarkTask(taskIndex);
                    storage.saveTasks(tasks.getTasks());
                    return "OK, I've marked this task as not done yet:\n  " + tasks.getTask(taskIndex);
                }
            }
        } catch (NumberFormatException e) {
            return "OOPS!!! Please provide a valid task number for unmark command.";
        }
    }

    private String handleDeleteResponse(String input) {
        try {
            String numberStr = Parser.extractTaskNumber(input, "delete");
            if (numberStr.isEmpty()) {
                return "OOPS!!! The task number for delete command cannot be empty.";
            } else {
                int taskIndex = Integer.parseInt(numberStr) - 1;
                if (taskIndex < 0 || taskIndex >= tasks.getSize()) {
                    return "OOPS!!! Task number " + (taskIndex + 1) + " does not exist.";
                } else {
                    Task deletedTask = tasks.getTask(taskIndex);
                    tasks.deleteTask(taskIndex);
                    storage.saveTasks(tasks.getTasks());
                    return "Noted. I've removed this task:\n  " + deletedTask + "\n"
                            + "Now you have " + tasks.getSize() + " task"
                            + (tasks.getSize() == 1 ? "" : "s") + " in the list.";
                }
            }
        } catch (NumberFormatException e) {
            return "OOPS!!! Please provide a valid task number for delete command.";
        }
    }

    private String handleTodoResponse(String input) {
        String description = Parser.extractTodoDescription(input);
        if (description.isEmpty()) {
            return "OOPS!!! The description of a todo cannot be empty.";
        } else {
            Task task = new Todo(description);
            tasks.addTask(task);
            storage.saveTasks(tasks.getTasks());
            return "Got it. I've added this task:\n  " + task + "\n"
                    + "Now you have " + tasks.getSize() + " task"
                    + (tasks.getSize() == 1 ? "" : "s") + " in the list.";
        }
    }

    private String handleDeadlineResponse(String input) {
        if (input.equals("deadline")) {
            return "OOPS!!! The description of a deadline cannot be empty.";
        } else {
            try {
                String[] parts = Parser.extractDeadlineParts(input);
                if (parts == null) {
                    return "OOPS!!! Please specify the deadline with '/by' format.";
                } else {
                    String description = parts[0];
                    String by = parts[1];
                    if (description.isEmpty()) {
                        return "OOPS!!! The description of a deadline cannot be empty.";
                    } else if (by.isEmpty()) {
                        return "OOPS!!! The deadline time cannot be empty.";
                    } else {
                        Task task = new Deadline(description, by);
                        tasks.addTask(task);
                        storage.saveTasks(tasks.getTasks());
                        return "Got it. I've added this task:\n  " + task + "\n"
                                + "Now you have " + tasks.getSize() + " task"
                                + (tasks.getSize() == 1 ? "" : "s") + " in the list.";
                    }
                }
            } catch (Exception e) {
                return "OOPS!!! Please use the format: deadline <description> /by <time>";
            }
        }
    }

    private String handleEventResponse(String input) {
        if (input.equals("event")) {
            return "OOPS!!! The description of an event cannot be empty.";
        } else {
            try {
                String[] parts = Parser.extractEventParts(input);
                if (parts == null) {
                    return "OOPS!!! Please specify the event with '/from' and '/to' format.";
                } else {
                    String description = parts[0];
                    String from = parts[1];
                    String to = parts[2];

                    if (description.isEmpty()) {
                        return "OOPS!!! The description of an event cannot be empty.";
                    } else if (from.isEmpty()) {
                        return "OOPS!!! The event start time cannot be empty.";
                    } else if (to.isEmpty()) {
                        return "OOPS!!! The event end time cannot be empty.";
                    } else {
                        Task task = new Event(description, from, to);
                        tasks.addTask(task);
                        storage.saveTasks(tasks.getTasks());
                        return "Got it. I've added this task:\n  " + task + "\n"
                                + "Now you have " + tasks.getSize() + " task"
                                + (tasks.getSize() == 1 ? "" : "s") + " in the list.";
                    }
                }
            } catch (Exception e) {
                return "OOPS!!! Please use the format: event <description> /from <start> /to <end>";
            }
        }
    }

    private String handleFindResponse(String input) {
        String keyword = Parser.extractFindKeyword(input);
        if (keyword.isEmpty()) {
            return "OOPS!!! Please specify a keyword to search for.";
        } else {
            ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
            if (matchingTasks.isEmpty()) {
                return "No matching tasks found.";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Here are the matching tasks in your list:\n");
                for (int i = 0; i < matchingTasks.size(); i++) {
                    sb.append((i + 1)).append(". ").append(matchingTasks.get(i)).append("\n");
                }
                return sb.toString().trim();
            }
        }
    }

    public static void main(String[] args) {
        new Zbot("./data/zbot.txt").run();
    }
}

