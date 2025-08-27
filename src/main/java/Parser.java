public class Parser {
    public static CommandType parseCommand(String input) {
        return CommandType.fromString(input);
    }
    
    public static String extractTaskNumber(String input, String command) {
        return input.substring(command.length()).trim();
    }
    
    public static String extractTodoDescription(String input) {
        if (input.equals("todo")) {
            return "";
        }
        return input.substring(5).trim();
    }
    
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
}