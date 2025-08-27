package zbot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import zbot.command.CommandType;
import zbot.parser.Parser;

public class ParserTest {
    
    @Test
    public void testParseBasicCommands() {
        assertEquals(CommandType.LIST, Parser.parseCommand("list"));
        assertEquals(CommandType.BYE, Parser.parseCommand("bye"));
        assertEquals(CommandType.TODO, Parser.parseCommand("todo read book"));
        assertEquals(CommandType.UNKNOWN, Parser.parseCommand("invalid"));
    }
    
    @Test
    public void testParseMarkCommands() {
        assertEquals(CommandType.MARK, Parser.parseCommand("mark 1"));
        assertEquals(CommandType.UNMARK, Parser.parseCommand("unmark 2"));
        assertEquals(CommandType.DELETE, Parser.parseCommand("delete 3"));
    }
    
    @Test
    public void testExtractTodoDescription() {
        assertEquals("read book", Parser.extractTodoDescription("todo read book"));
        assertEquals("", Parser.extractTodoDescription("todo"));
    }
    
    @Test
    public void testExtractDeadlineParts() {
        String[] parts = Parser.extractDeadlineParts("deadline submit report /by Friday");
        assertNotNull(parts);
        assertEquals("submit report", parts[0]);
        assertEquals("Friday", parts[1]);
        
        assertNull(Parser.extractDeadlineParts("deadline invalid format"));
    }
}