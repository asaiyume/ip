package zbot.task;

public enum TaskType {
    TODO, DEADLINE, EVENT;

    public static TaskType fromString(String type) {
        switch (type.toUpperCase()) {
            case "TODO":
                return TODO;
            case "DEADLINE":
                return DEADLINE;  
            case "EVENT":
                return EVENT;
            default:
                return TODO; // Default to TODO if not recognized
        }
    }

    public String getPrefix() {
        switch (this) {
            case TODO:
                return "[T]";
            case DEADLINE:
                return "[D]";
            case EVENT:
                return "[E]";
            default:
                return "[T]";
        }
    }
}