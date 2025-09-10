package zbot.tasklist;

import zbot.task.Task;
import java.util.ArrayList;

/**
 * Manages a collection of tasks with operations for adding, removing,
 * marking, and searching tasks. Provides a centralized way to handle
 * task operations and maintain the task list state.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates a new empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList with the given list of tasks.
     *
     * @param tasks The initial list of tasks to manage
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list cannot be null";
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add to the list
     */
    public void addTask(Task task) {
        assert task != null : "Task cannot be null";
        tasks.add(task);
    }

    /**
     * Retrieves a task at the specified index.
     *
     * @param index The zero-based index of the task
     * @return The task at the specified index, or null if index is invalid
     */
    public Task getTask(int index) {
        assert index >= 0 : "Index cannot be negative";
        if (isValidIndex(index)) {
            return tasks.get(index);
        }
        return null;
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to remove
     */
    public void deleteTask(int index) {
        assert index >= 0 : "Index cannot be negative";
        if (isValidIndex(index)) {
            tasks.remove(index);
        }
    }

    /**
     * Checks if the given index is valid for this task list.
     *
     * @param index The index to validate
     * @return true if index is valid, false otherwise
     */
    private boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    public int getSize() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void markTask(int index) {
        Task task = getTask(index);
        if (task != null) {
            task.markAsDone();
        }
    }

    public void unmarkTask(int index) {
        Task task = getTask(index);
        if (task != null) {
            task.markAsUndone();
        }
    }

    public ArrayList<Task> findTasks(String keyword) {
        assert keyword != null : "Keyword cannot be null";
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    public void sortTasks() {
        tasks.sort((task1, task2) -> task1.getDescription().compareToIgnoreCase(task2.getDescription()));
    }
}

