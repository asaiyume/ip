package zbot.parser;

import zbot.command.CommandType;

/**
 * Utility class for parsing user input commands and extracting relevant information.
 * This class provides static methods to parse and extract data from user commands
 * such as task numbers, descriptions, and command types.
 */
public class Parser {

    private Parser() {
        // Utility class should not be instantiated
    }

    /**
     * Parses the input string and determines the command type.
     *
     * @param input The raw user input string
     * @return The corresponding CommandType, or UNKNOWN if not recognized
     */
    public static CommandType parseCommand(String input) {
        return CommandType.fromString(input);
    }

    /**
     * Extracts the task number from a command string.
     *
     * @param input The full command input (e.g., "mark 2")
     * @param command The command prefix to remove (e.g., "mark")
     * @return The task number portion as a string
     */
    public static String extractTaskNumber(String input, String command) {
        return input.substring(command.length()).trim();
    }

    /**
     * Extracts the description from a todo command.
     *
     * @param input The full todo command (e.g., "todo read book")
     * @return The description portion, or empty string if just "todo"
     */
    public static String extractTodoDescription(String input) {
        if (input.equals("todo")) {
            return "";
        }
        return input.substring(5).trim();
    }

    /**
     * Extracts the description and deadline from a deadline command.
     *
     * @param input The full deadline command (e.g., "deadline submit report /by 2023-10-15")
     * @return Array containing [description, by] or null if format is invalid
     */
    public static String[] extractDeadlineParts(String input) {
        String content = input.substring(9);
        if (!content.contains(" /by ")) {
            return null;
        }
        String[] parts = content.split(" /by ", 2);
        String description = parts[0].trim();
        String by = parts[1].trim();
        return new String[]{description, by};
    }

    /**
     * Extracts the description, start time, and end time from an event command.
     *
     * @param input The full event command (e.g., "event meeting /from 2pm /to 4pm")
     * @return Array containing [description, from, to] or null if format is invalid
     */
    public static String[] extractEventParts(String input) {
        String content = input.substring(6);
        if (!content.contains(" /from ") || !content.contains(" /to ")) {
            return null;
        }
        String[] fromSplit = content.split(" /from ", 2);
        String description = fromSplit[0].trim();
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        String from = toSplit[0].trim();
        String to = toSplit[1].trim();
        return new String[]{description, from, to};
    }

    /**
     * Extracts the search keyword from a find command.
     *
     * @param input The full find command (e.g., "find book")
     * @return The keyword to search for, or empty string if just "find"
     */
    public static String extractFindKeyword(String input) {
        if (input.equals("find")) {
            return "";
        }
        return input.substring(5).trim();
    }
}

