import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {
    private final String filePath;
    
    public Storage(String filePath) {
        this.filePath = filePath;
    }
    
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            File dataFile = new File(filePath);
            if (!dataFile.exists()) {
                return tasks; // Return empty list if no saved data
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line;
            
            while ((line = reader.readLine()) != null) {
                Task task = parseTaskFromString(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
        return tasks;
    }
    
    public void saveTasks(ArrayList<Task> tasks) {
        try {
            File dataDir = new File(filePath).getParentFile();
            if (dataDir != null && !dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
                writer.write(taskToSaveString(task) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }
    
    private String taskToSaveString(Task task) {
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