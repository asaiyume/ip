import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    protected LocalDateTime by;
    protected String originalByString;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.originalByString = by;
        this.by = parseDateTime(by);
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            // Try parsing format like "2/12/2019 1800"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e1) {
            try {
                // Try parsing format like "Dec 2 2019 6pm"
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMM d yyyy h'pm'");
                return LocalDateTime.parse(dateTimeString, formatter2);
            } catch (DateTimeParseException e2) {
                // If parsing fails, return null and use original string
                return null;
            }
        }
    }

    public String getBy() {
        if (by != null) {
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");
            return by.format(displayFormatter);
        } else {
            return originalByString;
        }
    }

    public String getByForSaving() {
        return originalByString;
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + getBy() + ")";
    }
}