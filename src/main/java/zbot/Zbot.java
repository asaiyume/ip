package zbot;

import zbot.command.CommandType;
import zbot.parser.Parser;
import zbot.storage.Storage;
import zbot.task.*;
import zbot.tasklist.TaskList;
import zbot.ui.Ui;
import java.util.ArrayList;

public class Zbot {
    private TaskList tasks;
    private Storage storage;
    private Ui ui;
    
    public Zbot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.loadTasks());
    }
    
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
        CommandType command = Parser.parseCommand(input);
        
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
            case FIND:
                handleFind(input);
                break;
            case UNKNOWN:
            default:
                ui.showError("I'm sorry, but I don't know what that means :-(");
                break;
        }
    }
    
    private void handleList() {
        ui.showTaskList(tasks);
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
    
    public static void main(String[] args) {
        new Zbot("./data/zbot.txt").run();
    }
}