package zbot.task;

public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType taskType;

    /**
     * Constructs a new Task with the specified description and type.
     * @param description The description of the task
     * @param taskType The type of the task
     */
    public Task(String description, TaskType taskType) {
        this.description = description;
        this.isDone = false;
        this.taskType = taskType;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public boolean isDone() {
        return isDone;
    }

    public String getDescription() {
        return description;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsUndone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return taskType.getPrefix() + "[" + getStatusIcon() + "] " + description;
    }
}

