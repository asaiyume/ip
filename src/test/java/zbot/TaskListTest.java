package zbot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import zbot.task.*;
import zbot.tasklist.TaskList;

public class TaskListTest {
    
    private TaskList taskList;
    
    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }
    
    @Test
    public void testEmptyTaskList() {
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.getSize());
    }
    
    @Test
    public void testAddTask() {
        Todo todo = new Todo("test task");
        taskList.addTask(todo);
        
        assertFalse(taskList.isEmpty());
        assertEquals(1, taskList.getSize());
        assertEquals(todo, taskList.getTask(0));
    }
    
    @Test
    public void testDeleteTask() {
        Todo todo = new Todo("test task");
        taskList.addTask(todo);
        taskList.deleteTask(0);
        
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.getSize());
    }
    
    @Test
    public void testMarkUnmarkTask() {
        Todo todo = new Todo("test task");
        taskList.addTask(todo);
        
        taskList.markTask(0);
        assertTrue(taskList.getTask(0).isDone());
        
        taskList.unmarkTask(0);
        assertFalse(taskList.getTask(0).isDone());
    }
}