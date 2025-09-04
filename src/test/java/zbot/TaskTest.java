package zbot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import zbot.task.*;

public class TaskTest {
    
    @Test
    public void todoCreation_newTask_setsDescriptionAndNotDone() {
        Todo todo = new Todo("test task");
        assertEquals("test task", todo.getDescription());
        assertFalse(todo.isDone());
    }
    
    @Test
    public void markAsDone_todoTask_changesStatusToComplete() {
        Todo todo = new Todo("test task");
        todo.markAsDone();
        assertTrue(todo.isDone());
        
        todo.markAsUndone();
        assertFalse(todo.isDone());
    }
    
    @Test
    public void deadlineCreation_validInput_setsDescriptionAndBy() {
        Deadline deadline = new Deadline("submit report", "Friday");
        assertEquals("submit report", deadline.getDescription());
        assertEquals("Friday", deadline.getByForSaving());
    }
    
    @Test
    public void eventCreation_validInput_setsDescriptionAndTimes() {
        Event event = new Event("meeting", "2pm", "3pm");
        assertEquals("meeting", event.getDescription());
        assertEquals("2pm", event.getFromForSaving());
        assertEquals("3pm", event.getToForSaving());
    }
}