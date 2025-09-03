package zbot.ui;

import zbot.task.Task;
import zbot.tasklist.TaskList;
import java.util.Scanner;
import java.util.ArrayList;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("Hello! I'm Zbot");
        System.out.println("What can I do for you?");
    }

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showLine(String message) {
        System.out.println(message);
    }

    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in your list yet!");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.getSize(); i++) {
                System.out.println((i + 1) + ". " + tasks.getTask(i));
            }
        }
    }

    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " task" + (totalTasks == 1 ? "" : "s") + " in the list.");
    }

    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    public void showTaskDeleted(Task task, int remainingTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + remainingTasks + " task" + (remainingTasks == 1 ? "" : "s") + " in the list.");
    }

    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    public void showFormattedMessage(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public void showFormattedError(String format, Object... args) {
        System.out.println("OOPS!!! " + String.format(format, args));
    }

    public void showTaskDetails(String action, Task task, Object... details) {
        System.out.println(action);
        System.out.println("  " + task);
        for (Object detail : details) {
            System.out.println(detail);
        }
    }

    public void showFindResults(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + ". " + matchingTasks.get(i));
            }
        }
    }

    public void close() {
        scanner.close();
    }
}

