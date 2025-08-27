package zbot.command;

public enum CommandType {
    BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, UNKNOWN;

    public static CommandType fromString(String command) {
        command = command.trim().toLowerCase();
        
        if (command.equals("bye")) {
            return BYE;
        } else if (command.equals("list")) {
            return LIST;
        } else if (command.startsWith("mark ")) {
            return MARK;
        } else if (command.startsWith("unmark ")) {
            return UNMARK;
        } else if (command.startsWith("delete ")) {
            return DELETE;
        } else if (command.equals("todo") || command.startsWith("todo ")) {
            return TODO;
        } else if (command.equals("deadline") || command.startsWith("deadline ")) {
            return DEADLINE;
        } else if (command.equals("event") || command.startsWith("event ")) {
            return EVENT;
        } else if (command.equals("find") || command.startsWith("find ")) {
            return FIND;
        } else {
            return UNKNOWN;
        }
    }
}