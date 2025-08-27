package zbot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import zbot.task.*;

public class TaskTest {
    
    @Test
    public void testTodoCreation() {
        Todo todo = new Todo("test task");
        assertEquals("test task", todo.getDescription());
        assertFalse(todo.isDone());
    }
    
    @Test
    public void testTaskCompletion() {
        Todo todo = new Todo("test task");
        todo.markAsDone();
        assertTrue(todo.isDone());
        
        todo.markAsUndone();
        assertFalse(todo.isDone());
    }
    
    @Test
    public void testDeadlineCreation() {
        Deadline deadline = new Deadline("submit report", "Friday");
        assertEquals("submit report", deadline.getDescription());
        assertEquals("Friday", deadline.getByForSaving());
    }
    
    @Test
    public void testEventCreation() {
        Event event = new Event("meeting", "2pm", "3pm");
        assertEquals("meeting", event.getDescription());
        assertEquals("2pm", event.getFromForSaving());
        assertEquals("3pm", event.getToForSaving());
    }
}