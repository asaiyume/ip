import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    protected String originalFromString;
    protected String originalToString;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.originalFromString = from;
        this.originalToString = to;
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
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

    public String getFrom() {
        if (from != null) {
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");
            return from.format(displayFormatter);
        } else {
            return originalFromString;
        }
    }

    public String getTo() {
        if (to != null) {
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");
            return to.format(displayFormatter);
        } else {
            return originalToString;
        }
    }

    public String getFromForSaving() {
        return originalFromString;
    }

    public String getToForSaving() {
        return originalToString;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + getFrom() + " to: " + getTo() + ")";
    }
}