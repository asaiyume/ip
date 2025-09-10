package zbot.storage;

import zbot.task.Task;
import zbot.task.Todo;
import zbot.task.Deadline;
import zbot.task.Event;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        assert filePath != null : "File path cannot be null";
        assert !filePath.isEmpty() : "File path cannot be empty";
        this.filePath = filePath;
    }

    public ArrayList<Task> loadTasks() {
        try {
            File dataFile = new File(filePath);
            if (!dataFile.exists()) {
                return new ArrayList<>(); // Return empty list if no saved data
            }

            return Files.lines(Paths.get(filePath))
                    .map(this::parseTaskFromString)
                    .filter(task -> task != null)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveTasks(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list cannot be null";
        try {
            File dataDir = new File(filePath).getParentFile();
            if (dataDir != null && !dataDir.exists()) {
                dataDir.mkdirs();
            }

            String content = tasks.stream()
                    .map(this::taskToSaveString)
                    .collect(Collectors.joining("\n"));

            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            if (!content.isEmpty()) {
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private String taskToSaveString(Task task) {
        assert task != null : "Task cannot be null";
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

    private Task parseTaskFromString(String line) {
        assert line != null : "Line cannot be null";
        assert !line.isEmpty() : "Line cannot be empty";
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
}

