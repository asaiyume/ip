package zbot.parser;

import zbot.command.CommandType;

public class Parser {
    private Parser() {
        // Utility class should not be instantiated
    }

    public static CommandType parseCommand(String input) {
        assert input != null : "Input cannot be null";
        return CommandType.fromString(input);
    }

    public static String extractTaskNumber(String input, String command) {
        assert input != null : "Input cannot be null";
        assert command != null : "Command cannot be null";
        assert input.length() >= command.length() : "Input must be at least as long as command";
        return input.substring(command.length()).trim();
    }

    public static String extractTodoDescription(String input) {
        assert input != null : "Input cannot be null";
        assert input.startsWith("todo") : "Input must start with 'todo'";
        if (input.equals("todo")) {
            return "";
        }
        return input.substring(5).trim();
    }

    public static String[] extractDeadlineParts(String input) {
        assert input != null : "Input cannot be null";
        assert input.startsWith("deadline ") : "Input must start with 'deadline '";
        String content = input.substring(9);
        if (!content.contains(" /by ")) {
            return null;
        }
        String[] parts = content.split(" /by ", 2);
        String description = parts[0].trim();
        String by = parts[1].trim();
        assert !description.isEmpty() : "Description cannot be empty";
        assert !by.isEmpty() : "By date cannot be empty";
        return new String[]{description, by};
    }

    public static String[] extractEventParts(String input) {
        assert input != null : "Input cannot be null";
        assert input.startsWith("event ") : "Input must start with 'event '";
        String content = input.substring(6);
        if (!content.contains(" /from ") || !content.contains(" /to ")) {
            return null;
        }
        String[] fromSplit = content.split(" /from ", 2);
        String description = fromSplit[0].trim();
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        String from = toSplit[0].trim();
        String to = toSplit[1].trim();
        assert !description.isEmpty() : "Description cannot be empty";
        assert !from.isEmpty() : "From time cannot be empty";
        assert !to.isEmpty() : "To time cannot be empty";
        return new String[]{description, from, to};
    }

    public static String extractFindKeyword(String input) {
        assert input != null : "Input cannot be null";
        assert input.startsWith("find") : "Input must start with 'find'";
        if (input.equals("find")) {
            return "";
        }
        return input.substring(5).trim();
    }
}

